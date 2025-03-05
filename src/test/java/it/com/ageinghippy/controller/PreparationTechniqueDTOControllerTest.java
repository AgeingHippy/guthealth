package it.com.ageinghippy.controller;

import com.ageinghippy.GutHealthApplication;
import com.ageinghippy.model.dto.PreparationTechniqueDTO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = GutHealthApplication.class)
@AutoConfigureMockMvc
class PreparationTechniqueDTOControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    void getPreparationTechniques() throws Exception {

        MvcResult result = mockMvc.perform(get("/v1/preparation-techniques"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        List<PreparationTechniqueDTO> resultList = (new ObjectMapper()).readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});

        assertEquals(resultList.size(),4);
        assert(resultList.contains(new PreparationTechniqueDTO("PrepType1","Preparation type one description")));
        assert(resultList.contains(new PreparationTechniqueDTO("PrepType2","Preparation type two description")));
        assert(resultList.contains(new PreparationTechniqueDTO("PrepType3","Preparation type three description")));
        assert(resultList.contains(new PreparationTechniqueDTO("PrepType4","Preparation type four description")));
    }

    @Test
    void getPreparationTechnique() throws Exception {
        MvcResult result = mockMvc.perform(get("/v1/preparation-techniques/PrepType2"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        PreparationTechniqueDTO resultDto = (new ObjectMapper()).readValue(result.getResponse().getContentAsString(), PreparationTechniqueDTO.class);

        assertEquals(resultDto,new PreparationTechniqueDTO("PrepType2","Preparation type two description"));
    }

    @Test
    void createPreparationTechnique() {
    }
}