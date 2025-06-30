package com.ageinghippy.controller.mvc;

import com.ageinghippy.model.dto.MealDTOComplex;
import com.ageinghippy.model.dto.MealDTOSimple;
import com.ageinghippy.model.entity.UserPrinciple;
import com.ageinghippy.service.MealService;
import com.ageinghippy.service.UserPrincipleService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

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
}
