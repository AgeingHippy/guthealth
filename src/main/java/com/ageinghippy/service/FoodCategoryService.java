package com.ageinghippy.service;

import com.ageinghippy.controller.CLIMenu;
import com.ageinghippy.data.GutHealthDAO;
import com.ageinghippy.data.model.FoodCategory;
import com.ageinghippy.util.Util;

import java.util.ArrayList;

public class FoodCategoryService {
    private final GutHealthDAO gutHealthDAO;

    public FoodCategoryService(GutHealthDAO gutHealthDAO) {
        this.gutHealthDAO = gutHealthDAO;
    }

    public FoodCategory getFoodCategory(int id) {
        return gutHealthDAO.getFoodCategory(id);
    }

    public ArrayList<FoodCategory> getFoodCategories() {
        return gutHealthDAO.getFoodCategories();
    }

    public FoodCategory saveFoodCategory(FoodCategory foodCategory) {
        int id = 0;
        if (foodCategory.getId() == 0) {
            //insert
            id = gutHealthDAO.insertFoodCategory(foodCategory);
        } else {
            //update
            if (gutHealthDAO.updateFoodCategory(foodCategory)) {
                id = foodCategory.getId();
            }
        }
        return gutHealthDAO.getFoodCategory(id);
    }

    public void deleteFoodCategory(FoodCategory foodCategory) {
        gutHealthDAO.deleteFoodCategory(foodCategory);
    }
}
