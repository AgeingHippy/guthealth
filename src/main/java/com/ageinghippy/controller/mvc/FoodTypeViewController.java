package com.ageinghippy.controller.mvc;

import com.ageinghippy.model.dto.FoodCategoryDTOComplex;
import com.ageinghippy.model.dto.FoodCategoryDTOSimple;
import com.ageinghippy.model.dto.FoodTypeDTOComplex;
import com.ageinghippy.service.FoodCategoryService;
import com.ageinghippy.service.FoodTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/food-type")
@RequiredArgsConstructor
public class FoodTypeViewController {

    private final FoodTypeService foodTypeService;
    private final FoodCategoryService foodCategoryService;

    @GetMapping("")
    @PreAuthorize("hasPermission(#foodCategoryId,'FoodCategory','read')")
    //ToDo - May wish to remove this or redirect to food-category/edit
    public String showFoodTypesView(Model model, @RequestParam Long foodCategoryId) {
        FoodCategoryDTOComplex foodCategory = foodCategoryService.getFoodCategory(foodCategoryId);

        model.addAttribute("foodCategoryId", foodCategoryId);
        model.addAttribute("foodCategoryName", foodCategory.name());
        model.addAttribute("foodTypes", foodCategory.foodTypes());

        return "food-type";
    }

    @GetMapping("/new")
    @PreAuthorize("hasPermission(#foodCategoryId,'FoodCategory','edit')")
    public String showNewFoodTypeView(Model model, @RequestParam Long foodCategoryId) {
        if (!model.containsAttribute("foodType")) {
            FoodTypeDTOComplex foodType =
                    new FoodTypeDTOComplex(
                            null,
                            new FoodCategoryDTOSimple(foodCategoryId, null, null),
                            null,
                            null);
            model.addAttribute("foodType", foodType);
        }

        model.addAttribute("foodCategoryId", foodCategoryId);

        return "/food-type-new";
    }

    @RequestMapping("/delete/{id}")
    @PreAuthorize("hasPermission(#id,'FoodType','delete')")
    public String deleteFoodType(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Long foodCategoryId = foodTypeService.getFoodType(id).foodCategory().id();

        try {
            foodTypeService.deleteFoodType(id);
            redirectAttributes.addFlashAttribute("successMessage", "FoodType deleted successfully.");
        } catch (Exception e) {
            String errorMessage = "FoodType delete failed." + "\n" + e.getClass();
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
        }

        return "redirect:/food-category/edit/" + foodCategoryId;
    }

    @PostMapping("/create")
    @PreAuthorize("hasPermission(#foodType.foodCategory.id,'FoodCategory','edit')")
    public String createFoodType(@ModelAttribute FoodTypeDTOComplex foodType, RedirectAttributes redirectAttributes) {

        try {
            foodType = foodTypeService.createFoodType(foodType);
            redirectAttributes.addFlashAttribute("successMessage", "FoodType created successfully.");
            return "redirect:/food-category/edit/" + foodType.foodCategory().id();
        } catch (Exception e) {
            String errorMessage = "FoodType creation failed." + "\n" + e.getClass();
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
            redirectAttributes.addFlashAttribute("foodType", foodType);
            return "redirect:/food-type/new?foodCategoryId=" + foodType.foodCategory().id();
        }

    }

    @GetMapping("/edit/{id}")
    @PreAuthorize("hasPermission(#id,'FoodType','edit')")
    public String showFoodTypeEditView(Model model, @PathVariable Long id) {

        if (!model.containsAttribute("foodType")) {
            FoodTypeDTOComplex foodType = foodTypeService.getFoodType(id);
            model.addAttribute("foodType", foodType);
        }

        return "/food-type-edit";
    }

    @PostMapping("/update/{id}")
    @PreAuthorize("hasPermission(#id,'FoodType','edit')")
    public String updateFoodType(@ModelAttribute FoodTypeDTOComplex foodType, @RequestParam Long id, RedirectAttributes redirectAttributes) {

        try {
            foodType = foodTypeService.updateFoodType(id, foodType);
            redirectAttributes.addFlashAttribute("successMessage", "FoodType updated successfully.");
            return "redirect:/food-category/edit/" + foodType.foodCategory().id();
        } catch (Exception e) {
            String errorMessage = "FoodType update failed." + "\n" + e.getClass();
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
            redirectAttributes.addFlashAttribute("foodType", foodType);
            return "redirect:/food-type/edit/" + foodType.foodCategory().id();
        }

    }

}
