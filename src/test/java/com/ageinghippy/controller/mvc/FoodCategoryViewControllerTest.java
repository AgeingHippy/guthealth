package com.ageinghippy.controller.mvc;

import com.ageinghippy.DataSetupHelper;
import com.ageinghippy.model.dto.FoodCategoryDTOComplex;
import com.ageinghippy.model.dto.FoodCategoryDTOSimple;
import com.ageinghippy.model.entity.UserPrinciple;
import com.ageinghippy.service.FoodCategoryService;
import com.ageinghippy.service.UserPrincipleService;
import org.junit.jupiter.api.AfterEach;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FoodCategoryViewController.class)
@ContextConfiguration
class FoodCategoryViewControllerTest {

    @MockitoBean
    private FoodCategoryService foodCategoryService;

    @MockitoBean
    private UserPrincipleService userPrincipleService;

    @Autowired
    private MockMvc mockMvc;

    private final DataSetupHelper dsh = new DataSetupHelper();
    private String rootUri = "/food-category";

    @BeforeEach
    void setUp() {
        doAnswer(invocation ->
                dsh.getPrinciple(((User) invocation.getArgument(0)).getUsername())
        )
                .when(userPrincipleService).castToUserPrinciple(any());
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    @WithMockUser(username = "basic", roles = "USER")
    void showFoodCategories() throws Exception {
        List<FoodCategoryDTOSimple> foodCategories = List.of(
                dsh.getFoodCategoryDTOSimple(1L),
                dsh.getFoodCategoryDTOSimple(2L),
                dsh.getFoodCategoryDTOSimple(3L),
                dsh.getFoodCategoryDTOSimple(4L),
                dsh.getFoodCategoryDTOSimple(5L)
        );

        when(foodCategoryService.getFoodCategories(any(UserPrinciple.class))).thenReturn(foodCategories);

        mockMvc.perform(get(rootUri))
                .andDo(print())
                .andExpect(model().attribute("foodCategories", foodCategories))
                .andExpect(view().name("/food-category"));

        verify(foodCategoryService, times(1)).getFoodCategories(any(UserPrinciple.class));
    }

    @Test
    @WithMockUser(username = "basic", roles = "USER")
    void showSpecificFoodCategoryView() throws Exception {
        FoodCategoryDTOComplex foodCategory = dsh.getFoodCategoryDTOComplex(1L);

        when(foodCategoryService.getFoodCategory(1L)).thenReturn(foodCategory);

        mockMvc.perform(get(rootUri + "/view/1"))
                .andDo(print())
                .andExpect(view().name("food-category-view"))
                .andExpect(model().attribute("foodCategory", foodCategory));

        verify(foodCategoryService, times(1)).getFoodCategory(1L);
    }

    @Test
    @WithMockUser(username = "basic", roles = "USER")
    void deleteFoodCategory() throws Exception {
        doNothing().when(foodCategoryService).deleteFoodCategory(1L);

        mockMvc.perform(get(rootUri + "/delete/1"))
                .andDo(print())
                .andExpect(flash().attributeExists("successMessage"))
                .andExpect(redirectedUrl("/food-category"));

        verify(foodCategoryService, times(1)).deleteFoodCategory(1L);
    }

    @Test
    @WithMockUser(username = "basic", roles = "USER")
    void deleteFoodCategory_failure() throws Exception {
        doThrow(new RuntimeException("test")).when(foodCategoryService).deleteFoodCategory(1L);

        mockMvc.perform(get(rootUri + "/delete/1"))
                .andDo(print())
                .andExpect(flash().attributeExists("errorMessage"))
                .andExpect(redirectedUrl("/food-category"));

        verify(foodCategoryService, times(1)).deleteFoodCategory(1L);
    }

    @Test
    @WithMockUser(username = "basic", roles = "USER")
    void showNewFoodCategoryView() throws Exception {
        mockMvc.perform(get(rootUri + "/new"))
                .andDo(print())
                .andExpect(model().attribute(
                        "foodCategory",
                        new FoodCategoryDTOSimple(null, null, null)))
                .andExpect(view().name("/food-category-new"));
    }

    @Test
    @WithMockUser(username = "basic", roles = "USER")
    void showNewFoodCategoryView_redirect_target_with_flash() throws Exception {
        FoodCategoryDTOSimple foodCategoryDTOSimple = dsh.getFoodCategoryDTOSimple(1L);

        mockMvc.perform(get(rootUri + "/new")
                        .flashAttr("errorMessage", "test")
                        .flashAttr("foodCategory", foodCategoryDTOSimple))
                .andDo(print())
                .andExpect(model().attribute("foodCategory", foodCategoryDTOSimple))
                .andExpect(model().attributeExists("errorMessage"))
                .andExpect(view().name("/food-category-new"));
    }

    @Test
    @WithMockUser(username = "basic", roles = "USER")
    void createFoodCategory() throws Exception {
        UserPrinciple principle = dsh.getPrinciple("basic");
        FoodCategoryDTOSimple newFoodCategory =
                new FoodCategoryDTOSimple(null, "new", "new desc");
        FoodCategoryDTOComplex foodCategoryDTOComplex =
                new FoodCategoryDTOComplex(99L, "new", "new desc", List.of());

        when(foodCategoryService.createFoodCategory(newFoodCategory, principle))
                .thenReturn(foodCategoryDTOComplex);

        mockMvc.perform(post(rootUri + "/create")
                        .param("name", newFoodCategory.name())
                        .param("description", newFoodCategory.description())
                        .with(csrf()))
                .andDo(print())
                .andExpect(flash().attributeExists("successMessage"))
                .andExpect(redirectedUrl("/food-category/edit/99"));

        verify(foodCategoryService, times(1))
                .createFoodCategory(newFoodCategory, principle);
    }

    @Test
    @WithMockUser(username = "basic", roles = "USER")
    void createFoodCategory_failure() throws Exception {
        UserPrinciple principle = dsh.getPrinciple("basic");
        FoodCategoryDTOSimple newFoodCategory =
                new FoodCategoryDTOSimple(null, "new", "new desc");

        when(foodCategoryService.createFoodCategory(newFoodCategory, principle))
                .thenThrow(new RuntimeException("test"));

        mockMvc.perform(post(rootUri + "/create")
                        .param("name", newFoodCategory.name())
                        .param("description", newFoodCategory.description())
                        .with(csrf()))
                .andDo(print())
                .andExpect(flash().attributeExists("errorMessage"))
                .andExpect(flash().attribute("foodCategory", newFoodCategory))
                .andExpect(redirectedUrl("/food-category/new"));

        verify(foodCategoryService, times(1))
                .createFoodCategory(newFoodCategory, principle);
    }

    @Test
    @WithMockUser(username = "basic", roles = "USER")
    void showEditFoodCategoryView() throws Exception {
        FoodCategoryDTOComplex foodCategoryDTOComplex = dsh.getFoodCategoryDTOComplex(1L);

        when(foodCategoryService.getFoodCategory(1L)).thenReturn(foodCategoryDTOComplex);

        mockMvc.perform(get(rootUri + "/edit/1"))
                .andDo(print())
                .andExpect(model().attribute("foodCategory", foodCategoryDTOComplex))
                .andExpect(view().name("/food-category-edit"));

        verify(foodCategoryService, times(1)).getFoodCategory(1L);
    }

    @Test
    @WithMockUser(username = "basic", roles = "USER")
    void showEditFoodCategoryView_redirect_target_with_flash() throws Exception {
        FoodCategoryDTOComplex foodCategoryDTOComplex = dsh.getFoodCategoryDTOComplex(1L);

        mockMvc.perform(get(rootUri + "/edit/1")
                        .flashAttr("foodCategory", foodCategoryDTOComplex)
                        .flashAttr("errorMessage", "test"))
                .andDo(print())
                .andExpect(model().attribute("foodCategory", foodCategoryDTOComplex))
                .andExpect(model().attributeExists("errorMessage"))
                .andExpect(view().name("/food-category-edit"));

        verify(foodCategoryService, times(0)).getFoodCategory(1L);
    }

    @Test
    @WithMockUser(username = "basic", roles = "USER")
    void updateFoodCategory() throws Exception {
        FoodCategoryDTOSimple foodCategoryDTOSimple = dsh.getFoodCategoryDTOSimple(1L);
        FoodCategoryDTOComplex foodCategoryDTOComplex = dsh.getFoodCategoryDTOComplex(1L);

        when(foodCategoryService.updateFoodCategory(1L, foodCategoryDTOSimple))
                .thenReturn(foodCategoryDTOComplex);

        mockMvc.perform(post(rootUri + "/update/1")
                        .param("id", String.valueOf(foodCategoryDTOSimple.id()))
                        .param("name", foodCategoryDTOSimple.name())
                        .param("description", foodCategoryDTOSimple.description())
                        .with(csrf()))
                .andDo(print())
                .andExpect(flash().attributeExists("successMessage"))
                .andExpect(redirectedUrl("/food-category/edit/1"));

        verify(foodCategoryService, times(1)).updateFoodCategory(1L, foodCategoryDTOSimple);
    }

    @Test
    @WithMockUser(username = "basic", roles = "USER")
    void updateFoodCategory_failure() throws Exception {
        FoodCategoryDTOSimple foodCategoryDTORequest =
                new FoodCategoryDTOSimple(1L, "new name", "new description");
        FoodCategoryDTOComplex foodCategoryDTOComplex = dsh.getFoodCategoryDTOComplex(1L);
        FoodCategoryDTOComplex foodCategoryFlashModel =
                new FoodCategoryDTOComplex(1L, "new name", "new description", foodCategoryDTOComplex.foodTypes());

        doThrow(new RuntimeException("test")).when(foodCategoryService).updateFoodCategory(1L, foodCategoryDTORequest);
        when(foodCategoryService.getFoodCategory(1L)).thenReturn(foodCategoryDTOComplex);

        mockMvc.perform(post(rootUri + "/update/1")
                        .param("id", String.valueOf(foodCategoryDTORequest.id()))
                        .param("name", foodCategoryDTORequest.name())
                        .param("description", foodCategoryDTORequest.description())
                        .with(csrf()))
                .andDo(print())
                .andExpect(flash().attributeExists("errorMessage"))
                .andExpect(flash().attribute("foodCategory", foodCategoryFlashModel))
                .andExpect(redirectedUrl("/food-category/edit/1"));

        verify(foodCategoryService, times(1)).updateFoodCategory(1L, foodCategoryDTORequest);
        verify(foodCategoryService, times(1)).getFoodCategory(1L);
    }

    @Test
    @WithMockUser(username = "basic", roles = "USER")
    void viewSystemFoodCategories() throws Exception {
        List<FoodCategoryDTOSimple> foodCategories = List.of(
                dsh.getFoodCategoryDTOSimple(6L),
                dsh.getFoodCategoryDTOSimple(7L),
                dsh.getFoodCategoryDTOSimple(8L),
                dsh.getFoodCategoryDTOSimple(9L)
        );

        UserPrinciple systemUserPrinciple = dsh.getPrinciple("system");
        when(userPrincipleService.loadUserByUsername("system")).thenReturn(systemUserPrinciple);
        when(foodCategoryService.getFoodCategories(systemUserPrinciple)).thenReturn(foodCategories);


        mockMvc.perform(get(rootUri + "/system"))
                .andDo(print())
                .andExpect(model().attribute("foodCategories", foodCategories))
                .andExpect(view().name("/food-category-system"));

        verify(userPrincipleService, times(1)).loadUserByUsername("system");
        verify(foodCategoryService, times(1)).getFoodCategories(systemUserPrinciple);
    }

}