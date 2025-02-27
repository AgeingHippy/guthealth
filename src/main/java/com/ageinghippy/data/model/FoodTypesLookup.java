package com.ageinghippy.data.model;

import com.ageinghippy.model.FoodType;

import java.util.ArrayList;

public class FoodTypesLookup {
    private static ArrayList<FoodType> foodTypeList;

    private FoodTypesLookup() {
    }

    public static ArrayList<FoodType> getFoodTypes() {
        return foodTypeList;
    }

    public static void setFoodTypes(ArrayList<FoodType> foodTypes) {
        foodTypeList = foodTypes;
    }

}
