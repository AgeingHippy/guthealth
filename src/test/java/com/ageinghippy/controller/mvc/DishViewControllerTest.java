package com.ageinghippy.controller.mvc;

import com.ageinghippy.DataSetupHelper;
import com.ageinghippy.model.dto.DishDTOComplex;
import com.ageinghippy.model.dto.DishDTOSimple;
import com.ageinghippy.model.dto.PreparationTechniqueDTO;
import com.ageinghippy.model.entity.UserPrinciple;
import com.ageinghippy.service.DishService;
import com.ageinghippy.service.UserPrincipleService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
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

@WebMvcTest(DishViewController.class)
@ContextConfiguration
class DishViewControllerTest {
    @MockitoBean
    private DishService dishService;
    @MockitoBean
    private UserPrincipleService userPrincipleService;

    @Autowired
    private MockMvc mockMvc;

    private final DataSetupHelper dsh = new DataSetupHelper();
    private String rootUri = "/dish";

    @BeforeEach
    void setUp() {
        doAnswer(invocation -> dsh.getPrinciple(((User) invocation.getArgument(0)).getUsername()))
                .when(userPrincipleService).castToUserPrinciple(any());
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    @WithMockUser(username = "basic", roles = "USER")
    public void showAllDishes_success() throws Exception {
        UserPrinciple userPrinciple = dsh.getPrinciple("basic");
        List<DishDTOSimple> dishes = List.of(
                dsh.getDishDTOSimple(1L),
                dsh.getDishDTOSimple(2L),
                dsh.getDishDTOSimple(3L),
                dsh.getDishDTOSimple(4L)
        );

        when(dishService.getDishes(userPrinciple)).thenReturn(dishes);

        mockMvc.perform(get(rootUri))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("/dish"))
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(model().attribute("dishes", dishes));
    }

    @Test
    @WithMockUser(username = "basic", roles = "USER")
    public void showDish_success() throws Exception {
        DishDTOComplex dishDTOComplex = dsh.getDishDTOComplex(1L);

        when(dishService.getDish(1L)).thenReturn(dishDTOComplex);

        mockMvc.perform(get(rootUri + "/view/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("/dish-view"))
                .andExpect(model().attributeExists("dish"))
                .andExpect(model().attribute("dish", dishDTOComplex));
    }

    @Test
    @WithMockUser(username = "basic", roles = "USER")
    public void showDish_redirect_target_with_flash() throws Exception {
        DishDTOComplex dishDTOComplex =
                new DishDTOComplex(1L, "name", "description",
                        new PreparationTechniqueDTO("code", "descW"),
                        null);

        mockMvc.perform(get(rootUri + "/view/1")
                        .flashAttr("dish", dishDTOComplex))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("/dish-view"))
                .andExpect(model().attributeExists("dish"))
                .andExpect(model().attribute("dish", dishDTOComplex));
    }


    @Test
    @WithMockUser(username = "basic", roles = "USER")
    public void showDishNewView() throws Exception {
        DishDTOSimple expectedDish =
                new DishDTOSimple(null, null, null,
                        new PreparationTechniqueDTO(null, null));
        mockMvc.perform(get(rootUri + "/new"))
                .andDo(print())
                .andExpect(view().name("/dish-new"))
                .andExpect(model().attribute("dish", expectedDish));
    }

    @Test
    @WithMockUser(username = "basic", roles = "USER")
    public void showDishNewView_redirect_target_with_flash() throws Exception {
        DishDTOSimple expectedDish =
                new DishDTOSimple(1L, "test", "Test",
                        new PreparationTechniqueDTO("PT", "desc"));
        mockMvc.perform(get(rootUri + "/new")
                        .flashAttr("dish", expectedDish))
                .andDo(print())
                .andExpect(view().name("/dish-new"))
                .andExpect(model().attribute("dish", expectedDish));
    }

    @Test
    @WithMockUser(username = "basic", roles = "USER")
    public void createDish_success() throws Exception {
        DishDTOComplex savedDishDish = new DishDTOComplex(99L, "name", "description",
                new PreparationTechniqueDTO("code", "descr"), List.of());

        when(dishService.createDish(any(DishDTOSimple.class), any(UserPrinciple.class))).thenReturn(savedDishDish);

        mockMvc.perform(post(rootUri + "/create")
                        .param("name", "name")
                        .param("description", "description")
                        .param("preparationTechnique.code", "PTCode")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .with(csrf()))
                .andDo(print())
                .andExpect(redirectedUrl(rootUri + "/edit/99"))
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attributeExists("successMessage"));
    }

