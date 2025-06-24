package com.ageinghippy.service;

import com.ageinghippy.model.DTOMapper;
import com.ageinghippy.model.dto.MealDTOComplex;
import com.ageinghippy.model.dto.MealDTOSimple;
import com.ageinghippy.model.entity.Meal;
import com.ageinghippy.model.entity.UserPrinciple;
import com.ageinghippy.repository.MealRepository;
import com.ageinghippy.util.Util;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MealService {
    private final MealRepository mealRepository;
    private final EntityManager entityManager;
    private final DTOMapper dtoMapper;

    public MealDTOComplex getMeal(Long id) {
        return dtoMapper.map(mealRepository.findById(id).orElseThrow(), MealDTOComplex.class);
    }

    public List<MealDTOSimple> getMeals(UserPrinciple principle) {
        return dtoMapper.mapList(mealRepository.findAllByPrinciple(principle), MealDTOSimple.class);
    }

    @Transactional
    public MealDTOComplex createMeal(MealDTOSimple newMeal, UserPrinciple principle) {
        Meal meal = dtoMapper.map(newMeal, Meal.class);
        meal.setPrinciple(principle);

        return dtoMapper.map(saveMeal(meal),MealDTOComplex.class);
    }

    @Transactional
    public MealDTOComplex createMeal(MealDTOComplex newMeal, UserPrinciple principle) {
        Meal meal = dtoMapper.map(newMeal, Meal.class);
        meal.setPrinciple(principle);

        for (int i = 0; i < meal.getMealComponents().size(); i++) {
            meal.getMealComponents().get(i).setMeal(meal);
        }

        return dtoMapper.map(saveMeal(meal),MealDTOComplex.class);
    }

    @Transactional
    private Meal saveMeal(Meal meal) {
        meal = mealRepository.save(meal);
        entityManager.flush();
        entityManager.refresh(meal);
        return meal;
    }

    /**
     * Updates the meal identified by the provided id with non-null values as provided in updateMeal
     *
     * @param id         The id of the {@code Meal} to update
     * @param updateMeal the {@code MealDTOSimple} containing updated values
     * @return the updated {@code MealDTOComplex}
     * @throws java.util.NoSuchElementException if the Meal with the provided id does not exist
     */
    @Transactional
    public MealDTOComplex updateMeal(Long id, MealDTOSimple updateMeal) {
        Meal meal = mealRepository.findById(id).orElseThrow();
        meal.setDescription(Util.valueIfNull(updateMeal.description(), meal.getDescription()));
        meal.setDate(Util.valueIfNull(updateMeal.date(), meal.getDate()));
        meal.setTime(Util.valueIfNull(updateMeal.time(), meal.getTime()));

        return dtoMapper.map(saveMeal(meal),MealDTOComplex.class);
    }

    private void deleteMeal(Meal meal) {
        mealRepository.deleteById(meal.getId());
    }

    @Transactional
    public void deleteMeal(Long id) {
        Meal meal = mealRepository.findById(id).orElseThrow();
        deleteMeal(meal);
    }
}