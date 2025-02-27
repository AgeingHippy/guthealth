package com.ageinghippy.service;


import com.ageinghippy.model.FoodCategory;
import com.ageinghippy.model.FoodType;
import com.ageinghippy.repository.FoodTypeRepository;
import com.ageinghippy.util.Util;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class FoodTypeService {

    private final FoodTypeRepository foodTypeRepository;

    public FoodType getFoodType(Long id) {
        return foodTypeRepository.findById(id).orElse(null);
    }

    public ArrayList<FoodType> getFoodTypesByFoodCategory(FoodCategory foodCategory) {
        return (ArrayList<FoodType>) foodTypeRepository.findByFoodCategory_id(foodCategory.getId());
    }

    public ArrayList<FoodType> getAllFoodTypes() {
        return (ArrayList<FoodType>) foodTypeRepository.findAll();
    }

    public ArrayList<FoodType> getAllFoodTypesMatchingName(String infix) {
        return (ArrayList<FoodType>) foodTypeRepository.findByNameContainingIgnoreCase(infix);
    }

    public ArrayList<FoodType> getAllFoodTypesMatchingDescription(String infix) {
        return (ArrayList<FoodType>) foodTypeRepository.findByDescriptionContainingIgnoreCase(infix);
    }

    public void deleteFoodType(FoodType foodType) {
        foodTypeRepository.delete(foodType);
    }

    public void saveFoodTypes(ArrayList<FoodType> foodTypes) {
        foodTypes.forEach(this::saveFoodType);
    }

    public FoodType saveFoodType(FoodType foodType) {
        return foodTypeRepository.save(foodType);
    }

    /**
     * Updates the food type identified by the provided id with non-null values as provided in updateFoodType
     *
     * @param id The id of the {@code FoodType} to update
     * @param updateFoodType the {@code FoodType} containing updated values
     * @return the updated {@code FoodType}
     *
     * @throws java.util.NoSuchElementException if the food category with the provided id does not exist
     */
    public FoodType updateFoodType(Long id, FoodType updateFoodType) {
        FoodType foodType = foodTypeRepository.findById(id).orElseThrow();
        foodType.setName(Util.valueIfNull(updateFoodType.getName(), foodType.getName()));
        foodType.setDescription(Util.valueIfNull(updateFoodType.getDescription(), foodType.getDescription()));
        foodType.setFoodCategory(Util.valueIfNull(updateFoodType.getFoodCategory(), foodType.getFoodCategory()));
        return saveFoodType(foodType);
    }


    /**
     * Build and return a string describing the given {@code FoodType} with all foreign keys replaced by descriptors
     *
     * @param foodType the {@FoodType} for which a readable string is required
     * @return a {@code String} containing readable food type details
     */
    public String foodTypePrintString(FoodType foodType) {
//        FoodCategory foodCategory = foodType.getFoodCategory();
        return "FoodType{" +
                "id=" + foodType.getId() +
                ", foodCategory='" + foodType.getFoodCategory().getName() + '\'' +
                ", name='" + foodType.getName() + '\'' +
                ", description='" + foodType.getDescription() + '\'' +
                '}';
    }

}
