package com.ageinghippy.service;

import com.ageinghippy.model.DTOMapper;
import com.ageinghippy.model.dto.DishDTOComplex;
import com.ageinghippy.model.dto.DishDTOSimple;
import com.ageinghippy.model.entity.Dish;
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
    private final DTOMapper dtoMapper;

    public DishDTOComplex getDish(Long id) {
        return dtoMapper.map(dishRepository.findById(id).orElseThrow(), DishDTOComplex.class);
    }

    public List<DishDTOSimple> getDishes() {
        List<DishDTOSimple> dishes = dtoMapper.mapList(dishRepository.findAll(), DishDTOSimple.class);
        return dishes;
    }

    @Transactional
    public DishDTOComplex createDish(DishDTOSimple dish) {
        Dish newDish = dtoMapper.map(dish, Dish.class);

        newDish = saveDish(newDish);

        return dtoMapper.map(newDish, DishDTOComplex.class);
    }

    @Transactional
    public DishDTOComplex createDish(DishDTOComplex dish) {
        Dish newDish = dtoMapper.map(dish, Dish.class);

        for (int i =0; i < newDish.getDishComponents().size(); i++ ) {
            newDish.getDishComponents().get(i).setDish(newDish);
        }

        newDish = saveDish(newDish);

        return dtoMapper.map(newDish, DishDTOComplex.class);
    }

    @Transactional
    private Dish saveDish(Dish dish) {
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
        dish.setPreparationTechnique(Util.valueIfNull(updateDish.getPreparationTechnique(), dish.getPreparationTechnique()));
        //todo - add additional as appropriate

        return saveDish(dish);
    }

    public void deleteDish(Long id) {
        Dish dish = dishRepository.findById(id).orElseThrow();
        deleteDish(dish);
    }

    private void deleteDish(Dish dish) {
        dishRepository.deleteById(dish.getId());
    }
}