    @Test
    @WithMockUser(username = "basic", roles = "USER")
    public void createDish_failure() throws Exception {
        DishDTOSimple newDish = new DishDTOSimple(null, "name", "description",
                new PreparationTechniqueDTO("PTCode", null));

        when(dishService.createDish(any(DishDTOSimple.class), any(UserPrinciple.class))).thenThrow(new RuntimeException("test"));

        mockMvc.perform(post(rootUri + "/create")
                        .param("name", "name")
                        .param("description", "description")
                        .param("preparationTechnique.code", "PTCode")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .with(csrf()))
                .andDo(print())
                .andExpect(redirectedUrl(rootUri + "/new"))
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attributeExists("errorMessage"))
                .andExpect(flash().attribute("dish", newDish));
    }

    @Test
    @WithMockUser(username = "basic", roles = "USER")
    public void showDishEditView() throws Exception {
        DishDTOComplex dishDTOComplex = dsh.getDishDTOComplex(1L);

        when(dishService.getDish(1L)).thenReturn(dishDTOComplex);

        mockMvc.perform(get(rootUri + "/edit/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().attribute("dish", dishDTOComplex))
                .andExpect(view().name("/dish-edit"));
    }

    @Test
    @WithMockUser(username = "basic", roles = "USER")
    public void showDishEditView_redirect_target_with_flash() throws Exception {
        DishDTOComplex dishDTOComplex =
                new DishDTOComplex(1L, "name", "description",
                        new PreparationTechniqueDTO("code", "descW"),
                        null);

        mockMvc.perform(get(rootUri + "/edit/1")
                        .flashAttr("dish", dishDTOComplex))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().attribute("dish", dishDTOComplex))
                .andExpect(view().name("/dish-edit"));

        verify(dishService, times(0)).getDish(any());
    }

    @Test
    @WithMockUser(username = "basic", roles = "USER")
    public void updateDish() throws Exception {
        DishDTOSimple dishDTOSimple = new DishDTOSimple(1L, "name", "description",
                new PreparationTechniqueDTO("PTCode", "null"));

        when(dishService.updateDish(any(), any()))
                .thenReturn(new DishDTOComplex(null, null, null, null, null));

        mockMvc.perform(post(rootUri + "/update/1")
                        .param("id", "1")
                        .param("name", "name")
                        .param("description", "description")
                        .param("preparationTechnique.code", "PTCode")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/dish/edit/1"))
                .andExpect(flash().attributeExists("successMessage"));

        verify(dishService, times(1)).updateDish(any(), any());
    }

    @Test
    @WithMockUser(username = "basic", roles = "USER")
    public void updateDish_failure() throws Exception {
        DishDTOComplex dishDTOComplex = new DishDTOComplex(1L, "name", "description",
                new PreparationTechniqueDTO("PTCode", "null"),List.of());

        when(dishService.updateDish(any(), any())).thenThrow(new RuntimeException("test"));
        when(dishService.getDish(1L))
                .thenReturn(new DishDTOComplex(null, null, null, null, List.of()));

        mockMvc.perform(post(rootUri + "/update/1")
                        .param("id", "1")
                        .param("name", "name")
                        .param("description", "description")
                        .param("preparationTechnique.code", "PTCode")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/dish/edit/1"))
                .andExpect(flash().attributeExists("errorMessage"))
                .andExpect(flash().attributeExists("dish")); //todo validate contents of dish

        verify(dishService, times(1)).updateDish(any(), any());
    }

    @Test
    @WithMockUser(username = "basic", roles = "USER")
    public void deleteDish() throws Exception {
        doNothing().when(dishService).deleteDish(1L);

        mockMvc.perform(get(rootUri+"/delete/1"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/dish"))
                .andExpect(flash().attributeExists("successMessage"));
    }

    @Test
    @WithMockUser(username = "basic", roles = "USER")
    public void deleteDish_failure() throws Exception {
        doThrow(new RuntimeException("test")).when(dishService).deleteDish(1L);

        mockMvc.perform(get(rootUri+"/delete/1"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/dish"))
                .andExpect(flash().attributeExists("errorMessage"));
    }

}