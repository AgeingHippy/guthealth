package com.ageinghippy.repository;

import com.ageinghippy.model.entity.FoodCategory;
import com.ageinghippy.model.entity.UserPrinciple;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FoodCategoryRepository extends JpaRepository<FoodCategory, Long> {

    Optional<FoodCategory> findByNameAndPrinciple(String Name, UserPrinciple principle);

    List<FoodCategory> findAllByPrinciple(UserPrinciple principle);
}
