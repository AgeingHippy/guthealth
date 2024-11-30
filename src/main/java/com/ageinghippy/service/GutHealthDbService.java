package com.ageinghippy.service;

import com.ageinghippy.data.GutHealthDAO;
import com.ageinghippy.model.FoodCategory;
import com.ageinghippy.model.FoodType;

import java.util.ArrayList;

public class GutHealthDbService {
    private GutHealthDAO gutHealthDAO;

    public GutHealthDbService() {
        this.gutHealthDAO = new GutHealthDAO();
    }

    public void printFoodCategories() {
        ArrayList<FoodCategory> foodCategories = gutHealthDAO.getFoodCategories();
        System.out.println("=== FoodCategories === (" + foodCategories.size() + " records returned)");
        foodCategories.forEach(System.out::println);
        System.out.println("=== ========= ===");
    }


    public void printFoodTypes() {
        ArrayList<FoodType> foodTypes = gutHealthDAO.getFoodTypes();
        System.out.println("=== FoodTypes === (" + foodTypes.size() + " records returned)");
        foodTypes.forEach(System.out::println);
        System.out.println("=== ========= ===");
    }

    public FoodCategory saveFoodCategory(FoodCategory foodCategory) {
        int id = 0;
        if (foodCategory.getId() == 0) {
            //insert
            id = gutHealthDAO.insertFoodCategory(foodCategory);
        }
        else {
            //update
            if (gutHealthDAO.updateFoodCategory(foodCategory)) {
                id = foodCategory.getId();
            }

        }
        return gutHealthDAO.getFoodCategory(id);
    }

    public FoodCategory getFoodCategory(int id) {
        return gutHealthDAO.getFoodCategory(id);
    }




}
