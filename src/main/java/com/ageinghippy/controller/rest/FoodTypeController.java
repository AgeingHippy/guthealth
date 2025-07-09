package com.ageinghippy.controller.rest;

import com.ageinghippy.model.dto.FoodTypeDTOComplex;
import com.ageinghippy.model.dto.FoodTypeDTOSimple;
import com.ageinghippy.model.entity.UserPrinciple;
import com.ageinghippy.service.FoodTypeService;
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
@RequestMapping("/api/v1/food-types")
@RequiredArgsConstructor
public class FoodTypeController {

    private final FoodTypeService foodTypeService;
    private final UserPrincipleService userPrincipleService;

    @GetMapping("/all")
    public List<FoodTypeDTOComplex> getAllFoodTypes(Authentication authentication) {
        UserPrinciple principle = userPrincipleService.castToUserPrinciple(authentication.getPrincipal());
        return foodTypeService.getFoodTypesByPrinciple(principle);
    }

    @GetMapping
    @PreAuthorize("hasPermission(#foodCategoryId,'FoodCategory','read')")
    public List<FoodTypeDTOSimple> getFoodTypes(@RequestParam(required = true) Long foodCategoryId) {
        return foodTypeService.getFoodTypes(foodCategoryId);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasPermission(#id,'FoodType','read')")
    public ResponseEntity<FoodTypeDTOComplex> getFoodType(@PathVariable Long id) {
        return ResponseEntity.ok(foodTypeService.getFoodType(id));
    }

    @PostMapping
    @PreAuthorize("hasPermission(#foodType.foodCategory.id,'FoodCategory','edit')")
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
    @PreAuthorize("hasPermission(#id,'FoodType','edit')")
    public ResponseEntity<FoodTypeDTOComplex> putFoodType(@RequestBody FoodTypeDTOComplex foodType, @PathVariable Long id) {
        if (!id.equals(foodType.id())) {
            throw new IllegalArgumentException("The id specified in the request body must match the value specified in the url");
        }
        try {
            foodType = foodTypeService.updateFoodType(id, foodType);
            return ResponseEntity.ok(foodType);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasPermission(#id,'FoodType','delete')")
    public ResponseEntity<String> deleteFoodType(@PathVariable Long id) {
        foodTypeService.deleteFoodType(id);
        return ResponseEntity.noContent().build();
    }

}