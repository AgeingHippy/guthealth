package com.ageinghippy.service;


import com.ageinghippy.data.GutHealthDAO;
import com.ageinghippy.data.model.FoodCategory;
import com.ageinghippy.data.model.FoodType;

import java.util.ArrayList;

public class FoodTypeService {
    private final GutHealthDAO gutHealthDAO;

    public FoodTypeService(GutHealthDAO gutHealthDAO) {
        this.gutHealthDAO = gutHealthDAO;
    }

    public FoodType getFoodType(int id) {
        return gutHealthDAO.getFoodType(id);
    }

    public  ArrayList<FoodType> getFoodTypes(String whereClause) {
        return gutHealthDAO.getFoodTypes(whereClause);
    }

    public void deleteFoodType(FoodType foodType) {
        gutHealthDAO.deleteFoodType(foodType);
    }

    public void saveFoodTypes(ArrayList<FoodType> foodTypes) {
        foodTypes.forEach(this::saveFoodType);
    }

    public FoodType saveFoodType(FoodType foodType) {
        int id = 0;
        if (foodType.getId() == 0) {
            //insert
            id = gutHealthDAO.insertFoodType(foodType);
        } else {
            //update
            if (gutHealthDAO.updateFoodType(foodType)) {
                id = foodType.getId();
            }
        }
        return gutHealthDAO.getFoodType(id);
    }


    /**
     * Build and return a string describing the given {@code FoodType} with all foreign keys replaced by descriptors
     *
     * @param foodType the {@FoodType} for which a readable string is required
     * @return a {@code String} containing readable food type details
     */
    public String foodTypePrintString(FoodType foodType) {
        FoodCategory foodCategory = gutHealthDAO.getFoodCategory(foodType.getFoodCategoryId());
        return "FoodType{" +
                "id=" + foodType.getId() +
                ", foodCategory='" + foodCategory.getName() + '\'' +
                ", name='" + foodType.getName() + '\'' +
                ", description='" + foodType.getDescription() + '\'' +
                '}';
    }

}
