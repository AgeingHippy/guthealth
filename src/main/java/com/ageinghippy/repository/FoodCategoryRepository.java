package com.ageinghippy.repository;

import com.ageinghippy.model.FoodCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FoodCategoryRepository extends JpaRepository<FoodCategory, Long> {

    FoodCategory findByName(String Name);
}
