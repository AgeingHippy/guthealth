package com.ageinghippy.repository;

import com.ageinghippy.model.entity.Dish;
import com.ageinghippy.model.entity.UserPrinciple;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DishRepository extends JpaRepository<Dish, Long> {

    List<Dish> findAllByPrinciple(UserPrinciple principle);
}