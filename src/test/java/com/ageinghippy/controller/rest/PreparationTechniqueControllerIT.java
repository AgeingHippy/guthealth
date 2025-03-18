package com.ageinghippy.controller.rest;

import com.ageinghippy.GutHealthApplication;
import com.ageinghippy.model.dto.PreparationTechniqueDTO;
import com.ageinghippy.model.entity.PreparationTechnique;
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
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = GutHealthApplication.class)
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
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
        assert (resultList.contains(new PreparationTechniqueDTO("PrepType1", "Preparation type one description")));
        assert (resultList.contains(new PreparationTechniqueDTO("PrepType2", "Preparation type two description")));
        assert (resultList.contains(new PreparationTechniqueDTO("PrepType3", "Preparation type three description")));
        assert (resultList.contains(new PreparationTechniqueDTO("PrepType4", "Preparation type four description")));
    }

    @Test
    @Order(2)
    void getPreparationTechnique_found() throws Exception {
        MvcResult result = mockMvc.perform(get(baseUrl + "/{code}", "PrepType2"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        PreparationTechniqueDTO resultDto = objectMapper.readValue(result.getResponse().getContentAsString(), PreparationTechniqueDTO.class);

        assertEquals(resultDto, new PreparationTechniqueDTO("PrepType2", "Preparation type two description"));
    }

    @Test
    void getPreparationTechnique_notFound() throws Exception {
        mockMvc.perform(get(baseUrl + "/{code}", "PrepTypeZ"))
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
                        .andExpect(header().string("Location",matchesPattern(".*" +baseUrl + "/newCode")))
                        .andReturn();

        PreparationTechniqueDTO preparationTechniqueDTO = objectMapper.readValue(result.getResponse().getContentAsString(), PreparationTechniqueDTO.class);

        //THEN
        //Correct entity is returned
        assertEquals(new PreparationTechniqueDTO("newCode", "newDescription"), preparationTechniqueDTO);

        //Entity is correctly updated in the database
        String description = entityManager.createQuery(
                        "SELECT description FROM PreparationTechnique WHERE code = 'newCode'")
                .getSingleResult().toString();
        assertEquals(description, "newDescription");
    }

    @Test
    void createPreparationTechnique_failure() throws Exception {
        mockMvc.perform(post(baseUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"code\":\"PrepType1\",\"description\":\"description one\"}"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void updatePreparationTechnique_success() throws Exception {
        MvcResult result =
                mockMvc.perform(put(baseUrl + "/{code}", "PrepType1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"description\":\"nextDescription\"}"))
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andReturn();

        PreparationTechniqueDTO preparationTechniqueDTO = objectMapper.readValue(result.getResponse().getContentAsString(), PreparationTechniqueDTO.class);

        //Correct entity is returned
        assertEquals(preparationTechniqueDTO.description(), "nextDescription");

        //Entity is correctly updated in the database
        String description = entityManager.createQuery(
                        "SELECT description FROM PreparationTechnique WHERE code = 'PrepType1'")
                .getSingleResult().toString();
        assertEquals(description, "nextDescription");
    }

    @Test
    void updatePreparationTechnique_failure_notFound() throws Exception {
        mockMvc.perform(put(baseUrl + "/{code}", "InvalidCode")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"description\":\"newDescription\"}"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void updatePreparationTechnique_failure_badRequest() throws Exception {
        mockMvc.perform(put(baseUrl + "/{code}", "PrepType1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"code\":\"PrepType1\",\"description\":\"changedDescription\"}"))
                .andDo(print())
                .andExpect(status().isBadRequest());

        //and validate entity not updated
        //Entity is correctly updated in the database
        String description = entityManager.createQuery(
                        "SELECT description FROM PreparationTechnique WHERE code = 'PrepType1'")
                .getSingleResult().toString();
        assertNotEquals(description, "changedDescription");
    }


    @Test
    @Transactional
    void deletePreparationTechnique_success() throws Exception {
        //given a specific record exists in the database
        entityManager.persist(PreparationTechnique.builder()
                .code("testCode")
                .description("testDesc")
                .build());

        //WHEN the delete endpoint is called
        mockMvc.perform(delete(baseUrl + "/{code}", "testCode")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    void deletePreparationTechnique_failure_notfound() throws Exception {
        mockMvc.perform(delete(baseUrl + "/{code}", "InvalidCode")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void deletePreparationTechnique_failure_constraintViolation() throws Exception {
        mockMvc.perform(delete(baseUrl + "/{code}", "PrepType1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

}