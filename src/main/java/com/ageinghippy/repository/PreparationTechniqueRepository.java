package com.ageinghippy.repository;

import com.ageinghippy.model.entity.PreparationTechnique;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PreparationTechniqueRepository extends JpaRepository<PreparationTechnique,String> {}
