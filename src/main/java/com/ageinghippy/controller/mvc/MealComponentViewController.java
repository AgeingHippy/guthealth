package com.ageinghippy.controller.mvc;

import com.ageinghippy.model.dto.*;
import com.ageinghippy.service.FoodTypeService;
import com.ageinghippy.service.MealComponentService;
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
    private final FoodTypeService foodTypeService;

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
    public String createMealComponent(@RequestParam Long mealId, @ModelAttribute MealComponentDTO mealComponent) {
        mealComponentService.createNewMealComponent(mealId, mealComponent);

        return "redirect:/meal/edit/" + mealId;
    }

    @GetMapping("/edit/{id}")
    @PreAuthorize("hasPermission(#mealId,'Meal','edit')")
    public String showEditMealComponentView(Model model, @PathVariable Long id, @RequestParam Long mealId) {
        MealComponentDTO mealComponentDTO = mealComponentService.getMealComponent(id);
        FoodTypeDTOComplex foodTypeDTOComplex = foodTypeService.getFoodType(mealComponentDTO.foodType().id());

        model.addAttribute("mealComponent", mealComponentDTO);
        model.addAttribute("meal", mealService.getMeal(mealId));
        model.addAttribute("foodCategoryId",foodTypeDTOComplex.foodCategory().id());

        return "/meal-component-edit";
    }

    @PostMapping("/update/{id}")
    @PreAuthorize("hasPermission(#mealId,'Meal','edit')")
    public String updateMealComponent(Model model, @ModelAttribute MealComponentDTO mealComponent,
                                      @PathVariable Long id, @RequestParam Long mealId) {
        mealComponentService.updateMealComponent(id, mealComponent);

        return "redirect:/meal/edit/" + mealId;
    }

    @RequestMapping("/delete/{id}")
    @PreAuthorize("hasPermission(#mealId,'Meal','delete')")
    public String deleteMealComponent(@PathVariable Long id, @RequestParam Long mealId) {
        mealComponentService.deleteMealComponent(id);

        return "redirect:/meal/edit/" + mealId;
    }

}
