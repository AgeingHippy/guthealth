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
import org.springframework.test.context.ActiveProfiles;
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

@SpringBootTest
@ActiveProfiles("h2")
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
    @WithUserDetails("basic")
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
                        new PreparationTechniqueDTO(1L,"PrepType1", "Preparation type one description"))));
        assert (resultList.contains(
                new DishDTOSimple(2L, "Dish2", "Dish two Description",
                        new PreparationTechniqueDTO(1L,"PrepType1", "Preparation type one description"))));
        assert (resultList.contains(
                new DishDTOSimple(3L, "Dish3", "Dish three Description",
                        new PreparationTechniqueDTO(2L,"PrepType2", "Preparation type two description"))));
        assert (resultList.contains(
                new DishDTOSimple(4L, "Dish4", "Dish four Description",
                        new PreparationTechniqueDTO(3L,"PrepType3", "Preparation type three description"))));
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

        DishDTOComplex resultDto =
                objectMapper.readValue(result.getResponse().getContentAsString(), DishDTOComplex.class);

        assertEquals(resultDto,
                new DishDTOComplex(
                        3L,
                        "Dish3",
                        "Dish three Description",
                        new PreparationTechniqueDTO(2L,"PrepType2", "Preparation type two description"),
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
    @WithUserDetails("basic")
    void getOne_notFound() throws Exception {
        Long id = 99L;
        mockMvc.perform(get(baseUrl + "/{id}", id))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails("basic")
    void create_noDishComponents_success() throws Exception {
        String requestJson = """
                {
                  "name":"newDish",
                  "description":"newDish description",
                  "preparationTechnique": {
                    "id":2
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
                        new PreparationTechniqueDTO(2L,"PrepType2", "Preparation type two description"),
                        List.of()
                ));

        //and verify dish is in fact inserted into the database
        String fetchedName = entityManager.createQuery(
                        "SELECT name FROM Dish WHERE id = " + resultDto.id())
                .getSingleResult().toString();
        assertEquals(fetchedName, "newDish");

        //and verify dish has been created with principle_id = 2 ('basic' user)
        Long principleId = (Long) entityManager.createQuery(
                        "SELECT principle.id FROM Dish WHERE id = " + resultDto.id())
                .getSingleResult();
        assertEquals(2,principleId);
    }

    @Test
    @WithUserDetails("basic")
    void create_withDishComponents_success() throws Exception {
        String requestJson = """
                {
                  "name":"newDish2",
                  "description":"newDish2 description",
                  "preparationTechnique": {
                    "id":2
                  },
                  "dishComponents" : [
                    {
                        "foodType": {"id":1},
                        "proportion":100
                    },
                    {
                        "foodType": {"id":3},
                        "proportion":30
                    },
                    {
                        "foodType": {"id":5},
                        "proportion":5
                    }
                  ]
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
                        "newDish2",
                        "newDish2 description",
                        new PreparationTechniqueDTO(2L,"PrepType2", "Preparation type two description"),
                        List.of(
                                new DishComponentDTO(
                                        resultDto.dishComponents().stream().filter(dc -> dc.foodType().id().equals(1L)).findFirst().orElseThrow().id(),
                                        new FoodTypeDTOSimple(1L, "foodType1", "Food Type one Description"),
                                        100),
                                new DishComponentDTO(
                                        resultDto.dishComponents().stream().filter(dc -> dc.foodType().id().equals(3L)).findFirst().orElseThrow().id(),
                                        new FoodTypeDTOSimple(3L, "foodType3", "Food Type three Description"),
                                        30),
                                new DishComponentDTO(
                                        resultDto.dishComponents().stream().filter(dc -> dc.foodType().id().equals(5L)).findFirst().orElseThrow().id(),
                                        new FoodTypeDTOSimple(5L, "foodType5", "Food Type five Description"),
                                        5)
                        )
                ));

        //and verify dish is in fact inserted into the database
        String fetchedName = entityManager.createQuery(
                        "SELECT name FROM Dish WHERE id = " + resultDto.id())
                .getSingleResult().toString();
        assertEquals("newDish2", fetchedName);

        //and dishComponents inserted
        Long count = (Long) entityManager.createQuery(
                        "SELECT Count(*) FROM DishComponent WHERE dish.id = " + resultDto.id())
                .getSingleResult();
        assertEquals(3, count);
    }

    @Test
    @WithUserDetails("basic")
    void create_withDishComponents_failure_invalidFoodTypeOwner() throws Exception {
        //foodType16 belongs to a different user
        String requestJson = """
                {
                  "name":"newDish3",
                  "description":"newDish3 description",
                  "preparationTechnique": {
                    "id":2
                  },
                  "dishComponents" : [
                    {
                        "foodType": {"id":1},
                        "proportion":100
                    },
                    {
                        "foodType": {"id":3},
                        "proportion":30
                    },
                    {
                        "foodType": {"id":16},
                        "proportion":5
                    }
                  ]
                }""";

        mockMvc.perform(post(baseUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithUserDetails("basic")
    void create_failure_alreadyExists() throws Exception {
        String requestJson = """
                {
                  "name":"Dish2",
                  "description":"newDish2 description",
                  "preparationTechnique": {
                    "id":2
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
    @WithUserDetails("basic")
    void create_failure_invalidPreparationTechnique() throws Exception {
        String requestJson = """
                {
                  "name":"Dish99",
                  "description":"newDish99 description",
                  "preparationTechnique": {
                    "id":99
                  }
                }""";

        mockMvc.perform(post(baseUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andDo(print())
                .andExpect(status().isNotFound());

        //and verify dish is not in the database
        Long count = (Long) entityManager.createQuery(
                        "SELECT count(*) FROM Dish WHERE name = 'Dish99'")
                .getSingleResult();
        assertEquals(0, count);
    }

    @Test
    @WithUserDetails("basic")
    void update_success() throws Exception {
        String requestJson = """
                {
                  "id":3,
                  "name":"Dish3ChangedName",
                  "description":"Dish3 changed description",
                  "preparationTechnique": {
                    "id":3
                  }
                }""";

        MvcResult result = mockMvc.perform(put(baseUrl + "/{id}", 3L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        //verify response is updated foodType
        DishDTOComplex resultDto =
                objectMapper.readValue(result.getResponse().getContentAsString(), DishDTOComplex.class);
        assertEquals(
                new DishDTOComplex(
                        3L,
                        "Dish3ChangedName",
                        "Dish3 changed description",
                        new PreparationTechniqueDTO(3L,"PrepType3", "Preparation type three description"),
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
                ),
                resultDto
        );

        //and verify category is in fact updated in the database
        String fetchedName = entityManager.createQuery(
                        "SELECT name FROM Dish WHERE id = 3")
                .getSingleResult().toString();
        assertEquals(fetchedName, "Dish3ChangedName");
    }

    @Test
    @WithUserDetails("basic")
    void update_failure_notFound() throws Exception {
        String requestJson = """
                {
                  "id":99,
                  "name":"Dish99ChangedName"
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
                    "id":2,
                    "name":"Dish2UpdatedName"
                }""";

        mockMvc.perform(put(baseUrl + "/{id}", 3L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andDo(print())
                .andExpect(status().isBadRequest());

        String fetchedName = entityManager.createQuery(
                        "SELECT name FROM Dish WHERE id = 2")
                .getSingleResult().toString();
        assertEquals(fetchedName, "Dish2");
    }

    @Test
    @Transactional
    @WithUserDetails("basic")
    void delete_success_noChildren() throws Exception {

        //WHEN the delete endpoint is called
        mockMvc.perform(delete(baseUrl + "/{id}", 4)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());

        //and verify the record has been deleted from the database
        Long count = (Long) entityManager.createQuery("SELECT Count(*) FROM Dish where id = 4").getSingleResult();
        assertEquals(count, 0L);
    }

    @Test
    @Transactional
    @WithUserDetails("basic")
    void delete_success_hasChildren() throws Exception {

        //WHEN the delete endpoint is called
        mockMvc.perform(delete(baseUrl + "/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());

        //and verify the record has been deleted from the database
        Long count = (Long) entityManager.createQuery("SELECT Count(*) FROM Dish where id = 1").getSingleResult();
        assertEquals(count, 0L);

        //verify children also deleted
        count = (Long) entityManager.createQuery("SELECT Count(*) FROM DishComponent where dish.id = 1").getSingleResult();
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
