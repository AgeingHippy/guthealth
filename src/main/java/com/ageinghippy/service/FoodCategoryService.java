package com.ageinghippy.service;

import com.ageinghippy.model.DTOMapper;
import com.ageinghippy.model.dto.FoodCategoryDTOComplex;
import com.ageinghippy.model.entity.FoodCategory;
import com.ageinghippy.repository.FoodCategoryRepository;
import com.ageinghippy.util.Util;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FoodCategoryService {
    private final FoodCategoryRepository foodCategoryRepository;
    private final DTOMapper DTOMapper;
    private final EntityManager entityManager;

    public FoodCategory getFoodCategory(Long id) {
        return foodCategoryRepository.findById(id).orElseThrow();
    }

    public List<FoodCategory> getFoodCategories() {
        return foodCategoryRepository.findAll();
    }

    @Transactional
    public FoodCategory createFoodCategory(FoodCategory foodCategory) {

        foodCategory = saveFoodCategory(foodCategory);

        return foodCategory;
    }

    @Transactional
    public FoodCategory saveFoodCategory(FoodCategory foodCategory) {
        foodCategory = foodCategoryRepository.save(foodCategory);
        entityManager.flush();
        entityManager.refresh(foodCategory);
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
    @Transactional
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
