package com.ageinghippy.service;

import com.ageinghippy.controller.CLIMenu;
import com.ageinghippy.data.GutHealthDAO;
import com.ageinghippy.data.model.FoodCategory;
import com.ageinghippy.data.model.FoodType;
import com.ageinghippy.util.Util;

import java.util.ArrayList;

public class FoodTypeService {
    private final GutHealthDAO gutHealthDAO;
    private final FoodCategoryService foodCategoryService;

    public FoodTypeService(GutHealthDAO gutHealthDAO, FoodCategoryService foodCategoryService) {
        this.gutHealthDAO = gutHealthDAO;
        this.foodCategoryService = foodCategoryService;
    }

    public void createFoodType() {
        //get food type data
        FoodType foodType = new FoodType();
        //todo - prevent attempt to create when no food category returned - only possible if no food categories in the database
        foodType.setFoodCategoryId(foodCategoryService.selectFoodCategory().getId());
        foodType.setName(Util.getStringFromUser("Please enter the new food type name"));
        foodType.setDescription(Util.getStringFromUser("Please enter the new food type description"));
        //todo - validate data
        //insert into database
        foodType = saveFoodType(foodType);
        //return result
        printFoodType(foodType);
    }

    public void updateFoodType() {
        //get the record that needs to be updated
        int id = Util.getIntFromUser("Please enter the food type id");
        FoodType foodType = gutHealthDAO.getFoodType(id);

        int choice = -1;
        String title = "=== UPDATE FOOD TYPE RECORD ===";
        String[] options = new String[5];
        options[0] = "to discard all changes and exit";
        options[1] = "to save the changes and exit";
        options[2] = "to change the food category";
        options[3] = "to change the name";
        options[4] = "to change the description";

        if (foodType != null) {
            System.out.println("foodType = " + foodType);
            do {
                printFoodType(foodType);

                choice = CLIMenu.getChoice(title, options);

                switch (choice) {
                    case 0:
                        foodType = gutHealthDAO.getFoodType(id);
                        System.out.println("CHANGES DISCARDED : ");
                        break;
                    case 1:
                        foodType = saveFoodType(foodType);
                        System.out.println("SAVED : ");
                        break;
                    case 2:
                        foodType.setFoodCategoryId(foodCategoryService.selectFoodCategory().getId());
                        break;
                    case 3:
                        foodType.setName(Util.getStringFromUser("Please enter the updated food type name"));
                        break;
                    case 4:
                        foodType.setDescription(Util.getStringFromUser("Please enter the updated food type description"));
                        System.out.println("UNSAVED : " );
                        break;
                }

            } while (choice < 0 || choice > 1);
        }
    }

    public void deleteFoodType() {
        //get the record that needs to be deleted
        int id = Util.getIntFromUser("Please enter the food type id");
        FoodType foodType = gutHealthDAO.getFoodType(id);
        if (foodType != null) {
            //todo - modify print of foodType in the title to have category name rather than category_id
            String title = "=== DELETE " + foodType + " ===";
            String[] options = new String[2];
            options[0] = "to exit without deleting";
            options[1] = "to delete the Food Category";

            int choice = CLIMenu.getChoice(title, options);
            switch (choice) {
                case 0:
                    System.out.println("DELETE ABANDONED");
                    break;
                case 1:
                    gutHealthDAO.deleteFoodType(foodType);
                    System.out.println("RECORD DELETED");
                    break;
            }
        } else {
            System.out.println("FoodType with primary key '" + id + "' not found");
        }
    }

    public void printFoodTypes() {
        ArrayList<FoodType> foodTypes = gutHealthDAO.getFoodTypes();
        System.out.println("=== " + foodTypes.size() + " FoodType records returned ===");
        foodTypes.forEach(this::printFoodType);
        System.out.println("=== ========= ===");
    }

    public FoodType selectFoodType() {
        String[] options;
        int choice;
        FoodType foodType = null;
        ArrayList<FoodType> foodTypes = gutHealthDAO.getFoodTypes();
        if (!foodTypes.isEmpty()) {
            //build an array containing food type items
            options = new String[foodTypes.size()];
            for (int i = 0; i < foodTypes.size(); i++) {
                options[i] = foodTypes.get(i).getName() + "( " + foodTypes.get(i).getDescription() + ")";
            }
            choice = CLIMenu.getChoice("Please select the food type", options);
            foodType = foodTypes.get(choice);
        }
        return foodType;
    }


    private FoodType saveFoodType(FoodType foodType) {
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

    public void printFoodType(FoodType foodType) {
        String foodTypeString = foodType.toString();
        FoodCategory foodCategory =gutHealthDAO.getFoodCategory(foodType.getFoodCategoryId());
        String foodCategoryString = "foodCategory = '" + foodCategory.getName() + "'";
        String pattern = "foodCategory_id=\\d+";

        foodTypeString = foodTypeString.replaceAll(pattern, foodCategoryString);
        System.out.println(foodTypeString);
    }

}
