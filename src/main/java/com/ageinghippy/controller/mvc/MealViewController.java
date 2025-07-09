package com.ageinghippy.controller.mvc;

import com.ageinghippy.model.dto.*;
import com.ageinghippy.model.entity.UserPrinciple;
import com.ageinghippy.service.MealService;
import com.ageinghippy.service.UserPrincipleService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/meal")
@RequiredArgsConstructor
public class MealViewController {

    private final MealService mealService;
    private final UserPrincipleService userPrincipleService;

    @GetMapping("")
    public String getAllMeals(Model model, Authentication authentication) {
        UserPrinciple principle = userPrincipleService.castToUserPrinciple(authentication.getPrincipal());
        List<MealDTOSimple> meals = mealService.getMeals(principle);
        model.addAttribute("meals",meals);

        return "meal";
    }

    @GetMapping("/view/{id}")
    @PreAuthorize("hasPermission(#id,'Meal','read')")
    public String getMeal(Model model, @PathVariable Long id) {
        MealDTOComplex meal = mealService.getMeal(id);
        model.addAttribute("meal",meal);

        return "meal-view";
    }

    @GetMapping("/new")
    @PreAuthorize("hasRole('ROLE_USER')")
    public String showMealNewView(Model model) throws JsonProcessingException {
        if (!model.containsAttribute("meal")) {
            model.addAttribute("meal",
                    new MealDTOSimple(null, null, null,null));
        }

        return "/meal-new";
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('ROLE_USER')")
    public String createMeal(@ModelAttribute MealDTOSimple meal,
                             Authentication authentication,
                             RedirectAttributes redirectAttributes) {
        try {
            Long id = mealService.createMeal(meal, userPrincipleService.castToUserPrinciple(authentication.getPrincipal())).id();
            redirectAttributes.addFlashAttribute("successMessage", "Meal created successfully.");
            return "redirect:/meal/edit/" + id;
        } catch (Exception e) {
            String errorMessage = e.getClass().getSimpleName() + " - " + "Meal create failed.";
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
            redirectAttributes.addFlashAttribute("meal", meal);
            return "redirect:/meal/new";
        }
    }

    @GetMapping("/edit/{id}")
    @PreAuthorize("hasPermission(#id,'Meal','update')")
    public String showMealEditView(Model model, @PathVariable Long id) {
        if (!model.containsAttribute("meal")) {
            MealDTOComplex meal = mealService.getMeal(id);
            model.addAttribute("meal", meal);
        }

        return "/meal-edit";
    }

}
