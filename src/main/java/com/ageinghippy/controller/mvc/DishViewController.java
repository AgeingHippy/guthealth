package com.ageinghippy.controller.mvc;

import com.ageinghippy.model.dto.DishComponentDTO;
import com.ageinghippy.model.dto.DishDTOComplex;
import com.ageinghippy.model.dto.DishDTOSimple;
import com.ageinghippy.model.dto.PreparationTechniqueDTO;
import com.ageinghippy.service.DishService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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

        model.addAttribute("dishes", dishes);

        return "/dish";
    }

    @GetMapping("/view/{id}")
    public String showDish(Model model, @PathVariable Long id) {
        DishDTOComplex dish = dishService.getDish(id);

        model.addAttribute("dish", dish);

        return "/dish-view";
    }

    @GetMapping("/new")
    public String showDishNewView(Model model) {
        model.addAttribute("dish",
                new DishDTOSimple(null, null, null,
                        new PreparationTechniqueDTO(null, null)));

        return "/dish-new";
    }

    @PostMapping("/create")
    public String createDish(@ModelAttribute DishDTOComplex dish, Authentication authentication) {
        Long id = dishService.createDish(dish,authentication).id();

        return "redirect:/dish/edit/" + id;
    }

    @GetMapping("/edit/{id}")
    public String showDishEditView(Model model, @PathVariable Long id) {
        DishDTOComplex dish = dishService.getDish(id);

        model.addAttribute("dish", dish);

        return "/dish-edit";
    }

    @PostMapping("/update/{id}")
    public String updateDish(@ModelAttribute DishDTOSimple dish, @PathVariable Long id) {
        dishService.updateDish(id, dish);

        return "redirect:/dish/edit/" + id;
    }

    @RequestMapping("/delete/{id}")
    public String deleteDish(@PathVariable Long id) {
        dishService.deleteDish(id);

        return "redirect:/dish";
    }

}
