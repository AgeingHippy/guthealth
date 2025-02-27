package com.ageinghippy.controller;

import com.ageinghippy.model.FoodCategory;
import com.ageinghippy.service.FoodCategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/food-categories")
@RequiredArgsConstructor
public class FoodCategoryController {

    private final FoodCategoryService foodCategoryService;

    @GetMapping("/")
    public ArrayList<FoodCategory> getFoodCategories() {
        return foodCategoryService.getFoodCategories();
    }

    @GetMapping("/{id}")
    public FoodCategory getFoodCategory(@PathVariable Long id) {
        return foodCategoryService.getFoodCategory(id);
    }

    @PostMapping("/")
    public FoodCategory postFoodCategory(@Valid @RequestBody FoodCategory foodCategory) {
        return foodCategoryService.saveFoodCategory(foodCategory);
    }

    @PutMapping("/{id}")
    public FoodCategory putFoodCategory(@RequestBody FoodCategory foodCategory, @PathVariable Long id) {
        return foodCategoryService.updateFoodCategory(id, foodCategory);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteFoodCategory(@PathVariable Long id) {
        FoodCategory foodCategory = foodCategoryService.getFoodCategory(id);
        if (foodCategory != null) {
            foodCategoryService.deleteFoodCategory(foodCategory);
            return ResponseEntity.noContent().build();
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }

}
