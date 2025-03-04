package com.ageinghippy.service;

import com.ageinghippy.model.Meal;
import com.ageinghippy.repository.MealRepository;
import com.ageinghippy.util.Util;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Transient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MealService {
    private final MealRepository mealRepository;
    private final EntityManager entityManager;

    public Meal getMeal(Long id) {
        return mealRepository.findById(id).orElse(null);
    }

    public List<Meal> getMeals() {
        return mealRepository.findAll();
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
}