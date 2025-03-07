package com.ageinghippy.service;

import com.ageinghippy.model.MyMapper;
import com.ageinghippy.model.dto.FoodTypeDTO;
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
    private final MyMapper myMapper;
    private final EntityManager entityManager;

    public FoodType getFoodType(Long id) {
        return foodTypeRepository.findById(id).orElse(null);
    }

    public FoodTypeDTO getFoodTypeDto(Long id) {
        return myMapper.map(foodTypeRepository.findById(id).orElseThrow(),FoodTypeDTO.class);
    }

    public List<FoodType> getFoodTypes() {
        return foodTypeRepository.findAll();
    }

    @Transactional
    public FoodTypeDTO createFoodTypeDto(FoodTypeDTO newFoodType) {
        FoodType foodType = myMapper.map(newFoodType, FoodType.class);

//        foodType.setFoodCategory(foodCategoryRepository.findById(foodType.getFoodCategory().getId()).orElseThrow());

        return myMapper.map(saveFoodType(foodType),FoodTypeDTO.class);
    }

    @Transactional
    public FoodType createFoodType(FoodType foodType) {
        foodType.setFoodCategory(foodCategoryRepository.findById(foodType.getFoodCategory().getId()).orElseThrow());
        return saveFoodType(foodType);
    }

    @Transactional
    public FoodType saveFoodType(FoodType foodType) {
        foodType = foodTypeRepository.save(foodType);
        entityManager.flush();
        entityManager.refresh(foodType);

        return foodType;
    }

    /**
     * Updates the foodType identified by the provided id with non-null values as provided in updateFoodType
     *
     * @param id             The id of the {@code FoodType} to update
     * @param updateFoodType the {@code FoodType} containing updated values
     * @return the updated {@code FoodType}
     * @throws java.util.NoSuchElementException if the FoodType with the provided id does not exist
     */
    public FoodType updateFoodType(Long id, FoodType updateFoodType) {
        FoodType foodType = foodTypeRepository.findById(id).orElseThrow();

        foodType.setName(Util.valueIfNull(updateFoodType.getName(), foodType.getName()));
        foodType.setDescription(Util.valueIfNull(updateFoodType.getDescription(), foodType.getDescription()));
        if (updateFoodType.getFoodCategory() != null && updateFoodType.getFoodCategory().getId() != null) {
            foodType.setFoodCategory(foodCategoryRepository.findById(updateFoodType.getFoodCategory().getId()).orElseThrow());
        }

        return saveFoodType(foodType);
    }

    public void deleteFoodType(FoodType foodType) {
        foodTypeRepository.deleteById(foodType.getId());
    }
}