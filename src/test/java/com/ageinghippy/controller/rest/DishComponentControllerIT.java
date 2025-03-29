package com.ageinghippy.controller.rest;

import com.ageinghippy.model.dto.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.hamcrest.Matchers.matchesPattern;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
public class DishComponentControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    EntityManager entityManager;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final String baseUrl = "/api/v1/dish-components";

    @Test
    @Order(1)
    @WithUserDetails("basic")
    void getAll() throws Exception {
        MvcResult result = mockMvc.perform(get(baseUrl + "?dishId=1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        List<DishComponentDTO> resultList =
                objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
                });

        assertEquals(4, resultList.size());
        //check a portion of the contents
        assert (resultList.contains(
                new DishComponentDTO(
                        1L,
                        new FoodTypeDTOSimple(1L, "foodType1", "Food Type one Description"),
                        100)));
        assert (resultList.contains(
                new DishComponentDTO(
                        2L,
                        new FoodTypeDTOSimple(2L, "foodType2", "Food Type two Description"),
                        100)));
        assert (resultList.contains(
                new DishComponentDTO(
                        3L,
                        new FoodTypeDTOSimple(3L, "foodType3", "Food Type three Description"),
                        100)));
        assert (resultList.contains(
                new DishComponentDTO(
                        4L,
                        new FoodTypeDTOSimple(4L, "foodType4", "Food Type four Description"),
                        100)));

    }

    @Test
    @Order(2)
    @WithUserDetails("basic")
    void getOne_success() throws Exception {
        Long id = 3L;
        MvcResult result = mockMvc.perform(get(baseUrl + "/{id}", id))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        DishComponentDTO resultDto =
                objectMapper.readValue(result.getResponse().getContentAsString(), DishComponentDTO.class);

        assertEquals(resultDto,
                new DishComponentDTO(
                        3L,
                        new FoodTypeDTOSimple(3L, "foodType3", "Food Type three Description"),
                        100));
    }

    @Test
    @Order(3)
    @WithUserDetails("basic")
    void getOne_notFound() throws Exception {
        Long id = 99L;
        mockMvc.perform(get(baseUrl + "/{id}", id))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails("basic")
    void create_success() throws Exception {
        String requestJson = """
                {
                  "foodType": {"id":5},
                  "proportion":75
                }""";

        MvcResult result = mockMvc.perform(post(baseUrl + "?dishId=1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", matchesPattern(".*" + baseUrl + "/\\d+")))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        //verify response is new category
        DishComponentDTO resultDto =
                objectMapper.readValue(result.getResponse().getContentAsString(), DishComponentDTO.class);
        assertNotNull(resultDto.id());
        assertEquals(
                new DishComponentDTO(
                        resultDto.id(),
                        new FoodTypeDTOSimple(5L, "foodType5", "Food Type five Description"),
                        75),
                resultDto
        );

        //and verify dishComponent is in fact inserted into the database
        Integer value = (Integer) entityManager.createQuery(
                        "SELECT proportion FROM DishComponent WHERE id = " + resultDto.id())
                .getSingleResult();
        assertEquals(75, value);
    }

    @Test
    @WithUserDetails("basic")
    void create_failure_invalidFoodType() throws Exception {
        String requestJson = """
                {
                  "foodType": {"id":99},
                  "proportion":99
                }""";

        mockMvc.perform(post(baseUrl + "?dishId=1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails("basic")
    void create_failure_invalidDish() throws Exception {
        String requestJson = """
                {
                  "foodType": {"id":1},
                  "proportion":101
                }""";

        mockMvc.perform(post(baseUrl + "?dishId=99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails("basic")
    void update_success() throws Exception {
        String requestJson = """
                {
                  "id":3,
                  "foodType": {"id":15},
                  "proportion":101
                  }
                }""";

        MvcResult result = mockMvc.perform(put(baseUrl + "/{id}", 3L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        //verify response is updated foodType
        DishComponentDTO resultDto =
                objectMapper.readValue(result.getResponse().getContentAsString(), DishComponentDTO.class);
        assertEquals(
                new DishComponentDTO(
                        resultDto.id(),
                        new FoodTypeDTOSimple(15L, "foodType15", "Food Type fifteen Description"),
                        101),
                resultDto
        );

        //and verify category is in fact updated in the database
        Long id = (Long) entityManager.createQuery(
                        "SELECT foodType.id FROM DishComponent WHERE id = 3")
                .getSingleResult();
        assertEquals(15L, id);
    }

    @Test
    @WithUserDetails("basic")
    void update_failure_notFound() throws Exception {
        String requestJson = """
                {
                  "id":99,
                  "foodType": {"id":15},
                  "proportion":101
                }""";

        mockMvc.perform(put(baseUrl + "/{id}", 99L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails("basic")
    void update_failure_badRequest() throws Exception {
        String requestJson = """
                {
                  "id":10,
                  "foodType": {"id":15},
                  "proportion":101
                }""";

        mockMvc.perform(put(baseUrl + "/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andDo(print())
                .andExpect(status().isBadRequest());

        Long id = (Long) entityManager.createQuery(
                        "SELECT foodType.id FROM DishComponent WHERE id = 1")
                .getSingleResult();
        assertEquals(1L, id);
    }

    @Test
    @Transactional
    @WithUserDetails("basic")
    void delete_success() throws Exception {

        //WHEN the delete endpoint is called
        mockMvc.perform(delete(baseUrl + "/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());

        //and verify the record has been deleted from the database
        Long count = (Long) entityManager.createQuery("SELECT Count(*) FROM DishComponent where id = 1").getSingleResult();
        assertEquals(count, 0L);
    }

    @Test
    @WithUserDetails("basic")
    void delete_failure_notfound() throws Exception {
        mockMvc.perform(delete(baseUrl + "/{id}", 99L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }
}
