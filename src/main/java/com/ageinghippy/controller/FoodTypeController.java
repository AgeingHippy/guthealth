package com.ageinghippy.controller;

import com.ageinghippy.model.FoodType;
import com.ageinghippy.service.FoodTypeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/v1/food-types")
@RequiredArgsConstructor
public class FoodTypeController {

    private final FoodTypeService foodTypeService;

    @GetMapping
    public List<FoodType> getFoodTypes() {
        return foodTypeService.getFoodTypes();
    }

    @GetMapping("/{id}")
    public ResponseEntity<FoodType> getFoodType(@PathVariable Long id) {
        FoodType foodType = foodTypeService.getFoodType(id);
        if (foodType != null) {
            return ResponseEntity.ok(foodType);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<FoodType> postFoodType(@Valid @RequestBody FoodType foodType) {
        if (foodType.getId() != null) {
            throw new IllegalArgumentException("Food Type ID cannot be specified on new record");
        }
        foodType = foodTypeService.createFoodType(foodType);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(foodType.getId())
                .toUri();
        return ResponseEntity.created(location).body(foodType);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FoodType> putFoodType(@RequestBody FoodType foodType, @PathVariable Long id) {
        try {
            foodType = foodTypeService.updateFoodType(id, foodType);
            return ResponseEntity.ok(foodType);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteFoodType(@PathVariable Long id) {
        FoodType foodType = foodTypeService.getFoodType(id);
        if (foodType != null) {
            foodTypeService.deleteFoodType(foodType);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}