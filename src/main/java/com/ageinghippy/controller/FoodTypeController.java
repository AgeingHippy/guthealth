package com.ageinghippy.controller;

import com.ageinghippy.model.FoodCategory;
import com.ageinghippy.model.FoodType;
import com.ageinghippy.service.FoodTypeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RequiredArgsConstructor
@RestController
@RequestMapping("/food-types")
public class FoodTypeController {

    private final FoodTypeService foodTypeService;

    @GetMapping("/")
    public ArrayList<FoodType> getFoodTypes() {
        return foodTypeService.getAllFoodTypes();
    }

    @GetMapping("/{id}")
    public FoodType getFoodType(@PathVariable Long id) {
        return foodTypeService.getFoodType(id);
    }

    @PostMapping("/")
    public FoodType postFoodType(@Valid @RequestBody FoodType foodType) {
        return foodTypeService.saveFoodType(foodType);
    }

    @PutMapping("/{id}")
    public FoodType putFoodType(@RequestBody FoodType foodType, @PathVariable Long id) {
        return foodTypeService.updateFoodType(id, foodType);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteFoodType(@PathVariable Long id) {
        FoodType foodType = foodTypeService.getFoodType(id);
        if (foodType != null) {
            foodTypeService.deleteFoodType(foodType);
            return ResponseEntity.noContent().build();
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }

}
