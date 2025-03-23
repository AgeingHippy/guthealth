package com.ageinghippy.controller.rest;

import com.ageinghippy.model.dto.DishDTOComplex;
import com.ageinghippy.model.dto.DishDTOSimple;
import com.ageinghippy.service.DishService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/dishes")
@RequiredArgsConstructor
public class DishController {

    private final DishService dishService;

    @GetMapping
    public List<DishDTOSimple> getDishes(Authentication authentication) {
        return dishService.getDishes();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasPermission(#id,'Dish','read')")
    public ResponseEntity<DishDTOComplex> getDish(@PathVariable Long id) {
        return ResponseEntity.ok(dishService.getDish(id));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
    public ResponseEntity<DishDTOComplex> postFullDish(@RequestBody DishDTOComplex dish, Authentication authentication) {
        if (dish.id() != null) {
            throw new IllegalArgumentException("Dish ID cannot be specified on new record");
        }
        DishDTOComplex newDish = dishService.createDish(dish, authentication);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(newDish.id())
                .toUri();
        return ResponseEntity.created(location).body(newDish);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasPermission(#id,'Dish','edit')")
    public ResponseEntity<DishDTOComplex> putDish(@RequestBody DishDTOSimple dish, @PathVariable Long id) {
        if (!id.equals(dish.id())) {
            throw new IllegalArgumentException("The id specified in the request body must match the value specified in the url");
        }
        return ResponseEntity.ok(dishService.updateDish(id, dish));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasPermission(#id,'Dish','delete')")
    public ResponseEntity<String> deleteDish(@PathVariable Long id) {
        dishService.deleteDish(id);
        return ResponseEntity.noContent().build();
    }

}