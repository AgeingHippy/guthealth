package com.ageinghippy.controller.api;

import com.ageinghippy.model.dto.FoodTypeDTOComplex;
import com.ageinghippy.model.dto.FoodTypeDTOSimple;
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
@RequestMapping("/api/v1/food-types")
@RequiredArgsConstructor
public class FoodTypeController {

    private final FoodTypeService foodTypeService;

    @GetMapping
    //todo - is there value in this call? - content could become overwhelming and meaningless.... Add foodCategoryId path parameter?
    public List<FoodTypeDTOSimple> getFoodTypes() {
        return foodTypeService.getFoodTypes();
    }

    @GetMapping("/{id}")
    public ResponseEntity<FoodTypeDTOComplex> getFoodType(@PathVariable Long id) {
        return ResponseEntity.ok(foodTypeService.getFoodType(id));
    }

    @PostMapping
    public ResponseEntity<FoodTypeDTOComplex> postFoodType(@Valid @RequestBody FoodTypeDTOComplex foodType) {
        if (foodType.id() != null) {
            throw new IllegalArgumentException("Food Type ID cannot be specified on new record");
        }
        foodType = foodTypeService.createFoodType(foodType);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(foodType.id())
                .toUri();
        return ResponseEntity.created(location).body(foodType);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FoodTypeDTOComplex> putFoodType(@RequestBody FoodTypeDTOComplex foodType, @PathVariable Long id) {
        try {
            foodType = foodTypeService.updateFoodType(id, foodType);
            return ResponseEntity.ok(foodType);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteFoodType(@PathVariable Long id) {
        foodTypeService.deleteFoodType(id);
        return ResponseEntity.noContent().build();
    }

}