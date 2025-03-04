package com.ageinghippy.repository;

import com.ageinghippy.model.Dish;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DishRepository2 extends JpaRepository<Dish, Long> {
}
