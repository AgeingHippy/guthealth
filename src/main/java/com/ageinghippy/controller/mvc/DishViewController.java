package com.ageinghippy.controller.mvc;

import com.ageinghippy.model.dto.DishDTOComplex;
import com.ageinghippy.model.dto.DishDTOSimple;
import com.ageinghippy.service.DishService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/dish")
@RequiredArgsConstructor
@Slf4j
public class DishViewController {
    private final DishService dishService;

    @GetMapping
    public String showAllDishes(Model model) {
        List<DishDTOSimple> dishes = dishService.getDishes();

        model.addAttribute("dishes",dishes);

        return "/dish";
    }

    @GetMapping("/view/{id}")
    public String showDish(Model model, @PathVariable Long id) {
        DishDTOComplex dish = dishService.getDish(id);

        model.addAttribute("dish",dish);

        return "/dish-view";
    }

    @GetMapping("/edit/{id}")
    public String showDishEditView(Model model, @PathVariable Long id) {
        DishDTOComplex dish = dishService.getDish(id);

        model.addAttribute("dish",dish);

        return "/dish-edit";
    }


}
