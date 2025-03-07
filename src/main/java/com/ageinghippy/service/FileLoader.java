package com.ageinghippy.service;

import com.ageinghippy.model.entity.FoodCategory;
import com.ageinghippy.model.entity.FoodType;
import lombok.RequiredArgsConstructor;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@RequiredArgsConstructor
public class FileLoader {
    private final FoodCategoryService foodCategoryService;
    private final FoodTypeService foodTypeService;

    public void loadFoodTypes(String filePath) {
        final int EXPECTED = 3;
        String line; // FoodCategoryName | foodTypeName | foodTypeDescription
        String errorMessage;
        ArrayList<FoodType> foodTypes = new ArrayList<>();
        List<FoodCategory> foodCategories = foodCategoryService.getFoodCategories();
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
                    foodTypes.add(new FoodType(null, foodCategoryByName.apply(lineComponents[0]), lineComponents[1], lineComponents[2]));
                }
            }
            foodTypes.forEach(foodTypeService::createFoodType);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }

    }
}
