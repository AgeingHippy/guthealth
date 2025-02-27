package com.ageinghippy.repository;

import com.ageinghippy.model.FoodType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FoodTypeRepositoryTemp extends JpaRepository<FoodType, Long> {

//    List<FoodType> findByFoodCategoryId(Long foodCategoryId);

    List<FoodType> findByNameContainingIgnoreCase(String infix);

    List<FoodType> findByDescriptionContainingIgnoreCase(String infix);
}
