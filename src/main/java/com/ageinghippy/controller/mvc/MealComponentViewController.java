package com.ageinghippy.controller.mvc;

import com.ageinghippy.model.dto.DishComponentDTO;
import com.ageinghippy.model.dto.FoodTypeDTOSimple;
import com.ageinghippy.model.dto.MealComponentDTO;
import com.ageinghippy.model.dto.PreparationTechniqueDTO;
import com.ageinghippy.service.MealService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/meal-component")
@RequiredArgsConstructor
public class MealComponentViewController {

    private final MealService mealService;
    private final MealComponentService mealComponentService;

    @GetMapping("/new")
    @PreAuthorize("hasPermission(#mealId,'Meal','edit')")
    public String showNewMealComponentView(Model model, @RequestParam Long mealId) {
        model.addAttribute("mealComponent",
                new MealComponentDTO(null,
                        new FoodTypeDTOSimple(null, null, null),
                        new PreparationTechniqueDTO(null, null, null),
                        null));

        model.addAttribute("meal", mealService.getMeal(mealId));

        return "/meal-component-new";
    }

    @PostMapping("/create")
    @PreAuthorize("hasPermission(#mealId,'Meal','edit')")
    public String createDishComponent(@RequestParam Long mealId, @ModelAttribute MealComponentDTO mealComponent) {
        mealComponentService.createMealComponent(mealId, mealComponent);

        return "redirect:/dish/edit/" + mealId;
    }

}
