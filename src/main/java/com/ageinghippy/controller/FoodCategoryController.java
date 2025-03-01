package com.ageinghippy.controller;

import com.ageinghippy.model.FoodCategory;
import com.ageinghippy.service.FoodCategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/v1/food-categories")
@RequiredArgsConstructor
public class FoodCategoryController {

    private final FoodCategoryService foodCategoryService;

    @GetMapping
    public ArrayList<FoodCategory> getFoodCategories() {
        return foodCategoryService.getFoodCategories();
    }

    @GetMapping("/{id}")
    public ResponseEntity<FoodCategory>  getFoodCategory(@PathVariable Long id) {
        FoodCategory foodCategory = foodCategoryService.getFoodCategory(id);
        if (foodCategory != null) {
            return ResponseEntity.ok(foodCategory);
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<FoodCategory> postFoodCategory(@Valid @RequestBody FoodCategory foodCategory) {
        if (foodCategory.getId() != null) {
            throw new IllegalArgumentException("Food Category ID cannot be specified on new record");
        }
        foodCategory = foodCategoryService.saveFoodCategory(foodCategory);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(foodCategory.getId())
                .toUri();
        return ResponseEntity.created(location).body(foodCategory);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FoodCategory> putFoodCategory(@RequestBody FoodCategory foodCategory, @PathVariable Long id) {
        try {
            foodCategory = foodCategoryService.updateFoodCategory(id, foodCategory);
            return ResponseEntity.ok(foodCategory);
        } catch(NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
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
