package com.ageinghippy.service;

import com.ageinghippy.model.DTOMapper;
import com.ageinghippy.model.dto.FoodTypeDTOComplex;
import com.ageinghippy.model.dto.FoodTypeDTOSimple;
import com.ageinghippy.model.entity.FoodType;
import com.ageinghippy.repository.FoodCategoryRepository;
import com.ageinghippy.repository.FoodTypeRepository;
import com.ageinghippy.util.Util;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FoodTypeService {
    private final FoodTypeRepository foodTypeRepository;
    private final FoodCategoryRepository foodCategoryRepository;
    private final DTOMapper dtoMapper;
    private final EntityManager entityManager;

    public FoodTypeDTOComplex getFoodType(Long id) {
        return dtoMapper.map(foodTypeRepository.findById(id).orElseThrow(),FoodTypeDTOComplex.class);
    }

    public List<FoodTypeDTOSimple> getFoodTypes() {
        return dtoMapper.mapList(foodTypeRepository.findAll(), FoodTypeDTOSimple.class);
    }

    @Transactional
    public FoodTypeDTOComplex createFoodTypeDto(FoodTypeDTOComplex newFoodType) {
        FoodType foodType = dtoMapper.map(newFoodType, FoodType.class);

        return dtoMapper.map(saveFoodType(foodType), FoodTypeDTOComplex.class);
    }

    @Transactional
    public FoodTypeDTOComplex createFoodType(FoodTypeDTOComplex foodType) {
        FoodType newFoodType = dtoMapper.map(foodType, FoodType.class);

        newFoodType = saveFoodType(newFoodType );

        return dtoMapper.map(newFoodType,FoodTypeDTOComplex.class);
    }

    /**
     * Updates the foodType identified by the provided id with non-null values as provided in updateFoodType
     *
     * @param id             The id of the {@code FoodType} to update
     * @param updateFoodType the {@code FoodType} containing updated values
     * @return the updated {@code FoodType}
     * @throws java.util.NoSuchElementException if the FoodType with the provided id does not exist
     */
    @Transactional
    public FoodTypeDTOComplex updateFoodType(Long id, FoodTypeDTOComplex updateFoodType) {
        FoodType foodType = foodTypeRepository.findById(id).orElseThrow();

        foodType.setName(Util.valueIfNull(updateFoodType.name(), foodType.getName()));
        foodType.setDescription(Util.valueIfNull(updateFoodType.description(), foodType.getDescription()));
        if (updateFoodType.foodCategory() != null && updateFoodType.foodCategory().id() != null) {
            foodType.setFoodCategory(foodCategoryRepository.findById(updateFoodType.foodCategory().id()).orElseThrow());
        }

        foodType = saveFoodType(foodType);

        return dtoMapper.map(foodType, FoodTypeDTOComplex.class);
    }

    public void deleteFoodType(Long id) {
        deleteFoodType(foodTypeRepository.findById(id).orElseThrow());
    }

    private void deleteFoodType(FoodType foodType) {
        foodTypeRepository.deleteById(foodType.getId());
    }

    private FoodType saveFoodType(FoodType foodType) {
        foodType = foodTypeRepository.save(foodType);
        entityManager.flush();
        entityManager.refresh(foodType);

        return foodType;
    }
}