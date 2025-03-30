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

@Controller
@RequestMapping("/food-type")
@RequiredArgsConstructor
public class FoodTypeViewController {

    private final FoodTypeService foodTypeService;
    private final FoodCategoryService foodCategoryService;

    @GetMapping("")
    @PreAuthorize("hasPermission(#foodCategoryId,'FoodCategory','read')")
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
        FoodTypeDTOComplex foodType =
                new FoodTypeDTOComplex(
                        null,
                        new FoodCategoryDTOSimple(foodCategoryId, null, null),
                        null,
                        null);

        model.addAttribute("foodCategoryId", foodCategoryId);
        model.addAttribute("foodType", foodType);


        return "/food-type-new";
    }

    @RequestMapping("/delete/{id}")
    @PreAuthorize("hasPermission(#id,'FoodType','delete')")
    public String deleteFoodType(@PathVariable Long id) {
        Long foodCategoryId = foodTypeService.getFoodType(id).foodCategory().id();

        foodTypeService.deleteFoodType(id);

        return "redirect:/food-type?foodCategoryId=" + foodCategoryId;
    }

    @PostMapping("/create")
    @PreAuthorize("hasPermission(#foodType.foodCategory.id,'FoodCategory','edit')")
    public String createFoodType(@ModelAttribute FoodTypeDTOComplex foodType) {
        foodType = foodTypeService.createFoodType(foodType);


        ///todo - need to keep track of and redirect to calling page in some way...
        return "redirect:/food-type?foodCategoryId=" + foodType.foodCategory().id();
    }

    @GetMapping("/edit/{id}")
    @PreAuthorize("hasPermission(#id,'FoodType','edit')")
    public String showFoodTypeEditView(Model model, @PathVariable Long id) {
        FoodTypeDTOComplex foodType = foodTypeService.getFoodType(id);
        model.addAttribute("foodType",foodType);

        return "/food-type-edit";
    }

    @PostMapping("/update/{id}")
    @PreAuthorize("hasPermission(#id,'FoodType','edit')")
    public String updateFoodType(@ModelAttribute FoodTypeDTOComplex foodType, @RequestParam Long id) {
        foodType = foodTypeService.updateFoodType(id, foodType);

        //todo - need to keep track of and redirect to calling page in some way...
        return "redirect:/food-type?foodCategoryId=" + foodType.foodCategory().id();
    }

}
