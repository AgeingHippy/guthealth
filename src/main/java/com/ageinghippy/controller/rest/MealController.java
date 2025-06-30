package com.ageinghippy.controller.rest;

import com.ageinghippy.model.dto.MealDTOComplex;
import com.ageinghippy.model.dto.MealDTOSimple;
import com.ageinghippy.model.entity.Meal;
import com.ageinghippy.model.entity.UserPrinciple;
import com.ageinghippy.service.MealService;
import com.ageinghippy.service.UserPrincipleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
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
    private final UserPrincipleService userPrincipleService;

    @GetMapping
    public List<MealDTOSimple> getMeals(Authentication authentication) {
        return mealService.getMeals((UserPrinciple) authentication.getPrincipal());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasPermission(#id,'Meal','read')")
    public ResponseEntity<MealDTOComplex> getMeal(@PathVariable Long id) {
        MealDTOComplex meal = mealService.getMeal(id);
        if (meal != null) {
            return ResponseEntity.ok(meal);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<MealDTOComplex> postMeal(@Valid @RequestBody MealDTOComplex meal, Authentication authentication) {
        if (meal.id() != null) {
            throw new IllegalArgumentException("Food Type ID cannot be specified on new record");
        }
        MealDTOComplex newMeal = mealService.createMeal(meal,userPrincipleService.castToUserPrinciple(authentication.getPrincipal()));
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(newMeal.id())
                .toUri();
        return ResponseEntity.created(location).body(newMeal);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasPermission(#id,'Meal','edit')")
    public ResponseEntity<MealDTOComplex> putMeal(@RequestBody MealDTOSimple meal, @PathVariable Long id) {
        if (!id.equals(meal.id())) {
            throw new IllegalArgumentException("The id specified in the request body must match the value specified in the url");
        }
        try {
            MealDTOComplex updatedMeal = mealService.updateMeal(id, meal);
            return ResponseEntity.ok(updatedMeal);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasPermission(#id,'Meal','delete')")
    public ResponseEntity<String> deleteMeal(@PathVariable Long id) {
        mealService.deleteMeal(id);
        return ResponseEntity.noContent().build();
    }

}