package com.ageinghippy.controller.rest;

import com.ageinghippy.GutHealthApplication;
import com.ageinghippy.model.dto.FoodCategoryDTOComplex;
import com.ageinghippy.model.dto.FoodCategoryDTOSimple;
import com.ageinghippy.model.entity.FoodCategory;
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
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.hamcrest.Matchers.matchesPattern;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = GutHealthApplication.class)
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
public class FoodCategoryControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    EntityManager entityManager;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final String baseUrl = "/api/v1/food-categories";

    @Test
    @Order(1)
    void getAll() throws Exception {
        MvcResult result = mockMvc.perform(get(baseUrl))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        List<FoodCategoryDTOSimple> resultList =
                objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
                });

        List<String> foodCategoryNames = resultList.stream().map(FoodCategoryDTOSimple::name).toList();

        assertEquals(resultList.size(), 5);
        assert (resultList.contains(new FoodCategoryDTOSimple(1L,"foodCategory1_name","Food Category one description")));
        assert (resultList.contains(new FoodCategoryDTOSimple(2L,"foodCategory2_name","Food Category two description")));
        assert (resultList.contains(new FoodCategoryDTOSimple(3L,"foodCategory3_name","Food Category three description")));
        assert (resultList.contains(new FoodCategoryDTOSimple(4L,"foodCategory4_name","Food Category four description")));
        assert (resultList.contains(new FoodCategoryDTOSimple(5L,"foodCategory5_name","Food Category five description")));
    }

    @Test
    @Order(2)
    void getOne_success() throws Exception {
        Long foodCategoryId = 3L;
        MvcResult result = mockMvc.perform(get(baseUrl + "/{id}", foodCategoryId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        FoodCategoryDTOComplex resultDto =
                objectMapper.readValue(result.getResponse().getContentAsString(), FoodCategoryDTOComplex.class);

        assertEquals(resultDto.name(), "foodCategory3_name");
        assertEquals(resultDto.foodTypes().size(),4);
    }

    @Test
    @Order(3)
    void getOne_notFound() throws Exception {
        Long foodCategoryId = 99L;
        mockMvc.perform(get(baseUrl + "/{id}", foodCategoryId))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void create_success() throws Exception {
        String newFoodCategoryJson = """
                {
                  "name":"anotherCategory",
                  "description":"anotherDescription"
                }""";

        MvcResult result = mockMvc.perform(post(baseUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newFoodCategoryJson))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", matchesPattern(".*" + baseUrl + "/\\d+")))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        //verify response is new category
        FoodCategoryDTOComplex resultDto =
                objectMapper.readValue(result.getResponse().getContentAsString(), FoodCategoryDTOComplex.class);
        assertNotNull(resultDto.id());
        assertEquals(resultDto.name(), "anotherCategory");
        assertEquals(resultDto.foodTypes().size(),0);

        //and verify category is in fact inserted into the database
        String fetchedName = entityManager.createQuery(
                        "SELECT name FROM FoodCategory WHERE id = " + resultDto.id())
                .getSingleResult().toString();
        assertEquals(fetchedName, "anotherCategory");
    }

    @Test
    void create_failure_alreadyExists() throws Exception {
        String newFoodCategoryJson = """
                {
                  "name":"foodCategory1_name",
                  "description":"Duplicated category"
                }""";

        mockMvc.perform(post(baseUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newFoodCategoryJson))
                .andDo(print())
                .andExpect(status().isBadRequest());

        //and verify original category is unchanged in the database
        String fetchedDescription = entityManager.createQuery(
                        "SELECT description FROM FoodCategory WHERE name = 'foodCategory1_name'")
                .getSingleResult().toString();
        assertEquals(fetchedDescription, "Food Category one description");
    }

    @Test
    void update_success() throws Exception {
        String newFoodCategoryJson = """
                {
                  "id":1,
                  "name":"foodCategory1_name_updated",
                  "description":"Updated description here"
                }""";

        MvcResult result = mockMvc.perform(put(baseUrl + "/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newFoodCategoryJson))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        //verify response is new category
        FoodCategoryDTOComplex resultDto =
                objectMapper.readValue(result.getResponse().getContentAsString(), FoodCategoryDTOComplex.class);
        assertEquals(resultDto.id(),1L);
        assertEquals(resultDto.name(), "foodCategory1_name_updated");
        assertEquals(resultDto.description(), "Updated description here");
        assertEquals(resultDto.foodTypes().size(),5);

        //and verify category is in fact updated in the database
        String fetchedName = entityManager.createQuery(
                        "SELECT name FROM FoodCategory WHERE id = 1")
                .getSingleResult().toString();
        assertEquals(fetchedName, "foodCategory1_name_updated");
    }

    @Test
    void update_failure_notFound() throws Exception {
        String newFoodCategoryJson = """
                {
                  "id":99,
                  "name":"name_updated",
                  "description":"Another updated description here"
                }""";

        mockMvc.perform(put(baseUrl + "/{id}", 99L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newFoodCategoryJson))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void update_failure_badRequest() throws Exception {
        String newFoodCategoryJson = """
                {
                    "id":2,
                    "name":"name_updated",
                    "description":"Another updated description here"
                }""";

        mockMvc.perform(put(baseUrl + "/{id}", 3L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newFoodCategoryJson))
                .andDo(print())
                .andExpect(status().isBadRequest());

        String fetchedName = entityManager.createQuery(
                        "SELECT name FROM FoodCategory WHERE id = 2")
                .getSingleResult().toString();
        assertEquals(fetchedName, "foodCategory2_name");
    }

    @Test
    @Transactional
    void delete_success() throws Exception {
        //given a specific record exists in the database
        entityManager.persist(FoodCategory.builder()
                .name("testName")
                .description("testDesc")
                .build());

        Long foodCategoryId = (Long) entityManager.createQuery("SELECT id FROM FoodCategory where name = 'testName'").getSingleResult();

        //WHEN the delete endpoint is called
        mockMvc.perform(delete(baseUrl + "/{id}", foodCategoryId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());

        //and verify the record has been deleted from the database
        Long count = (Long) entityManager.createQuery("SELECT Count(*) FROM FoodCategory where name = 'testName'").getSingleResult();
        assertEquals(count,0L);
    }

    @Test
    void delete_failure_notfound() throws Exception {
        mockMvc.perform(delete(baseUrl + "/{id}", 99L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void delete_failure_constraintViolation() throws Exception {
        mockMvc.perform(delete(baseUrl + "/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
}
