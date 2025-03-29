package com.ageinghippy.controller.rest;

import com.ageinghippy.model.dto.DishComponentDTO;
import com.ageinghippy.model.entity.DishComponent;
import com.ageinghippy.service.DishComponentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/v1/dish-components")
@RequiredArgsConstructor
public class DishComponentController {

    private final DishComponentService dishComponentService;

    @GetMapping
    @PreAuthorize("hasPermission(#dishId,'Dish','read')")
    public List<DishComponentDTO> getDishComponents(@RequestParam Long dishId) {
        return dishComponentService.getDishComponents(dishId);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasPermission(#id,'DishComponent','read')")
    public ResponseEntity<DishComponentDTO> getDishComponent(@PathVariable Long id) {
        DishComponentDTO dishComponent = dishComponentService.getDishComponent(id);
        if (dishComponent != null) {
            return ResponseEntity.ok(dishComponent);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    @PreAuthorize("hasPermission(#dishId,'Dish','edit')")
    public ResponseEntity<DishComponentDTO> postDishComponent(@RequestParam Long dishId, @RequestBody DishComponentDTO dishComponent) {
        if (dishComponent.id() != null) {
            throw new IllegalArgumentException("ID cannot be specified on new record");
        }
        dishComponent = dishComponentService.createDishComponent(dishId, dishComponent);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequestUri()
                .path("/{id}")
                .buildAndExpand(dishComponent.id())
                .toUri();
        return ResponseEntity.created(location).body(dishComponent);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasPermission(#id,'DishComponent','edit')")
    public ResponseEntity<DishComponentDTO> putDishComponent(@RequestBody DishComponentDTO dishComponent, @PathVariable Long id) {
        if (!id.equals(dishComponent.id())) {
            throw new IllegalArgumentException("The id specified in the request body must match the value specified in the url");
        }
        try {
            dishComponent = dishComponentService.updateDishComponent(id, dishComponent);
            return ResponseEntity.ok(dishComponent);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasPermission(#id,'DishComponent','delete')")
    public ResponseEntity<String> deleteDishComponent(@PathVariable Long id) {
        dishComponentService.deleteDishComponent(id);
        return ResponseEntity.noContent().build();
    }

}