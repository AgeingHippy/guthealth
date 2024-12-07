package com.ageinghippy.controller;

import com.ageinghippy.data.model.FoodCategory;
import com.ageinghippy.service.FoodCategoryService;
import com.ageinghippy.util.Util;

import java.util.ArrayList;

public class CLIFoodCategoryMenu {
    private final FoodCategoryService foodCategoryService;

    public CLIFoodCategoryMenu(FoodCategoryService foodCategoryService) {
        this.foodCategoryService = foodCategoryService;
    }

    void foodCategoryDataManipulationMenu(CLIMenu cliMenu) {
        int choice;
        String title = "=== FOOD CATEGORY DATA MANIPULATION MENU ===";
        String[] options = new String[5];
        options[0] = "to exit";
        options[1] = "to add new food category";
        options[2] = "to update an existing food category";
        options[3] = "to delete an existing food category";
        options[4] = "to view existing food categories";

        do {
            choice = CLIMenu.getChoice(title, options);

            switch (choice) {
                case 0: //exit
                    System.out.println("You have chosen " + options[choice]);
                    break;
                case 1: //insert
                    System.out.println("You have chosen " + options[choice]);
                    createFoodCategoryMenuOption();
                    break;
                case 2: //update
                    System.out.println("You have chosen " + options[choice]);
                    updateFoodCategoryMenuOption();
                    break;
                case 3: //delete
                    System.out.println("You have chosen " + options[choice]);
                    deleteFoodCategoryMenuOption();
                    break;
                case 4:
                    System.out.println("You have chosen " + options[choice]);
                    printFoodCategoriesMenuOption();
                    break;
                default:
                    System.out.println("You have made an invalid choice. Please try again.");
            }

        } while (choice != 0);
    }

    public void createFoodCategoryMenuOption() {
        //get food category data
        FoodCategory foodCategory = new FoodCategory();
        foodCategory.setName(Util.getStringFromUser("Please enter the new food category name"));
        foodCategory.setDescription(Util.getStringFromUser("Please enter the new food category description"));
        //todo - validate data
        //insert into database
        foodCategory = foodCategoryService.saveFoodCategory(foodCategory);
        //return result
        System.out.println(foodCategory);
    }

    public void updateFoodCategoryMenuOption() {
        //get the record that needs to be updated
        int id = Util.getIntFromUser("Please enter the food category id");
        FoodCategory foodCategory = foodCategoryService.getFoodCategory(id);

        int choice = -1;
        String title = "=== UPDATE FOOD CATEGORY RECORD ===";
        String[] options = new String[3];
        options[0] = "to discard all changes and exit";
        options[1] = "to save the changes and exit";
        options[2] = "to change the description";

        if (foodCategory != null) {
            System.out.println("foodCategory = " + foodCategory);
            do {
                choice = CLIMenu.getChoice(title, options);

                switch (choice) {
                    case 0:
                        foodCategory = foodCategoryService.getFoodCategory(id);
                        System.out.println("CHANGES DISCARDED : " + foodCategory);
                        break;
                    case 1:
                        foodCategory = foodCategoryService.saveFoodCategory(foodCategory);
                        System.out.println("SAVED : " + foodCategory);
                        break;
                    case 2:
                        foodCategory.setDescription(Util.getStringFromUser("Please enter the updated food category description"));
                        System.out.println("UNSAVED : " + foodCategory);
                        break;
                }

            } while (choice < 0 || choice > 1);
        }
    }

    public void deleteFoodCategoryMenuOption() {
        //get the record that needs to be deleted
        int id = Util.getIntFromUser("Please enter the food category id");
        FoodCategory foodCategory = foodCategoryService.getFoodCategory(id);
        if (foodCategory != null) {
            String title = "=== DELETE " + foodCategory + " ===";
            String[] options = new String[2];
            options[0] = "to exit without deleting";
            options[1] = "to delete the Food Category";

            int choice = CLIMenu.getChoice(title, options);
            switch (choice) {
                case 0:
                    System.out.println("DELETE ABANDONED");
                    break;
                case 1:
                    foodCategoryService.deleteFoodCategory(foodCategory);
                    System.out.println("RECORD DELETED");
                    break;
            }
        } else {
            System.out.println("FoodCategory with primary key '" + id + "' not found");
        }
    }

    public FoodCategory selectFoodCategoryMenuOption() {
        String[] options;
        int choice;
        FoodCategory foodCategory = null;
        ArrayList<FoodCategory> foodCategories = foodCategoryService.getFoodCategories();
        if (!foodCategories.isEmpty()) {
            //build an array containing food category items
            options = new String[foodCategories.size()];
            for (int i = 0; i < foodCategories.size(); i++) {
                options[i] = foodCategories.get(i).getName() + "( " + foodCategories.get(i).getDescription() + ")";
            }
            choice = CLIMenu.getChoice("Please select the food category", options);
            foodCategory = foodCategories.get(choice);
        }
        return foodCategory;
    }

    public void printFoodCategoriesMenuOption() {
        ArrayList<FoodCategory> foodCategories = foodCategoryService.getFoodCategories();
        System.out.println("=== " + foodCategories.size() + " FoodCategory records returned ===");
        foodCategories.forEach(System.out::println);
        System.out.println("=== ========= ===");
    }
}
