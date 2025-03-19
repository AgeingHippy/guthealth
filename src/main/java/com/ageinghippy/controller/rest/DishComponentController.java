package com.ageinghippy.controller.rest;

import com.ageinghippy.model.entity.DishComponent;
import com.ageinghippy.service.DishComponentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
    public List<DishComponent> getDishComponents() {
        return dishComponentService.getDishComponents();
    }

    @GetMapping("/{id}")
    public ResponseEntity<DishComponent> getDishComponent(@PathVariable Long id) {
        DishComponent dishComponent = dishComponentService.getDishComponent(id);
        if (dishComponent != null) {
            return ResponseEntity.ok(dishComponent);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<DishComponent> postDishComponent(@Valid @RequestBody DishComponent dishComponent) {
        if (dishComponent.getId() != null) {
            throw new IllegalArgumentException("Food Type ID cannot be specified on new record");
        }
        dishComponent = dishComponentService.createDishComponent(dishComponent);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(dishComponent.getId())
                .toUri();
        return ResponseEntity.created(location).body(dishComponent);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DishComponent> putDishComponent(@RequestBody DishComponent dishComponent, @PathVariable Long id) {
        if (!id.equals(dishComponent.getId())) {
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
    public ResponseEntity<String> deleteDishComponent(@PathVariable Long id) {
        DishComponent dishComponent = dishComponentService.getDishComponent(id);
        if (dishComponent != null) {
            dishComponentService.deleteDishComponent(dishComponent);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}