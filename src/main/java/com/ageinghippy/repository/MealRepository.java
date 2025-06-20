package com.ageinghippy.repository;

import com.ageinghippy.model.entity.Meal;
import com.ageinghippy.model.entity.UserPrinciple;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MealRepository extends JpaRepository<Meal, Long> {

    List<Meal> findAllByPrinciple(UserPrinciple principle);
}