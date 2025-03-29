package com.ageinghippy.controller.rest;

import com.ageinghippy.GutHealthApplication;
import com.ageinghippy.model.dto.FoodCategoryDTOSimple;
import com.ageinghippy.model.dto.FoodTypeDTOComplex;
import com.ageinghippy.model.dto.FoodTypeDTOSimple;
import com.ageinghippy.model.entity.FoodCategory;
import com.ageinghippy.model.entity.FoodType;
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
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = GutHealthApplication.class)
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
@WithUserDetails("basic")
public class FoodTypeControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    EntityManager entityManager;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final String baseUrl = "/api/v1/food-types";

    @Test
    @Order(1)
    void getAll() throws Exception {
        MvcResult result = mockMvc.perform(get(baseUrl))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        List<FoodTypeDTOSimple> resultList =
                objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
                });

        assertEquals(resultList.size(), 15);
        //check a portion of the contents
        assert (resultList.contains(new FoodTypeDTOSimple(1L, "foodType1", "Food Type one Description")));
        assert (resultList.contains(new FoodTypeDTOSimple(2L, "foodType2", "Food Type two Description")));
        assert (resultList.contains(new FoodTypeDTOSimple(3L, "foodType3", "Food Type three Description")));
        assert (resultList.contains(new FoodTypeDTOSimple(4L, "foodType4", "Food Type four Description")));
        assert (resultList.contains(new FoodTypeDTOSimple(5L, "foodType5", "Food Type five Description")));
        assert (resultList.contains(new FoodTypeDTOSimple(6L, "foodType6", "Food Type six Description")));
        assert (resultList.contains(new FoodTypeDTOSimple(7L, "foodType7", "Food Type seven Description")));
        assert (resultList.contains(new FoodTypeDTOSimple(8L, "foodType8", "Food Type eight Description")));
        assert (resultList.contains(new FoodTypeDTOSimple(9L, "foodType9", "Food Type nine Description")));
        assert (resultList.contains(new FoodTypeDTOSimple(10L, "foodType10", "Food Type ten Description")));
        assert (resultList.contains(new FoodTypeDTOSimple(11L, "foodType11", "Food Type eleven Description")));
        assert (resultList.contains(new FoodTypeDTOSimple(12L, "foodType12", "Food Type twelve Description")));
        assert (resultList.contains(new FoodTypeDTOSimple(13L, "foodType13", "Food Type thirteen Description")));
        assert (resultList.contains(new FoodTypeDTOSimple(14L, "foodType14", "Food Type fourteen Description")));
        assert (resultList.contains(new FoodTypeDTOSimple(15L, "foodType15", "Food Type fifteen Description")));
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

        FoodTypeDTOComplex resultDto =
                objectMapper.readValue(result.getResponse().getContentAsString(), FoodTypeDTOComplex.class);

        assertEquals(resultDto,
                new FoodTypeDTOComplex(3L,
                        new FoodCategoryDTOSimple(1L, "foodCategory1_name", "Food Category one description"),
                        "foodType3",
                        "Food Type three Description"));

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
        String newFoodTypeJson = """
                {
                  "name":"anotherName",
                  "description":"anotherDescription",
                  "foodCategory": {
                    "id": 2
                  }
                }""";

        MvcResult result = mockMvc.perform(post(baseUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newFoodTypeJson))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", matchesPattern(".*" + baseUrl + "/\\d+")))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        //verify response is new category
        FoodTypeDTOComplex resultDto =
                objectMapper.readValue(result.getResponse().getContentAsString(), FoodTypeDTOComplex.class);
        assertNotNull(resultDto.id());
        assertEquals(resultDto.name(), "anotherName");
        assertEquals(resultDto.description(), "anotherDescription");
        assertEquals(resultDto.foodCategory(),
                new FoodCategoryDTOSimple(2L, "foodCategory2_name", "Food Category two description"));

        //and verify category is in fact inserted into the database
        String fetchedName = entityManager.createQuery(
                        "SELECT name FROM FoodType WHERE id = " + resultDto.id())
                .getSingleResult().toString();
        assertEquals(fetchedName, "anotherName");
    }

    @Test
    void create_failure_alreadyExists() throws Exception {
        String newFoodTypeJson = """
                {
                  "name":"foodType4",
                  "description":"anotherDifferentDescription",
                  "foodCategory": {
                    "id": 1
                  }
                }""";

        mockMvc.perform(post(baseUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newFoodTypeJson))
                .andDo(print())
                .andExpect(status().isBadRequest());

        //and verify original category is unchanged in the database
        String fetchedDescription = entityManager.createQuery(
                        "SELECT description FROM FoodType WHERE name = 'foodType4'")
                .getSingleResult().toString();
        assertEquals(fetchedDescription, "Food Type four Description");
    }

    @Test
    void create_failure_invalidFoodCategory() throws Exception {
        String newFoodTypeJson = """
                {
                  "name":"thirdName",
                  "description":"thirdDescription",
                  "foodCategory": {
                    "id": 99
                  }
                }""";

        mockMvc.perform(post(baseUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newFoodTypeJson))
                .andDo(print())
                .andExpect(status().isBadRequest());

        //and verify food type is in fact not inserted into the database
        Long count = (Long) entityManager.createQuery("SELECT Count(*) FROM FoodType WHERE name = 'thirdName'")
                .getSingleResult();
        assertEquals(count, 0);
    }

    @Test
    void update_success() throws Exception {
        String newFoodTypeJson = """
                {
                  "id":1,
                  "name":"foodType1_NEW",
                  "description":"Food Type one Description_NEW",
                  "foodCategory": {
                    "id": 2
                  }
                }""";

        MvcResult result = mockMvc.perform(put(baseUrl + "/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newFoodTypeJson))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        //verify response is updated foodType
        FoodTypeDTOComplex resultDto =
                objectMapper.readValue(result.getResponse().getContentAsString(), FoodTypeDTOComplex.class);
        assertEquals(resultDto,
                new FoodTypeDTOComplex(1L,
                        new FoodCategoryDTOSimple(2L, "foodCategory2_name", "Food Category two description"),
                        "foodType1_NEW",
                        "Food Type one Description_NEW")
        );

        //and verify category is in fact updated in the database
        String fetchedName = entityManager.createQuery(
                        "SELECT name FROM FoodType WHERE id = 1")
                .getSingleResult().toString();
        assertEquals(fetchedName, "foodType1_NEW");
    }

    @Test
    void update_failure_notFound() throws Exception {
        String newFoodTypeJson = """
                {
                  "id":99,
                  "name":"name_updated",
                  "description":"Another updated description here"
                }""";

        mockMvc.perform(put(baseUrl + "/{id}", 99L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newFoodTypeJson))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void update_failure_badRequest() throws Exception {
        String requestJson = """
                {
                    "id":2,
                    "name":"name_updated",
                    "description":"Another updated description here"
                }""";

        mockMvc.perform(put(baseUrl + "/{id}", 3L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andDo(print())
                .andExpect(status().isBadRequest());

        String fetchedName = entityManager.createQuery(
                        "SELECT name FROM FoodType WHERE id = 2")
                .getSingleResult().toString();
        assertEquals(fetchedName, "foodType2");
    }

    @Test
    @Transactional
    void delete_success() throws Exception {
        //given a specific record exists in the database
        entityManager.persist(FoodType.builder()
                .name("testName")
                .description("testDesc")
                .foodCategory(FoodCategory.builder().id(1L).build())
                .build());

        Long id = (Long) entityManager.createQuery("SELECT id FROM FoodType where name = 'testName'").getSingleResult();

        //WHEN the delete endpoint is called
        mockMvc.perform(delete(baseUrl + "/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());

        //and verify the record has been deleted from the database
        Long count = (Long) entityManager.createQuery("SELECT Count(*) FROM FoodType where name = 'testName'").getSingleResult();
        assertEquals(count, 0L);
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
