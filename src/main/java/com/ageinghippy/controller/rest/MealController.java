package com.ageinghippy.controller.rest;

import com.ageinghippy.model.entity.Meal;
import com.ageinghippy.service.MealService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/v1/meals")
@RequiredArgsConstructor
public class MealController {

    private final MealService mealService;

    @GetMapping
    public List<Meal> getMeals() {
        return mealService.getMeals();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Meal> getMeal(@PathVariable Long id) {
        Meal meal = mealService.getMeal(id);
        if (meal != null) {
            return ResponseEntity.ok(meal);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<Meal> postMeal(@Valid @RequestBody Meal meal) {
        if (meal.getId() != null) {
            throw new IllegalArgumentException("Food Type ID cannot be specified on new record");
        }
        meal = mealService.createMeal(meal);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(meal.getId())
                .toUri();
        return ResponseEntity.created(location).body(meal);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Meal> putMeal(@RequestBody Meal meal, @PathVariable Long id) {
        try {
            meal = mealService.updateMeal(id, meal);
            return ResponseEntity.ok(meal);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteMeal(@PathVariable Long id) {
        Meal meal = mealService.getMeal(id);
        if (meal != null) {
            mealService.deleteMeal(meal);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}