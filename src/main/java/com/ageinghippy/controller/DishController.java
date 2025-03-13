package com.ageinghippy.controller;

import com.ageinghippy.model.entity.Dish;
import com.ageinghippy.service.DishService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/v1/dishes")
@RequiredArgsConstructor
public class DishController {

    private final DishService dishService;

    @GetMapping
    public List<Dish> getDishes() {
        return dishService.getDishes();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Dish> getDish(@PathVariable Long id) {
        Dish dish = dishService.getDish(id);
        if (dish != null) {
            return ResponseEntity.ok(dish);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<Dish> postDish(@Valid @RequestBody Dish dish) {
        if (dish.getId() != null) {
            throw new IllegalArgumentException("Food Type ID cannot be specified on new record");
        }
        dish = dishService.createDish(dish);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(dish.getId())
                .toUri();
        return ResponseEntity.created(location).body(dish);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Dish> putDish(@RequestBody Dish dish, @PathVariable Long id) {
        try {
            dish = dishService.updateDish(id, dish);
            return ResponseEntity.ok(dish);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteDish(@PathVariable Long id) {
        Dish dish = dishService.getDish(id);
        if (dish != null) {
            dishService.deleteDish(dish);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}