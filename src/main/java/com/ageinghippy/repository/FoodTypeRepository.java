package com.ageinghippy.repository;

import com.ageinghippy.model.FoodType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FoodTypeRepository extends JpaRepository<FoodType, Long> {

    List<FoodType> findByFoodCategory_id(Long foodCategoryId);

    List<FoodType> findByNameContainingIgnoreCase(String infix);

    List<FoodType> findByDescriptionContainingIgnoreCase(String infix);
}
