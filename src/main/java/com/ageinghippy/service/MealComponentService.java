package com.ageinghippy.service;

import com.ageinghippy.model.DTOMapper;
import com.ageinghippy.model.dto.MealComponentDTO;
import com.ageinghippy.model.entity.MealComponent;
import com.ageinghippy.repository.*;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
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

    public MealComponentDTO getMealComponent(Long id) {
        return dtoMapper.map(mealComponentRepository.findById(id).orElseThrow(),MealComponentDTO.class);
    }

    @Transactional
    public MealComponentDTO createNewMealComponent(Long mealId, MealComponentDTO newMealComponent) {
        MealComponent mealComponent = dtoMapper.map(newMealComponent,MealComponent.class);

        mealComponent.setMeal(mealRepository.findById(mealId).orElseThrow());
        mealComponent.setFoodType(foodTypeRepository.findById(newMealComponent.foodType().id()).orElseThrow());
        mealComponent.setPreparationTechnique(preparationTechniqueRepository.findById(newMealComponent.preparationTechnique().id()).orElseThrow());

        mealComponent = saveMealComponent(mealComponent);

        return dtoMapper.map(mealComponent,MealComponentDTO.class);
    }

    @Transactional
    public MealComponentDTO updateMealComponent(Long id, MealComponentDTO updatedMealComponent) {
        MealComponent mealComponent = dtoMapper.map(updatedMealComponent, MealComponent.class);
        return dtoMapper.map(saveMealComponent(mealComponent),MealComponentDTO.class);
    }

    private MealComponent saveMealComponent(MealComponent mealComponent) {
        mealComponent = mealComponentRepository.save(mealComponent);

        entityManager.flush();
        entityManager.refresh(mealComponent);

        return mealComponent;
    }

    @Transactional
    public void deleteMealComponent(Long id) {
        MealComponent mealComponent = mealComponentRepository.findById(id).orElseThrow();

        mealComponentRepository.delete(mealComponent);
    }
}
