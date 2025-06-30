package com.ageinghippy.controller.mvc;

import com.ageinghippy.DataSetupHelper;
import com.ageinghippy.model.dto.MealDTOComplex;
import com.ageinghippy.model.dto.MealDTOSimple;
import com.ageinghippy.model.entity.UserPrinciple;
import com.ageinghippy.service.MealService;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

@WebMvcTest(MealViewController.class)
@ContextConfiguration
public class MealViewControllerTest {

    @MockitoBean
    UserPrincipleService userPrincipleService;

    @MockitoBean
    MealService mealService;

    @Autowired
    MockMvc mockMvc;

    private DataSetupHelper dsh = new DataSetupHelper();
    private String rootUri = "/meal";

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
    void showMealsView() throws Exception {
        UserPrinciple principle = dsh.getPrinciple("basic");
        List<MealDTOSimple> meals = List.of (
                dsh.getMealDTOSimple(1L),
                dsh.getMealDTOSimple(2L),
                dsh.getMealDTOSimple(3L),
                dsh.getMealDTOSimple(4L)
        );

        when(mealService.getMeals(principle)).thenReturn(meals);

        mockMvc.perform(get(rootUri))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("meal"))
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(model().attribute("meals", meals));

        verify(mealService, times(1)).getMeals(principle);
    }

    @Test
    @WithMockUser(username = "basic", roles = "USER")
    void showMealView() throws Exception {
        MealDTOComplex mealDTOComplex = dsh.getMealDTOComplex(1L);

        when(mealService.getMeal(1L)).thenReturn(mealDTOComplex);

        mockMvc.perform(get(rootUri + "/view/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("meal-view"))
                .andExpect(model().attributeExists("meal"))
                .andExpect(model().attribute("meal", mealDTOComplex));

        verify(mealService, times(1)).getMeal(1L);
    }

}
