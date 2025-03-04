package com.ageinghippy.repository;

import com.ageinghippy.model.DishComponent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DishComponentRepository extends JpaRepository<DishComponent, Long> {
}