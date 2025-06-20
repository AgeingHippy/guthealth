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
    public Meal createMeal(Meal meal) {
        meal.getMealComponents().forEach(mealComponent -> mealComponent.setMeal(meal));
        return saveMeal(meal);
    }

    @Transactional
    public Meal saveMeal(Meal meal) {
        meal = mealRepository.save(meal);
        entityManager.flush();
        entityManager.refresh(meal);
        return meal;
    }

    /**
     * Updates the meal identified by the provided id with non-null values as provided in updateMeal
     *
     * @param id         The id of the {@code Meal} to update
     * @param updateMeal the {@code Meal} containing updated values
     * @return the updated {@code Meal}
     * @throws java.util.NoSuchElementException if the Meal with the provided id does not exist
     */
    @Transactional
    public Meal updateMeal(Long id, Meal updateMeal) {
        Meal meal = mealRepository.findById(id).orElseThrow();
        meal.setDate(Util.valueIfNull(updateMeal.getDate(), meal.getDate()));
        meal.setTime(Util.valueIfNull(updateMeal.getTime(), meal.getTime()));
        //todo - add support for mealComponents

        return saveMeal(meal);
    }

    public void deleteMeal(Meal meal) {
        mealRepository.deleteById(meal.getId());
    }

    public void deleteMeal(Long id) {
        Meal meal = mealRepository.findById(id).orElseThrow();
        deleteMeal(meal);
    }
}