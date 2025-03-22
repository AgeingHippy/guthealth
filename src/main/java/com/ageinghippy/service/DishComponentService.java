package com.ageinghippy.service;

import com.ageinghippy.model.DTOMapper;
import com.ageinghippy.model.dto.DishComponentDTO;
import com.ageinghippy.model.entity.DishComponent;
import com.ageinghippy.repository.DishComponentRepository;
import com.ageinghippy.repository.DishRepository;
import com.ageinghippy.repository.FoodTypeRepository;
import com.ageinghippy.util.Util;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DishComponentService {
    private final DishComponentRepository dishComponentRepository;
    private final FoodTypeRepository foodTypeRepository;
    private final DishRepository dishRepository;
    private final DTOMapper dtoMapper;
    private final EntityManager entityManager;

    public DishComponentDTO getDishComponent(Long id) {
        return dtoMapper.map(dishComponentRepository.findById(id).orElseThrow(), DishComponentDTO.class);
    }

    public List<DishComponentDTO> getDishComponents(Long dishId) {
        return dtoMapper.mapList(dishComponentRepository.findAllByDishId(dishId), DishComponentDTO.class);
    }

    @Transactional
    public DishComponentDTO createDishComponent(Long dishId, DishComponentDTO dishComponent) {
        DishComponent newDishComponent = dtoMapper.map(dishComponent, DishComponent.class);

        newDishComponent.setDish(dishRepository.findById(dishId).orElseThrow());
        newDishComponent.setFoodType(foodTypeRepository.findById(dishComponent.foodType().id()).orElseThrow());

        newDishComponent = saveDishComponent(newDishComponent);

        return dtoMapper.map(newDishComponent, DishComponentDTO.class);
    }

    private DishComponent saveDishComponent(DishComponent dishComponent) {
        dishComponent = dishComponentRepository.save(dishComponent);

        entityManager.flush();
        entityManager.refresh(dishComponent);

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
    @Transactional
    public DishComponentDTO updateDishComponent(Long id, DishComponentDTO updateDishComponent) {
        DishComponent dishComponent = dishComponentRepository.findById(id).orElseThrow();

        dishComponent.setProportion(Util.valueIfNull(updateDishComponent.proportion(), dishComponent.getProportion()));
        if (updateDishComponent.foodType().id() != null) {
            dishComponent.setFoodType(foodTypeRepository.findById(updateDishComponent.foodType().id()).orElseThrow());
        }

        return dtoMapper.map(saveDishComponent(dishComponent), DishComponentDTO.class);
    }

    @Transactional
    public void deleteDishComponent(Long id) {
        DishComponent dishComponent = dishComponentRepository.findById(id).orElseThrow();
        deleteDishComponent(dishComponent);
    }

    private void deleteDishComponent(DishComponent dishComponent) {
        dishComponentRepository.deleteById(dishComponent.getId());
    }
}