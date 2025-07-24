package com.ageinghippy.repository;

import com.ageinghippy.model.entity.MealComponent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MealComponentRepository extends JpaRepository<MealComponent, Long> {
}
