package com.ageinghippy.service;

import com.ageinghippy.model.DTOMapper;
import com.ageinghippy.model.dto.DishDTOComplex;
import com.ageinghippy.model.dto.DishDTOSimple;
import com.ageinghippy.model.entity.Dish;
import com.ageinghippy.model.entity.UserPrinciple;
import com.ageinghippy.repository.DishRepository;
import com.ageinghippy.repository.PreparationTechniqueRepository;
import com.ageinghippy.util.Util;
import jakarta.persistence.EntityManager;
import jakarta.validation.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class DishService {
    private final DishRepository dishRepository;
    private final PreparationTechniqueRepository preparationTechniqueRepository;
    private final EntityManager entityManager;
    private final DTOMapper dtoMapper;
    private final Validator validator;

    public DishDTOComplex getDish(Long id) {
        return dtoMapper.map(dishRepository.findById(id).orElseThrow(), DishDTOComplex.class);
    }

    public List<DishDTOSimple> getDishes(UserPrinciple userPrinciple) {
        List<DishDTOSimple> dishes = dtoMapper.mapList(
                dishRepository.findAllByPrinciple(userPrinciple),
                DishDTOSimple.class);
        return dishes;
    }

    @Transactional
    public DishDTOComplex createDish(DishDTOComplex dish, UserPrinciple userPrinciple) {
        Dish newDish = dtoMapper.map(dish, Dish.class);

        newDish.setPrinciple(userPrinciple);

        for (int i = 0; i < newDish.getDishComponents().size(); i++) {
            newDish.getDishComponents().get(i).setDish(newDish);
        }

        newDish = saveDish(newDish);

        return dtoMapper.map(newDish, DishDTOComplex.class);
    }

    @Transactional
    public DishDTOComplex createDish(DishDTOSimple dish, UserPrinciple userPrinciple) {
        Dish newDish = dtoMapper.map(dish, Dish.class);

        newDish.setPrinciple(userPrinciple);

        newDish = saveDish(newDish);

        return dtoMapper.map(newDish, DishDTOComplex.class);
    }

    @Transactional
    private Dish saveDish(Dish dish) {

        //verify specified preparationTechnique exists
        preparationTechniqueRepository.findById(dish.getPreparationTechnique().getId()).orElseThrow();

        //validate dish
        Set<ConstraintViolation<Object>> constraintViolations = validator.validate(dish);
        if (!constraintViolations.isEmpty()) {
            throw new ConstraintViolationException("constraintViolations", constraintViolations);
        }

        dish = dishRepository.save(dish);

        entityManager.flush();
        entityManager.refresh(dish);

        return dish;
    }

    /**
     * Updates the dish identified by the provided id with non-null values as provided in updateDish
     *
     * @param id         The id of the {@code Dish} to update
     * @param updateDish the {@code DishDTOSimple} containing updated values
     * @return the updated {@code DishDTOComplex}
     * @throws java.util.NoSuchElementException if the Dish with the provided id does not exist
     */
    @Transactional
    public DishDTOComplex updateDish(Long id, DishDTOSimple updateDish) {

        Dish dish = dishRepository.findById(id).orElseThrow();
        dish.setName(Util.valueIfNull(updateDish.name(), dish.getName()));
        dish.setDescription(Util.valueIfNull(updateDish.description(), dish.getDescription()));
        if (updateDish.preparationTechnique() != null && updateDish.preparationTechnique().id() != null) {
            dish.setPreparationTechnique(
                    preparationTechniqueRepository.findById(updateDish.preparationTechnique().id()).orElseThrow());
        }

        dish = saveDish(dish);

        return dtoMapper.map(dish, DishDTOComplex.class);
    }

    public void deleteDish(Long id) {
        Dish dish = dishRepository.findById(id).orElseThrow();
        deleteDish(dish);
    }

    private void deleteDish(Dish dish) {
        dishRepository.deleteById(dish.getId());
    }
}