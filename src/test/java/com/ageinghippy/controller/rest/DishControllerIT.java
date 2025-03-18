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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
public class DishControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    EntityManager entityManager;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final String baseUrl = "/api/v1/dishes";

    @Test
    @Order(1)
    void getAll() throws Exception {
        MvcResult result = mockMvc.perform(get(baseUrl))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        List<DishDTOSimple> resultList =
                objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
                });

        assertEquals(resultList.size(), 4);
        //check a portion of the contents
        assert (resultList.contains(
                new DishDTOSimple(1L, "Dish1", "Dish one Description",
                        new PreparationTechniqueDTO("PrepType1", "Preparation type one description"))));
        assert (resultList.contains(
                new DishDTOSimple(2L, "Dish2", "Dish two Description",
                        new PreparationTechniqueDTO("PrepType1", "Preparation type one description"))));
        assert (resultList.contains(
                new DishDTOSimple(3L, "Dish3", "Dish three Description",
                        new PreparationTechniqueDTO("PrepType2", "Preparation type two description"))));
        assert (resultList.contains(
                new DishDTOSimple(4L, "Dish4", "Dish four Description",
                        new PreparationTechniqueDTO("PrepType3", "Preparation type three description"))));
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

        DishDTOComplex resultDto =
                objectMapper.readValue(result.getResponse().getContentAsString(), DishDTOComplex.class);

        assertEquals(resultDto,
                new DishDTOComplex(
                        3L,
                        "Dish3",
                        "Dish three Description",
                        new PreparationTechniqueDTO("PrepType2", "Preparation type two description"),
                        List.of(
                                new DishComponentDTO(
                                        9L,
                                        new FoodTypeDTOSimple(1L, "foodType1", "Food Type one Description"),
                                        1),
                                new DishComponentDTO(
                                        10L,
                                        new FoodTypeDTOSimple(10L, "foodType10", "Food Type ten Description"),
                                        2)
                        )
                ));

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
                  "name":"newDish",
                  "description":"newDish description",
                  "preparationTechnique": {
                    "code": "PrepType2"
                  }
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
        DishDTOComplex resultDto =
                objectMapper.readValue(result.getResponse().getContentAsString(), DishDTOComplex.class);
        assertNotNull(resultDto.id());
        assertEquals(resultDto,
                new DishDTOComplex(
                        resultDto.id(),
                        "newDish",
                        "newDish description",
                        new PreparationTechniqueDTO("PrepType2", "Preparation type two description"),
                        List.of()
                ));

        //and verify dish is in fact inserted into the database
        String fetchedName = entityManager.createQuery(
                        "SELECT name FROM Dish WHERE id = " + resultDto.id())
                .getSingleResult().toString();
        assertEquals(fetchedName, "newDish");
    }

    @Test
    void create_failure_alreadyExists() throws Exception {
        String requestJson = """
                {
                  "name":"Dish2",
                  "description":"newDish2 description",
                  "preparationTechnique": {
                    "code": "PrepType2"
                  }
                }""";

        mockMvc.perform(post(baseUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andDo(print())
                .andExpect(status().isBadRequest());

        //and verify original dish is unchanged in the database
        String fetchedDescription = entityManager.createQuery(
                        "SELECT description FROM Dish WHERE name = 'Dish2'")
                .getSingleResult().toString();
        assertEquals(fetchedDescription, "Dish two Description");
    }

    @Test
    void create_failure_invalidPreparationTechnique() throws Exception {
        String requestJson = """
                {
                  "name":"Dish99",
                  "description":"newDish99 description",
                  "preparationTechnique": {
                    "code": "PrepType99"
                  }
                }""";

        mockMvc.perform(post(baseUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andDo(print())
                .andExpect(status().isBadRequest());

        //and verify dish is not in the database
        Long count = (Long) entityManager.createQuery(
                        "SELECT count(*) FROM Dish WHERE name = 'Dish99'")
                .getSingleResult();
        assertEquals(0, count);
    }
}
