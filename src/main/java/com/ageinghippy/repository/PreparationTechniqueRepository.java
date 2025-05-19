package com.ageinghippy.repository;

import com.ageinghippy.model.entity.PreparationTechnique;
import com.ageinghippy.model.entity.UserPrinciple;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PreparationTechniqueRepository extends JpaRepository<PreparationTechnique,Long> {

    List<PreparationTechnique> findAllByPrinciple(UserPrinciple principle);

}
