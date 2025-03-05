package it.com.ageinghippy.controller;

import com.ageinghippy.GutHealthApplication;
import com.ageinghippy.model.dto.PreparationTechniqueDTO;
import com.ageinghippy.model.entity.PreparationTechnique;
import com.ageinghippy.repository.PreparationTechniqueRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = GutHealthApplication.class)
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PreparationTechniqueDTOControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PreparationTechniqueRepository preparationTechniqueRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @Order(1)
    void getPreparationTechniques() throws Exception {

        MvcResult result = mockMvc.perform(get("/v1/preparation-techniques"))
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
        MvcResult result = mockMvc.perform(get("/v1/preparation-techniques/PrepType2"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        PreparationTechniqueDTO resultDto = objectMapper.readValue(result.getResponse().getContentAsString(), PreparationTechniqueDTO.class);

        assertEquals(resultDto, new PreparationTechniqueDTO("PrepType2", "Preparation type two description"));
    }

    @Test
    void getPreparationTechnique_notFound() throws Exception {
        mockMvc.perform(get("/v1/preparation-techniques/PrepTypeZ"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void createPreparationTechnique_success() throws Exception {
        MvcResult result =
                mockMvc.perform(post("/v1/preparation-techniques")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"code\":\"newCode\",\"description\":\"newDescription\"}"))
                        .andDo(print())
                        .andExpect(status().isCreated())
                        .andReturn();

        PreparationTechniqueDTO preparationTechniqueDTO = objectMapper.readValue(result.getResponse().getContentAsString(), PreparationTechniqueDTO.class);

        //THEN
        //Correct entity is returned
        assertEquals(new PreparationTechniqueDTO("newCode", "newDescription"), preparationTechniqueDTO);

        //Entity is correctly added to the database
        PreparationTechnique pt = preparationTechniqueRepository.findById("newCode").orElseThrow();
        assertEquals(pt.getCode(), "newCode");
        assertEquals(pt.getDescription(), "newDescription");
    }

    @Test
    void createPreparationTechnique_failure() throws Exception {
        mockMvc.perform(post("/v1/preparation-techniques")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"code\":\"PrepType1\",\"description\":\"description one\"}"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void updatePreparationTechnique_success() throws Exception {
        MvcResult result =
                mockMvc.perform(put("/v1/preparation-techniques/PrepType1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"description\":\"newDescription\"}"))
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andReturn();

        PreparationTechniqueDTO preparationTechniqueDTO = objectMapper.readValue(result.getResponse().getContentAsString(), PreparationTechniqueDTO.class);

        assertEquals(preparationTechniqueDTO.description(), "newDescription");

        //Entity is correctly updated in the database
        PreparationTechnique pt = preparationTechniqueRepository.findById("PrepType1").orElseThrow();
        assertEquals(pt.getCode(), "PrepType1");
        assertEquals(pt.getDescription(), "newDescription");
    }

    @Test
    void updatePreparationTechnique_failure() throws Exception {
        mockMvc.perform(put("/v1/preparation-techniques/InvalidCode")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"description\":\"newDescription\"}"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void deletePreparationTechnique_success() throws Exception {
        //given a specific record exists in the database
        preparationTechniqueRepository.save(
                PreparationTechnique.builder().code("testCode").description("testDesc").build());

        //WHEN the delete endpoint is called
        mockMvc.perform(delete("/v1/preparation-techniques/testCode")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    void deletePreparationTechnique_failure_notfound() throws Exception {
        mockMvc.perform(delete("/v1/preparation-techniques/InvalidCode")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void deletePreparationTechnique_failure_constraintViolation() throws Exception {
        mockMvc.perform(delete("/v1/preparation-techniques/PrepType1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

}