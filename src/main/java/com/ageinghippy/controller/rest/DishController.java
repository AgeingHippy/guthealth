package com.ageinghippy.controller.rest;

import com.ageinghippy.model.dto.DishDTOComplex;
import com.ageinghippy.model.dto.DishDTOSimple;
import com.ageinghippy.model.entity.Dish;
import com.ageinghippy.model.entity.DishComponent;
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
@RequestMapping("/api/v1/dishes")
@RequiredArgsConstructor
public class DishController {

    private final DishService dishService;

    @GetMapping
    public List<DishDTOSimple> getDishes() {
        return dishService.getDishes();
    }

    @GetMapping("/{id}")
    public ResponseEntity<DishDTOComplex> getDish(@PathVariable Long id) {
        return ResponseEntity.ok(dishService.getDish(id));
    }

    @PostMapping
    public ResponseEntity<DishDTOComplex> postDish(@Valid @RequestBody DishDTOSimple dish) {
        if (dish.id() != null) {
            throw new IllegalArgumentException("Dish ID cannot be specified on new record");
        }
        DishDTOComplex newDish = dishService.createDish(dish);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(newDish.id())
                .toUri();
        return ResponseEntity.created(location).body(newDish);
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
        dishService.deleteDish(id);
        return ResponseEntity.noContent().build();
    }

}