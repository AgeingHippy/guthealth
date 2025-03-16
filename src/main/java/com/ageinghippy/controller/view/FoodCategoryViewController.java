package com.ageinghippy.controller.view;

import com.ageinghippy.model.dto.FoodCategoryDTOSimple;
import com.ageinghippy.service.FoodCategoryService;
import com.ageinghippy.service.FoodTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/food-category")
@RequiredArgsConstructor
public class FoodCategoryViewController {

    private final FoodCategoryService foodCategoryService;

    @GetMapping("")
    public String showFoodCategories(Model model) {
        List<FoodCategoryDTOSimple> foodCategories = foodCategoryService.getFoodCategories();

        model.addAttribute("foodCategories",foodCategories);

        return "/food-category";
    }

    @RequestMapping("/delete/{id}")
    public String deleteFoodCategory(@PathVariable Long id) {
        foodCategoryService.deleteFoodCategory(id);

        return "redirect:/food-category";
    }

    @GetMapping("/new")
    public String showNewFoodCategoryForm(Model model) {
        model.addAttribute("foodCategory",new FoodCategoryDTOSimple(null,null,null));

        return "/food-category-new";
    }

//    @GetMapping("/edit/{id}")
//    public String showNewFoodCategoryForm(Model model, @PathVariable Long id) {
//        FoodCategoryDTOSimple foodCategory = foodCategoryService.getFoodCategory(id);
//        model.addAttribute("foodCategory",new FoodCategoryDTOSimple(null,null,null));
//
//        return "/food-category-new";
//    }

    @PostMapping("/create")
    public String createFoodCategory(@ModelAttribute FoodCategoryDTOSimple foodCategory) {
        foodCategoryService.createFoodCategory(foodCategory);

        //todo - redirect to edit to allow addition of new food types?
        return "redirect:/food-category";
    }

    @PostMapping("/update/{id}")
    public String updateFoodCategory(@PathVariable Long id, @ModelAttribute FoodCategoryDTOSimple foodCategory) {
        foodCategoryService.updateFoodCategory(id, foodCategory);

        //todo - redirect to edit to allow addition of new food types?
        return "redirect:/food-category";
    }


}
