package com.ageinghippy.controller.mvc;

import com.ageinghippy.model.dto.DishComponentDTO;
import com.ageinghippy.model.dto.DishDTOComplex;
import com.ageinghippy.model.dto.DishDTOSimple;
import com.ageinghippy.model.dto.PreparationTechniqueDTO;
import com.ageinghippy.model.entity.UserPrinciple;
import com.ageinghippy.service.DishService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
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
    public String showAllDishes(Model model, Authentication authentication) {
        List<DishDTOSimple> dishes = dishService.getDishes((UserPrinciple) authentication.getPrincipal());

        model.addAttribute("dishes", dishes);

        return "/dish";
    }

    @GetMapping("/view/{id}")
    @PreAuthorize("hasPermission(#id,'Dish','read')")
    public String showDish(Model model, @PathVariable Long id) {
        DishDTOComplex dish = dishService.getDish(id);

        model.addAttribute("dish", dish);

        return "/dish-view";
    }

    @GetMapping("/new")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
    public String showDishNewView(Model model) {
        model.addAttribute("dish",
                new DishDTOSimple(null, null, null,
                        new PreparationTechniqueDTO(null, null)));

        return "/dish-new";
    }

    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
    public String createDish(@ModelAttribute DishDTOComplex dish, Authentication authentication) {
        Long id = dishService.createDish(dish, (UserPrinciple) authentication.getPrincipal()).id();

        return "redirect:/dish/edit/" + id;
    }

    @GetMapping("/edit/{id}")
    @PreAuthorize("hasPermission(#id,'Dish','read')")
    public String showDishEditView(Model model, @PathVariable Long id) {
        DishDTOComplex dish = dishService.getDish(id);

        model.addAttribute("dish", dish);

        return "/dish-edit";
    }

    @PostMapping("/update/{id}")
    @PreAuthorize("hasPermission(#id,'Dish','update')")
    public String updateDish(@ModelAttribute DishDTOSimple dish, @PathVariable Long id) {
        dishService.updateDish(id, dish);

        return "redirect:/dish/edit/" + id;
    }

    @RequestMapping("/delete/{id}")
    @PreAuthorize("hasPermission(#id,'Dish','delete')")
    public String deleteDish(@PathVariable Long id) {
        dishService.deleteDish(id);

        return "redirect:/dish";
    }

}
