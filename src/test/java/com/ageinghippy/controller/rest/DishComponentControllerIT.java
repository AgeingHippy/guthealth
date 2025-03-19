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
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

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
    void getAll() throws Exception {
        MvcResult result = mockMvc.perform(get(baseUrl + "?dishId=3"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        List<DishComponentDTO> resultList =
                objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
                });

        assertEquals(resultList.size(), 4);
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
    void getOne_notFound() throws Exception {
        Long id = 99L;
        mockMvc.perform(get(baseUrl + "/{id}", id))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void create_success() throws Exception {
        String requestJson = """
                {
                  "foodType": {"id":4},
                  "proportion":75
                }""";

        MvcResult result = mockMvc.perform(post(baseUrl)
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
        assertEquals(resultDto,
                new DishComponentDTO(
                        resultDto.id(),
                        new FoodTypeDTOSimple(4L, "foodType4", "Food Type four Description"),
                        75
                ));

        //and verify dishComponent is in fact inserted into the database
        int value = Integer.parseInt(entityManager.createQuery(
                        "SELECT proportion FROM DishComponent WHERE id = " + resultDto.id())
                .getSingleResult().toString());
        assertEquals(75, value);
    }

    @Test
    void create_failure_invalidFoodType() throws Exception {
        String requestJson = """
                {
                  "foodType": {"id":99},
                  "proportion":99
                }""";

        mockMvc.perform(post(baseUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
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
        assertEquals(resultDto,
                new DishComponentDTO(
                        resultDto.id(),
                        new FoodTypeDTOSimple(3L, "foodType15", "Food Type fifteen Description"),
                        101
                )
        );

        //and verify category is in fact updated in the database
        String fetchedName = entityManager.createQuery(
                        "SELECT name FROM Dish WHERE id = 3")
                .getSingleResult().toString();
        assertEquals(fetchedName, "Dish3ChangedName");
    }

    @Test
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
}
