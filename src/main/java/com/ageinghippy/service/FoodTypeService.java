package com.ageinghippy.service;

import com.ageinghippy.model.FoodType;
import com.ageinghippy.repository.FoodCategoryRepository;
import com.ageinghippy.repository.FoodTypeRepository;
import com.ageinghippy.util.Util;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FoodTypeService {
    private final FoodTypeRepository foodTypeRepository;
    private final FoodCategoryRepository foodCategoryRepository;

    public FoodType getFoodType(Long id) {
        return foodTypeRepository.findById(id).orElse(null);
    }

    public List<FoodType> getFoodTypes() {
        return foodTypeRepository.findAll();
    }

    public FoodType createFoodType(FoodType foodType) {
        foodType.setFoodCategory(foodCategoryRepository.findById(foodType.getFoodCategory().getId()).orElseThrow());
        return saveFoodType(foodType);
    }

    public FoodType saveFoodType(FoodType foodType) {
        foodType = foodTypeRepository.save(foodType);

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