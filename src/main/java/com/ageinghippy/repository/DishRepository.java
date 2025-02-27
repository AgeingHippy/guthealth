package com.ageinghippy.repository;

import com.ageinghippy.data.model.Dish;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DishRepository extends JpaRepository<Dish, Long> {
}
