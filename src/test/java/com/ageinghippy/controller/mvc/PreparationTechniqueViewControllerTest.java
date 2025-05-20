package com.ageinghippy.controller.mvc;

import com.ageinghippy.DataSetupHelper;
import com.ageinghippy.model.dto.PreparationTechniqueDTO;
import com.ageinghippy.model.entity.UserPrinciple;
import com.ageinghippy.service.PreparationTechniqueService;
import com.ageinghippy.service.UserPrincipleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.core.userdetails.User;
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

    @MockitoBean
    private UserPrincipleService userPrincipleService;

    @Autowired
    private MockMvc mockMvc;

    private final DataSetupHelper dsh = new DataSetupHelper();
    private String rootUri = "/preparation-technique";

    @BeforeEach
    void setUp() {
        doAnswer(invocation -> dsh.getPrinciple(((User) invocation.getArgument(0)).getUsername()))
                .when(userPrincipleService).castToUserPrinciple(any());
    }

    @Test
    @WithMockUser(username = "basic", roles = "USER")
    void showPreparationTechniques() throws Exception {
        List<PreparationTechniqueDTO> preparationTechniqueDTOList = List.of(
                dsh.getPreparationTechniqueDTO(1L),
                dsh.getPreparationTechniqueDTO(2L),
                dsh.getPreparationTechniqueDTO(3L),
                dsh.getPreparationTechniqueDTO(4L)
        );

        when(preparationTechniqueService.getPreparationTechniques(any(UserPrinciple.class)))
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
        PreparationTechniqueDTO preparationTechniqueDTO = dsh.getPreparationTechniqueDTO(1L);

        when(preparationTechniqueService.getPreparationTechnique(1L))
                .thenReturn(preparationTechniqueDTO);

        mockMvc.perform(get(rootUri + "/edit/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("/preparation-technique-edit"))
                .andExpect(model().attribute("preparationTechnique", preparationTechniqueDTO));
    }

    @Test
    @WithMockUser(username = "basic", roles = "USER")
    void editPreparationTechnique_redirect_target_with_flash() throws Exception {
        PreparationTechniqueDTO preparationTechniqueDTO = dsh.getPreparationTechniqueDTO(1L);

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

        when(preparationTechniqueService.updatePreparationTechnique(anyLong(), any(PreparationTechniqueDTO.class)))
                .thenReturn(new PreparationTechniqueDTO(null, null, null));

        mockMvc.perform(post(rootUri + "/update/1")
                        .param("id", "1")
                        .param("code", "PrepType1")
                        .param("description", "PrepType1 description update")
                        .with(csrf()))
                .andDo(print())
                .andExpect(redirectedUrl("/preparation-technique"))
                .andExpect(flash().attributeExists("successMessage"));

        verify(preparationTechniqueService, times(1))
                .updatePreparationTechnique(anyLong(), any(PreparationTechniqueDTO.class));
    }

    @Test
    @WithMockUser(username = "basic", roles = "USER")
    void updatePreparationTechnique_failure() throws Exception {

        when(preparationTechniqueService.updatePreparationTechnique(anyLong(), any(PreparationTechniqueDTO.class)))
                .thenThrow(new RuntimeException("test"));

        mockMvc.perform(post(rootUri + "/update/1")
                        .param("id", "1")
                        .param("code", "PrepType1")
                        .param("description", "PrepType1 description update")
                        .with(csrf()))
                .andDo(print())
                .andExpect(redirectedUrl("/preparation-technique/edit/1"))
                .andExpect(flash().attributeExists("errorMessage"))
                .andExpect(flash().attribute("preparationTechnique",
                        new PreparationTechniqueDTO(1L, "PrepType1", "PrepType1 description update")));

        verify(preparationTechniqueService, times(1))
                .updatePreparationTechnique(anyLong(), any(PreparationTechniqueDTO.class));
    }

    @Test
    @WithMockUser(username = "basic", roles = "USER")
    void showNewPreparationTechniqueForm() throws Exception {
        mockMvc.perform(get(rootUri + "/new"))
                .andDo(print())
                .andExpect(model().attribute("preparationTechnique", new PreparationTechniqueDTO(null, null, null)))
                .andExpect(view().name("preparation-technique-new"));
    }

    @Test
    @WithMockUser(username = "basic", roles = "USER")
    void showNewPreparationTechniqueForm_redirect_target_with_flash() throws Exception {
        PreparationTechniqueDTO preparationTechniqueDTO = new PreparationTechniqueDTO(null, "code", "desc");
        mockMvc.perform(get(rootUri + "/new")
                        .flashAttr("preparationTechnique", preparationTechniqueDTO))
                .andDo(print())
                .andExpect(model().attribute("preparationTechnique", preparationTechniqueDTO))
                .andExpect(view().name("preparation-technique-new"));
    }

    @Test
    @WithMockUser(username = "basic", roles = "USER")
    void createPreparationTechnique() throws Exception {
        when(preparationTechniqueService.createPreparationTechnique(any(), any()))
                .thenReturn(new PreparationTechniqueDTO(1L, "code", "desc"));

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
        when(preparationTechniqueService.createPreparationTechnique(any(), any()))
                .thenThrow(new RuntimeException("test"));

        mockMvc.perform(post(rootUri + "/create")
                        .param("code", "code")
                        .param("description", "desc")
                        .with(csrf()))
                .andDo(print())
                .andExpect(redirectedUrl("/preparation-technique/new"))
                .andExpect(flash().attributeExists("errorMessage"))
                .andExpect(flash().attribute("preparationTechnique",
                        new PreparationTechniqueDTO(null, "code", "desc")));
    }

    @Test
    @WithMockUser(username = "basic", roles = "USER")
    void deletePreparationTechnique() throws Exception {
        doNothing().when(preparationTechniqueService).deletePreparationTechnique(1L);

        mockMvc.perform(get(rootUri + "/delete/1"))
                .andDo(print())
                .andExpect(redirectedUrl("/preparation-technique"))
                .andExpect(flash().attributeExists("successMessage"));

        verify(preparationTechniqueService, times(1)).deletePreparationTechnique(1L);
    }

    @Test
    @WithMockUser(username = "basic", roles = "USER")
    void deletePreparationTechnique_failure() throws Exception {
        doThrow(new RuntimeException("test")).when(preparationTechniqueService).deletePreparationTechnique(1L);
        mockMvc.perform(get(rootUri + "/delete/1"))
                .andDo(print())
                .andExpect(redirectedUrl("/preparation-technique"))
                .andExpect(flash().attributeExists("errorMessage"));

        verify(preparationTechniqueService, times(1)).deletePreparationTechnique(1L);
    }

    @Test
    @WithMockUser(username = "basic", roles = {"USER", "GUEST"})
    void showSystemPreparationTechniques_success() throws Exception {
        UserPrinciple systemPrinciple = dsh.getPrinciple("system");
        List<PreparationTechniqueDTO> preparationTechniqueDTOList = List.of(
                dsh.getPreparationTechniqueDTO(5L),
                dsh.getPreparationTechniqueDTO(6L));

        when(userPrincipleService.loadUserByUsername("system")).thenReturn(systemPrinciple);
        when(preparationTechniqueService.getPreparationTechniques(systemPrinciple))
                .thenReturn(preparationTechniqueDTOList);

        mockMvc.perform(get(rootUri + "/system"))
                .andDo(print())
                .andExpect(model().attribute("preparationTechniques", preparationTechniqueDTOList))
                .andExpect(view().name("/preparation-technique-system"));

        verify(userPrincipleService, times(1)).loadUserByUsername("system");
        verify(preparationTechniqueService, times(1)).getPreparationTechniques(systemPrinciple);
    }
}