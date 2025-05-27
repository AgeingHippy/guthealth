package com.ageinghippy.controller.rest;

import com.ageinghippy.GutHealthApplication;
import com.ageinghippy.model.dto.PreparationTechniqueDTO;
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
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = GutHealthApplication.class)
@ActiveProfiles("h2")
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
@WithUserDetails("basic")
class PreparationTechniqueControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    EntityManager entityManager;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final String baseUrl = "/api/v1/preparation-techniques";

    @Test
    @Order(1)
    void getPreparationTechniques() throws Exception {

        MvcResult result = mockMvc.perform(get(baseUrl))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        List<PreparationTechniqueDTO> resultList = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
        });

        assertEquals(resultList.size(), 4);
        assert (resultList.contains(new PreparationTechniqueDTO(1L, "PrepType1", "Preparation type one description")));
        assert (resultList.contains(new PreparationTechniqueDTO(2L, "PrepType2", "Preparation type two description")));
        assert (resultList.contains(new PreparationTechniqueDTO(3L, "PrepType3", "Preparation type three description")));
        assert (resultList.contains(new PreparationTechniqueDTO(4L, "PrepType4", "Preparation type four description")));
    }

    @Test
    @Order(2)
    void getPreparationTechnique_found() throws Exception {
        MvcResult result = mockMvc.perform(get(baseUrl + "/{id}", 2L))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        PreparationTechniqueDTO resultDto = objectMapper.readValue(result.getResponse().getContentAsString(), PreparationTechniqueDTO.class);

        assertEquals(resultDto, new PreparationTechniqueDTO(2L, "PrepType2", "Preparation type two description"));
    }

    @Test
    void getPreparationTechnique_notFound() throws Exception {
        mockMvc.perform(get(baseUrl + "/{id}", 99L))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void createPreparationTechnique_success() throws Exception {
        MvcResult result =
                mockMvc.perform(post(baseUrl)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"code\":\"newCode\",\"description\":\"newDescription\"}"))
                        .andDo(print())
                        .andExpect(status().isCreated())
                        .andExpect(header().string("Location", matchesPattern(".*" + baseUrl + "/\\d*")))
                        .andReturn();

        PreparationTechniqueDTO preparationTechniqueDTO = objectMapper.readValue(result.getResponse().getContentAsString(), PreparationTechniqueDTO.class);

        //THEN
        //Correct entity is returned
        assertEquals("newCode",preparationTechniqueDTO.code());
        assertEquals("newDescription",preparationTechniqueDTO.description());

        //Entity is correctly updated in the database
        String description = entityManager.createQuery(
                        "SELECT description FROM PreparationTechnique WHERE code = 'newCode' AND principle.id = 2")
                .getSingleResult().toString();
        assertEquals(description, "newDescription");
    }

    @Test
    void createPreparationTechnique_failure() throws Exception {
        String requestJson = """
                {   "id": "11",
                    "code":"PrepType11",
                    "description":"description eleven"
                }""";

        mockMvc.perform(post(baseUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void updatePreparationTechnique_success() throws Exception {
        String requestJson = """
                {   "id": 1,
                    "code":"PrepType1",
                    "description":"nextDescription"
                }""";

        MvcResult result =
                mockMvc.perform(put(baseUrl + "/{id}", 1L)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestJson))
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andReturn();

        PreparationTechniqueDTO preparationTechniqueDTO = objectMapper.readValue(result.getResponse().getContentAsString(), PreparationTechniqueDTO.class);

        //Correct entity is returned
        assertEquals(preparationTechniqueDTO.description(), "nextDescription");

        //Entity is correctly updated in the database
        String description = entityManager.createQuery(
                        "SELECT description FROM PreparationTechnique WHERE id=1")
                .getSingleResult().toString();
        assertEquals(description, "nextDescription");
    }

    @Test
    void updatePreparationTechnique_failure_notFound() throws Exception {
        String requestJson = """
                {   "id": 99,
                    "code":"InvalidCode",
                    "description":"name_updated"
                }""";

        mockMvc.perform(put(baseUrl + "/{id}", 99L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void updatePreparationTechnique_failure_badRequest() throws Exception {
        String requestJson = """
                {   "id": 1,
                    "code":"PrepType1",
                    "description":"Another updated description here"
                }""";

        mockMvc.perform(put(baseUrl + "/{id}", 2)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andDo(print())
                .andExpect(status().isBadRequest());

    }


    @Test
    @Transactional
    void deletePreparationTechnique_success() throws Exception {
        //given a specific record exists in the database

        //WHEN the delete endpoint is called
        mockMvc.perform(delete(baseUrl + "/{id}", 4L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    void deletePreparationTechnique_failure_notfound() throws Exception {
        mockMvc.perform(delete(baseUrl + "/{id}", 99L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void deletePreparationTechnique_failure_constraintViolation() throws Exception {
        mockMvc.perform(delete(baseUrl + "/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

}