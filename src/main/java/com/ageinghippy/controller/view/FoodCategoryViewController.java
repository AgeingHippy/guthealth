package com.ageinghippy.controller.view;

import com.ageinghippy.model.dto.FoodCategoryDTOComplex;
import com.ageinghippy.model.dto.FoodCategoryDTOSimple;
import com.ageinghippy.model.dto.FoodTypeDTOComplex;
import com.ageinghippy.service.FoodCategoryService;
import com.ageinghippy.service.FoodTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/food-category")
@RequiredArgsConstructor
public class FoodCategoryViewController {

    private final FoodCategoryService foodCategoryService;
    private final FoodTypeService foodTypeService;

    @GetMapping("")
    public String showFoodCategories(Model model) {
        List<FoodCategoryDTOSimple> foodCategories = foodCategoryService.getFoodCategories();

        model.addAttribute("foodCategories", foodCategories);

        return "/food-category";
    }

    @GetMapping("/view/{id}")
    public String showSpecificFoodCategoryView(Model model, @PathVariable Long id) {
        FoodCategoryDTOComplex foodCategory = foodCategoryService.getFoodCategory(id);

        model.addAttribute("foodCategory", foodCategory);

        return "food-category-view";
    }


    @RequestMapping("/delete/{id}")
    public String deleteFoodCategory(@PathVariable Long id) {
        foodCategoryService.deleteFoodCategory(id);

        return "redirect:/food-category";
    }

    @GetMapping("/new")
    public String showNewFoodCategoryView(Model model) {
        model.addAttribute("foodCategory", new FoodCategoryDTOSimple(null, null, null));

        return "/food-category-new";
    }

    @PostMapping("/create")
    public String createFoodCategory(@ModelAttribute FoodCategoryDTOSimple foodCategory, @RequestParam Optional<Boolean> addFoodTypes) {
        Long id = foodCategoryService.createFoodCategory(foodCategory).id();

        //todo - redirect to edit to allow addition of new food types?
        if (addFoodTypes.orElseGet(() -> false)) {
            return "redirect:/food-type?foodCategoryId="+id;
        } else {
            return "redirect:/food-category";
        }
    }

    @GetMapping("/edit/{id}")
    public String showEditFoodCategoryView(Model model, @PathVariable Long id) {
        FoodCategoryDTOComplex foodCategoryDTOComplex = foodCategoryService.getFoodCategory(id);
        //todo - do I want to use dtoComplex for new and edited food categories?
        model.addAttribute("foodCategory",
                new FoodCategoryDTOSimple(
                        foodCategoryDTOComplex.id(),
                        foodCategoryDTOComplex.name(),
                        foodCategoryDTOComplex.description()));

        return "/food-category-edit";
    }

    @PostMapping("/update/{id}")
    public String updateFoodCategory(@PathVariable Long id, @ModelAttribute FoodCategoryDTOSimple foodCategory,
                                     @RequestParam(required = false) Optional<Boolean> addFoodTypes) {
        foodCategoryService.updateFoodCategory(id, foodCategory);

        //todo - redirect to edit to allow addition of new food types?
        if (addFoodTypes.orElseGet(() -> false)) {
            return "redirect:/food-type?foodCategoryId="+id;
        } else {
            return "redirect:/food-category";
        }
    }


}
