package com.ageinghippy.service;

import com.ageinghippy.model.entity.DishComponent;
import com.ageinghippy.repository.DishComponentRepository;
import com.ageinghippy.repository.FoodTypeRepository;
import com.ageinghippy.util.Util;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DishComponentService {
    private final DishComponentRepository dishComponentRepository;
    private final FoodTypeRepository foodTypeRepository;

    public DishComponent getDishComponent(Long id) {
        return dishComponentRepository.findById(id).orElse(null);
    }

    public List<DishComponent> getDishComponents() {
        return dishComponentRepository.findAll();
    }

    public DishComponent createDishComponent(DishComponent dishComponent) {

        return saveDishComponent(dishComponent);
    }

    public DishComponent saveDishComponent(DishComponent dishComponent) {
        dishComponent = dishComponentRepository.save(dishComponent);

        return dishComponent;
    }

    /**
     * Updates the dishComponent identified by the provided id with non-null values as provided in updateDishComponent
     *
     * @param id                  The id of the {@code DishComponent} to update
     * @param updateDishComponent the {@code DishComponent} containing updated values
     * @return the updated {@code DishComponent}
     * @throws java.util.NoSuchElementException if the DishComponent with the provided id does not exist
     */
    public DishComponent updateDishComponent(Long id, DishComponent updateDishComponent) {
        DishComponent dishComponent = dishComponentRepository.findById(id).orElseThrow();
        dishComponent.setProportion(Util.valueIfNull(updateDishComponent.getProportion(), dishComponent.getProportion()));
        if (updateDishComponent.getFoodType().getId() != null) {
            dishComponent.setFoodType(foodTypeRepository.findById(updateDishComponent.getFoodType().getId()).orElseThrow());
        }

        return saveDishComponent(dishComponent);
    }

    public void deleteDishComponent(DishComponent dishComponent) {
        dishComponentRepository.deleteById(dishComponent.getId());
    }
}