package com.ageinghippy.service;

import com.ageinghippy.model.FoodCategory;
import com.ageinghippy.model.FoodType;

import java.io.*;
import java.util.ArrayList;
import java.util.function.Function;

public class FileLoader {
    private final FoodCategoryService foodCategoryService;
    private final FoodTypeService foodTypeService;

    public FileLoader(FoodCategoryService foodCategoryService, FoodTypeService foodTypeService) {
        this.foodCategoryService = foodCategoryService;
        this.foodTypeService = foodTypeService;
    }

    public void loadFoodTypes(String filePath) {
        final int EXPECTED = 3;
        String line; // FoodCategoryName | foodTypeName | foodTypeDescription
        String errorMessage;
        ArrayList<FoodType> foodTypes = new ArrayList<>();
        ArrayList<FoodCategory> foodCategories = foodCategoryService.getFoodCategories();
        Function<String, FoodCategory> foodCategoryByName = foodCategoryName ->
                foodCategories.stream().filter(fc -> fc.getName().equalsIgnoreCase(foodCategoryName)).findFirst().orElse(null);

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath));
             BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(filePath + ".err"))) {

            while ((line = bufferedReader.readLine()) != null) {
                String[] lineComponents = line.split("\\|");
                errorMessage = "";
                if (lineComponents.length != 3) {
                    errorMessage = "Incorrect number of components in record. Expected " + EXPECTED + " found " + lineComponents.length;
                } else if (lineComponents[0] == null || lineComponents[1] == null || lineComponents[2] == null) {
                    errorMessage = "All 3 of FoodCategoryName | foodTypeName | foodTypeDescription are required";
                } else if (foodCategoryByName.apply(lineComponents[0]) == null) {
                    errorMessage = "FoodCategory " + lineComponents[0] + " not found.";
                }

                if (!errorMessage.isEmpty()) {
                    bufferedWriter.write(line);
                    bufferedWriter.write(" : ERR : " + errorMessage);
                    bufferedWriter.newLine();
                } else {
                    //data passes the basic validation so create new food type
                    foodTypes.add(new FoodType(0, foodCategoryByName.apply(lineComponents[0]), lineComponents[1], lineComponents[2]));
                }
            }
            foodTypeService.saveFoodTypes(foodTypes);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }

    }
}
