package com.ageinghippy.controller;

import com.ageinghippy.model.FoodType;
import com.ageinghippy.service.FoodTypeServiceTemp;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/food-types-temp")
public class FoodTypeControllerTemp {

    private final FoodTypeServiceTemp foodTypeServiceTemp;

    @GetMapping("/")
    public ArrayList<FoodType> getFoodTypes() {
        return foodTypeServiceTemp.getAllFoodTypes();
    }

    @GetMapping("/{id}")
    public FoodType getFoodType(@PathVariable Long id) {
        return foodTypeServiceTemp.getFoodType(id);
    }

    @PostMapping("/")
    public FoodType postFoodType(@Valid @RequestBody FoodType foodType) {
        return foodTypeServiceTemp.saveFoodType(foodType);
    }

    @PutMapping("/{id}")
    public FoodType putFoodType(@RequestBody FoodType foodType, @PathVariable Long id) {
        return foodTypeServiceTemp.updateFoodType(id, foodType);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteFoodType(@PathVariable Long id) {
        FoodType foodType = foodTypeServiceTemp.getFoodType(id);
        if (foodType != null) {
            foodTypeServiceTemp.deleteFoodType(foodType);
            return ResponseEntity.noContent().build();
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }

}
