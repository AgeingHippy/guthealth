package com.ageinghippy.repository;

import com.ageinghippy.model.entity.FoodCategory;
import com.ageinghippy.model.entity.UserPrinciple;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FoodCategoryRepository extends JpaRepository<FoodCategory, Long> {

    FoodCategory findByName(String Name);

    List<FoodCategory> findAllByPrinciple(UserPrinciple principle);
}
