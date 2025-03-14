package com.ageinghippy.controller;

import com.ageinghippy.model.entity.FoodCategory;
import com.ageinghippy.service.FoodCategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/v1/food-categories")
@RequiredArgsConstructor
public class FoodCategoryController {

    private final FoodCategoryService foodCategoryService;

    @GetMapping
    public List<FoodCategory> getFoodCategories() {
        return foodCategoryService.getFoodCategories();
    }

    @GetMapping("/{id}")
    public ResponseEntity<FoodCategory> getFoodCategory(@PathVariable Long id) {
        return ResponseEntity.ok(foodCategoryService.getFoodCategory(id));
    }

    @PostMapping
    public ResponseEntity<FoodCategory> postFoodCategory(@Valid @RequestBody FoodCategory foodCategory) {
        if (foodCategory.getId() != null) {
            throw new IllegalArgumentException("Food Category ID cannot be specified on new record");
        }
        foodCategory = foodCategoryService.createFoodCategory(foodCategory);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(foodCategory.getId())
                .toUri();
        return ResponseEntity.created(location).body(foodCategory);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FoodCategory> putFoodCategory(@RequestBody FoodCategory foodCategory, @PathVariable Long id) {
        foodCategory = foodCategoryService.updateFoodCategory(id, foodCategory);
        return ResponseEntity.ok(foodCategory);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteFoodCategory(@PathVariable Long id) {
        FoodCategory foodCategory = foodCategoryService.getFoodCategory(id);
        foodCategoryService.deleteFoodCategory(foodCategory);
        return ResponseEntity.noContent().build();
    }

}
