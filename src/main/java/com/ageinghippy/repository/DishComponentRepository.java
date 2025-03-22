package com.ageinghippy.repository;

import com.ageinghippy.model.entity.DishComponent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DishComponentRepository extends JpaRepository<DishComponent, Long> {

    List<DishComponent> findAllByDishId(Long dishId);
}