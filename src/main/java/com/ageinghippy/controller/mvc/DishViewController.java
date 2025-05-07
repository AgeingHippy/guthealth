package com.ageinghippy.controller.mvc;

import com.ageinghippy.model.dto.DishDTOComplex;
import com.ageinghippy.model.dto.DishDTOSimple;
import com.ageinghippy.model.dto.PreparationTechniqueDTO;
import com.ageinghippy.model.entity.UserPrinciple;
import com.ageinghippy.service.DishService;
import com.ageinghippy.service.UserPrincipleService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/dish")
@RequiredArgsConstructor
@Slf4j
public class DishViewController {
    private final DishService dishService;
    private final UserPrincipleService userPrincipleService;

    @GetMapping
    public String showAllDishes(Model model, Authentication authentication) {
        List<DishDTOSimple> dishes =
                dishService.getDishes(userPrincipleService.castToUserPrinciple(authentication.getPrincipal()));

        model.addAttribute("dishes", dishes);

        return "/dish";
    }

    @GetMapping("/view/{id}")
    @PreAuthorize("hasPermission(#id,'Dish','read')")
    public String showDish(Model model, @PathVariable Long id) {
        if (!model.containsAttribute("dish")) {
            DishDTOComplex dish = dishService.getDish(id);
            model.addAttribute("dish", dish);
        }

        return "/dish-view";
    }

    @GetMapping("/new")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
    public String showDishNewView(Model model) throws JsonProcessingException {
        if (!model.containsAttribute("dish")) {
            model.addAttribute("dish",
                    new DishDTOSimple(null, null, null,
                            new PreparationTechniqueDTO(null, null)));
        }

        return "/dish-new";
    }

    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
    public String createDish(@ModelAttribute DishDTOSimple dish,
                             Authentication authentication,
                             RedirectAttributes redirectAttributes) {
        try {
            Long id = dishService.createDish(dish, userPrincipleService.castToUserPrinciple(authentication.getPrincipal())).id();
            redirectAttributes.addFlashAttribute("successMessage", "Dish created successfully.");
            return "redirect:/dish/edit/" + id;
        } catch (Exception e) {
            String errorMessage = e.getClass().getSimpleName() + " - " + "Dish create failed.";
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
            redirectAttributes.addFlashAttribute("dish", dish);
            return "redirect:/dish/new";
        }

    }

    @GetMapping("/edit/{id}")
    @PreAuthorize("hasPermission(#id,'Dish','read')")
    public String showDishEditView(Model model, @PathVariable Long id) {
        if (!model.containsAttribute("dish")) {
            DishDTOComplex dish = dishService.getDish(id);
            model.addAttribute("dish", dish);
        }

        return "/dish-edit";
    }

    @PostMapping("/update/{id}")
    @PreAuthorize("hasPermission(#id,'Dish','update')")
    public String updateDish(@ModelAttribute DishDTOSimple dish,
                             @PathVariable Long id,
                             RedirectAttributes redirectAttributes) {
        try {
            dishService.updateDish(id, dish);
            redirectAttributes.addFlashAttribute("successMessage", "Dish updated successfully");
        } catch (Exception e) {
            String errorMessage = e.getClass().getSimpleName() + " - " + "Dish update failed.";
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
            redirectAttributes.addFlashAttribute("dish",
                    new DishDTOComplex(dish.id(), dish.name(), dish.description(), dish.preparationTechnique(),
                            dishService.getDish(dish.id()).dishComponents()));
        }

        return "redirect:/dish/edit/" + id;
    }

    @RequestMapping("/delete/{id}")
    @PreAuthorize("hasPermission(#id,'Dish','delete')")
    public String deleteDish(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            dishService.deleteDish(id);
            redirectAttributes.addFlashAttribute("successMessage", "Dish deleted successfully");
        } catch (Exception e) {
            String errorMessage = e.getClass().getSimpleName() + " - " + "Dish delete failed.";
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
        }

        return "redirect:/dish";
    }

}
