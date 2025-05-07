package com.ageinghippy.controller.mvc;

import com.ageinghippy.DataSetupHelper;
import com.ageinghippy.model.dto.DishDTOComplex;
import com.ageinghippy.model.dto.DishDTOSimple;
import com.ageinghippy.model.entity.Dish;
import com.ageinghippy.model.entity.UserPrinciple;
import com.ageinghippy.service.DishService;
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
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;
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

    //todo - Test showDish with redirectAttributes set
}