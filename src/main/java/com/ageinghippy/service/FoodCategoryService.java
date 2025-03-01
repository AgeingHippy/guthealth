package com.ageinghippy.service;

import com.ageinghippy.model.FoodCategory;
import com.ageinghippy.repository.FoodCategoryRepository;
import com.ageinghippy.util.Util;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class FoodCategoryService {
    private final FoodCategoryRepository foodCategoryRepository;

    public FoodCategory getFoodCategory(Long id) {
        return foodCategoryRepository.findById(id).orElse(null);
    }

    public ArrayList<FoodCategory> getFoodCategories() {
        return (ArrayList<FoodCategory>) foodCategoryRepository.findAll();
    }

    public FoodCategory saveFoodCategory(FoodCategory foodCategory) {
        foodCategory = foodCategoryRepository.save(foodCategory);

        return foodCategory;
    }

    /**
     * Updates the food category identified by the provided id with non-null values as provided in updateFoodCategory
     *
     * @param id The id of the {@code FoodCategory} to update
     * @param updateFoodCategory the {@code FoodCategory} containing updated values
     * @return the updated {@code FoodCategory}
     *
     * @throws java.util.NoSuchElementException if the food category with the provided id does not exist
     */
    public FoodCategory updateFoodCategory(Long id, FoodCategory updateFoodCategory) {
        FoodCategory foodCategory = foodCategoryRepository.findById(id).orElseThrow();
        foodCategory.setName(Util.valueIfNull(updateFoodCategory.getName(), foodCategory.getName()));
        foodCategory.setDescription(Util.valueIfNull(updateFoodCategory.getDescription(), foodCategory.getDescription()));
        return saveFoodCategory(foodCategory);
    }

    public void deleteFoodCategory(FoodCategory foodCategory) {
        foodCategoryRepository.deleteById(foodCategory.getId());
    }
}
