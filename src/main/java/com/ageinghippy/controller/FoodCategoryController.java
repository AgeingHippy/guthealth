package com.ageinghippy.controller;

import com.ageinghippy.model.dto.FoodCategoryDTOComplex;
import com.ageinghippy.model.dto.FoodCategoryDTOSimple;
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
    public List<FoodCategoryDTOSimple> getFoodCategories() {
        return foodCategoryService.getFoodCategories();
    }

    @GetMapping("/{id}")
    public ResponseEntity<FoodCategoryDTOComplex> getFoodCategory(@PathVariable Long id) {
        return ResponseEntity.ok(foodCategoryService.getFoodCategory(id));
    }

    @PostMapping
    public ResponseEntity<FoodCategoryDTOComplex> postFoodCategory(@Valid @RequestBody FoodCategoryDTOSimple foodCategory) {
        if (foodCategory.id() != null) {
            throw new IllegalArgumentException("Food Category ID cannot be specified on new record");
        }
        FoodCategoryDTOComplex foodCategoryDTOComplex = foodCategoryService.createFoodCategory(foodCategory);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(foodCategoryDTOComplex.id())
                .toUri();
        return ResponseEntity.created(location).body(foodCategoryDTOComplex);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FoodCategoryDTOComplex> putFoodCategory(@RequestBody FoodCategoryDTOSimple foodCategory, @PathVariable Long id) {
        return ResponseEntity.ok(foodCategoryService.updateFoodCategory(id, foodCategory));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteFoodCategory(@PathVariable Long id) {
        foodCategoryService.deleteFoodCategory(id);
        return ResponseEntity.noContent().build();
    }

}
