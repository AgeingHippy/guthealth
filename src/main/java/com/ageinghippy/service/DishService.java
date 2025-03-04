package com.ageinghippy.service;

import com.ageinghippy.model.Dish;
import com.ageinghippy.repository.DishRepository;
import com.ageinghippy.util.Util;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DishService {
    private final DishRepository dishRepository;
    private final EntityManager entityManager;

    public Dish getDish(Long id) {
        return dishRepository.findById(id).orElse(null);
    }

    public List<Dish> getDishes() {
        List<Dish> dishes = dishRepository.findAll();
        return dishes;
    }

    @Transactional
    public Dish createDish(Dish dish) {
        dish.getDishComponents().forEach(dishComponent -> dishComponent.setDish(dish));
        return saveDish(dish);
//        entityManager.refresh(dish);
//        return dish;
    }

    @Transactional
    public Dish saveDish(Dish dish) {
        dish = dishRepository.save(dish);
        entityManager.flush();
        entityManager.refresh(dish);
        return dish;
    }

    /**
     * Updates the dish identified by the provided id with non-null values as provided in updateDish
     *
     * @param id         The id of the {@code Dish} to update
     * @param updateDish the {@code Dish} containing updated values
     * @return the updated {@code Dish}
     * @throws java.util.NoSuchElementException if the Dish with the provided id does not exist
     */
    @Transactional
    public Dish updateDish(Long id, Dish updateDish) {
        Dish dish = dishRepository.findById(id).orElseThrow();
        dish.setName(Util.valueIfNull(updateDish.getName(), dish.getName()));
        dish.setDescription(Util.valueIfNull(updateDish.getDescription(), dish.getDescription()));
        dish.setPreparationTechnique(Util.valueIfNull(updateDish.getPreparationTechnique(),dish.getPreparationTechnique()));
        //todo - add additional as appropriate

        return saveDish(dish);
    }

    public void deleteDish(Dish dish) {
        dishRepository.deleteById(dish.getId());
    }
}