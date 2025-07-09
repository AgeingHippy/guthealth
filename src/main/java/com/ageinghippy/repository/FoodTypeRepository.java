package com.ageinghippy.repository;

import com.ageinghippy.model.entity.FoodType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FoodTypeRepository extends JpaRepository<FoodType, Long> {

    List<FoodType> findAllByFoodCategory_id(Long foodCategoryId);

    @Query(value= "SELECT ft FROM FoodType ft JOIN ft.foodCategory fc WHERE fc.principle.id = :principleId")
    List<FoodType> findAllByPrincipleId(@Param("principleId") Long principleId);
}