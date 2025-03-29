package com.ageinghippy.controller.rest;

import com.ageinghippy.model.dto.FoodCategoryDTOComplex;
import com.ageinghippy.model.dto.FoodCategoryDTOSimple;
import com.ageinghippy.model.entity.UserPrinciple;
import com.ageinghippy.service.FoodCategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/food-categories")
@RequiredArgsConstructor
public class FoodCategoryController {

    private final FoodCategoryService foodCategoryService;

    @GetMapping
    public List<FoodCategoryDTOSimple> getFoodCategories(Authentication authentication) {
        return foodCategoryService.getFoodCategories((UserPrinciple) authentication.getPrincipal());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasPermission(#id,'FoodCategory','read')")
    public ResponseEntity<FoodCategoryDTOComplex> getFoodCategory(@PathVariable Long id) {
        return ResponseEntity.ok(foodCategoryService.getFoodCategory(id));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
    public ResponseEntity<FoodCategoryDTOComplex> postFoodCategory(@RequestBody FoodCategoryDTOSimple foodCategory,
                                                                   Authentication authentication) {
        if (foodCategory.id() != null) {
            throw new IllegalArgumentException("Food Category ID cannot be specified on new record");
        }
        FoodCategoryDTOComplex foodCategoryDTOComplex = foodCategoryService.createFoodCategory(foodCategory, (UserPrinciple) authentication.getPrincipal());
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(foodCategoryDTOComplex.id())
                .toUri();
        return ResponseEntity.created(location).body(foodCategoryDTOComplex);
    }

    //todo - prototype method
    @PostMapping("/complex")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
    public ResponseEntity<FoodCategoryDTOComplex> postFoodCategoryComplex(@RequestBody FoodCategoryDTOComplex foodCategory,
                                                                          Authentication authentication) {
        if (foodCategory.id() != null) {
            throw new IllegalArgumentException("Food Category ID cannot be specified on new record");
        }
        FoodCategoryDTOComplex foodCategoryDTOComplex = foodCategoryService.createFoodCategory(foodCategory,(UserPrinciple) authentication.getPrincipal());
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(foodCategoryDTOComplex.id())
                .toUri();
        return ResponseEntity.created(location).body(foodCategoryDTOComplex);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasPermission(#id,'FoodCategory','edit')")
    public ResponseEntity<FoodCategoryDTOComplex> putFoodCategory(@RequestBody FoodCategoryDTOSimple foodCategory, @PathVariable Long id) {
        if (!id.equals(foodCategory.id())) {
            throw new IllegalArgumentException("The id specified in the request body must match the value specified in the url");
        }
        return ResponseEntity.ok(foodCategoryService.updateFoodCategory(id, foodCategory));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasPermission(#id,'FoodCategory','delete')")
    public ResponseEntity<String> deleteFoodCategory(@PathVariable Long id) {
        foodCategoryService.deleteFoodCategory(id);
        return ResponseEntity.noContent().build();
    }

}
