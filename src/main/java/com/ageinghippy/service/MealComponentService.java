package com.ageinghippy.service;

import com.ageinghippy.model.DTOMapper;
import com.ageinghippy.model.dto.MealComponentDTO;
import com.ageinghippy.repository.*;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MealComponentService {

    private final MealRepository mealRepository;
    private final MealComponentRepository mealComponentRepository;
    private final FoodTypeRepository foodTypeRepository;
    private final PreparationTechniqueRepository preparationTechniqueRepository;
    private final DTOMapper dtoMapper;
    private final EntityManager entityManager;

    public MealComponentDTO createNewMealComponent(Long mealId, MealComponentDTO newMealComponent) {

    }
}
