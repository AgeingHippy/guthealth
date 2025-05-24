package com.ageinghippy.repository;

import com.ageinghippy.model.entity.FoodType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FoodTypeRepository extends JpaRepository<FoodType, Long> {

    List<FoodType> findAllByFoodCategory_id(Long foodCategoryId);
}