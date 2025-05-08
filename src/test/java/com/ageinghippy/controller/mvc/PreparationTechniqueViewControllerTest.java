package com.ageinghippy.controller.mvc;

import com.ageinghippy.DataSetupHelper;
import com.ageinghippy.model.dto.PreparationTechniqueDTO;
import com.ageinghippy.service.PreparationTechniqueService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PreparationTechniqueViewController.class)
@ContextConfiguration
class PreparationTechniqueViewControllerTest {

    @MockitoBean
    private PreparationTechniqueService preparationTechniqueService;

    @Autowired
    private MockMvc mockMvc;

    private final DataSetupHelper dsh = new DataSetupHelper();
    private String rootUri = "/preparation-technique";

    @BeforeEach
    void setUp() {
    }

    @Test
    @WithMockUser(username = "basic", roles = "USER")
    void showPreparationTechniques() throws Exception {
        List<PreparationTechniqueDTO> preparationTechniqueDTOList = List.of(
                dsh.getPreparationTechniqueDTO("PrepType1"),
                dsh.getPreparationTechniqueDTO("PrepType2"),
                dsh.getPreparationTechniqueDTO("PrepType3"),
                dsh.getPreparationTechniqueDTO("PrepType4")
        );

        when(preparationTechniqueService.getPreparationTechniques())
                .thenReturn(preparationTechniqueDTOList);

        mockMvc.perform(get(rootUri))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().attribute("preparationTechniques", preparationTechniqueDTOList))
                .andExpect(view().name("/preparation-technique"));
    }

    @Test
    @WithMockUser(username = "basic", roles = "USER")
    void editPreparationTechnique() throws Exception {
        PreparationTechniqueDTO preparationTechniqueDTO = dsh.getPreparationTechniqueDTO("PrepType1");

        when(preparationTechniqueService.getPreparationTechnique("PrepType1"))
                .thenReturn(preparationTechniqueDTO);

        mockMvc.perform(get(rootUri + "/edit/PrepType1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("/preparation-technique-edit"))
                .andExpect(model().attribute("preparationTechnique", preparationTechniqueDTO));
    }

    @Test
    @WithMockUser(username = "basic", roles = "USER")
    void editPreparationTechnique_redirect_target_with_flash() throws Exception {
        PreparationTechniqueDTO preparationTechniqueDTO = dsh.getPreparationTechniqueDTO("PrepType1");

        mockMvc.perform(get(rootUri + "/edit/1")
                        .flashAttr("preparationTechnique", preparationTechniqueDTO))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("/preparation-technique-edit"))
                .andExpect(model().attribute("preparationTechnique", preparationTechniqueDTO));

        verify(preparationTechniqueService, times(0)).getPreparationTechnique(any());
    }

    @Test
    @WithMockUser(username = "basic", roles = "USER")
    void updatePreparationTechnique() throws Exception {

        when(preparationTechniqueService.updatePreparationTechnique(anyString(), any(PreparationTechniqueDTO.class)))
                .thenReturn(new PreparationTechniqueDTO("", ""));

        mockMvc.perform(post(rootUri + "/update/PrepType1")
                        .param("code", "PrepType1")
                        .param("description", "PrepType1 description update")
                        .with(csrf()))
                .andDo(print())
                .andExpect(redirectedUrl("/preparation-technique"))
                .andExpect(flash().attributeExists("successMessage"));

        verify(preparationTechniqueService, times(1))
                .updatePreparationTechnique(anyString(), any(PreparationTechniqueDTO.class));
    }

    @Test
    @WithMockUser(username = "basic", roles = "USER")
    void updatePreparationTechnique_failure() throws Exception {

        when(preparationTechniqueService.updatePreparationTechnique(anyString(), any(PreparationTechniqueDTO.class)))
                .thenThrow(new RuntimeException("test"));

        mockMvc.perform(post(rootUri + "/update/PrepType1")
                        .param("code", "PrepType1")
                        .param("description", "PrepType1 description update")
                        .with(csrf()))
                .andDo(print())
                .andExpect(redirectedUrl("/preparation-technique/edit/PrepType1"))
                .andExpect(flash().attributeExists("errorMessage"))
                .andExpect(flash().attribute("preparationTechnique",
                        new PreparationTechniqueDTO("PrepType1", "PrepType1 description update")));

        verify(preparationTechniqueService, times(1))
                .updatePreparationTechnique(anyString(), any(PreparationTechniqueDTO.class));
    }

    @Test
    @WithMockUser(username = "basic", roles = "USER")
    void showNewPreparationTechniqueForm() throws Exception {
        mockMvc.perform(get(rootUri + "/new"))
                .andDo(print())
                .andExpect(model().attribute("preparationTechnique", new PreparationTechniqueDTO(null, null)))
                .andExpect(view().name("preparation-technique-new"));
    }

    @Test
    @WithMockUser(username = "basic", roles = "USER")
    void showNewPreparationTechniqueForm_redirect_target_with_flash() throws Exception {
        PreparationTechniqueDTO preparationTechniqueDTO = new PreparationTechniqueDTO("code", "desc");
        mockMvc.perform(get(rootUri + "/new")
                        .flashAttr("preparationTechnique", preparationTechniqueDTO))
                .andDo(print())
                .andExpect(model().attribute("preparationTechnique", preparationTechniqueDTO))
                .andExpect(view().name("preparation-technique-new"));
    }

    @Test
    @WithMockUser(username = "basic", roles = "USER")
    void createPreparationTechnique() throws Exception {
        when(preparationTechniqueService.createPreparationTechnique(any()))
                .thenReturn(new PreparationTechniqueDTO("code", "desc"));

        mockMvc.perform(post(rootUri + "/create")
                        .param("code", "code")
                        .param("description", "desc")
                        .with(csrf()))
                .andDo(print())
                .andExpect(redirectedUrl("/preparation-technique"))
                .andExpect(flash().attributeExists("successMessage"));
    }

    @Test
    @WithMockUser(username = "basic", roles = "USER")
    void createPreparationTechnique_failure() throws Exception {
        when(preparationTechniqueService.createPreparationTechnique(any()))
                .thenThrow(new RuntimeException("test"));

        mockMvc.perform(post(rootUri + "/create")
                        .param("code", "code")
                        .param("description", "desc")
                        .with(csrf()))
                .andDo(print())
                .andExpect(redirectedUrl("/preparation-technique/new"))
                .andExpect(flash().attributeExists("errorMessage"))
                .andExpect(flash().attribute("preparationTechnique",
                        new PreparationTechniqueDTO("code", "desc")));
    }

    @Test
    @WithMockUser(username = "basic", roles = "USER")
    void deletePreparationTechnique() throws Exception {
        doNothing().when(preparationTechniqueService).deletePreparationTechnique("PrepType1");
        mockMvc.perform(get(rootUri +"/delete/PrepType1"))
                .andDo(print())
                .andExpect(redirectedUrl("/preparation-technique"))
                .andExpect(flash().attributeExists("successMessage"));

        verify(preparationTechniqueService, times(1)).deletePreparationTechnique("PrepType1");
    }

    @Test
    @WithMockUser(username = "basic", roles = "USER")
    void deletePreparationTechnique_failure() throws Exception {
        doThrow(new RuntimeException("test")).when(preparationTechniqueService).deletePreparationTechnique("PrepType1");
        mockMvc.perform(get(rootUri +"/delete/PrepType1"))
                .andDo(print())
                .andExpect(redirectedUrl("/preparation-technique"))
                .andExpect(flash().attributeExists("errorMessage"));

        verify(preparationTechniqueService, times(1)).deletePreparationTechnique("PrepType1");
    }
}