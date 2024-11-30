package com.ageinghippy.service;

import com.ageinghippy.controller.CLIMenu;
import com.ageinghippy.model.FoodCategory;
import com.ageinghippy.util.Util;

public class FoodCategoryService {
    private GutHealthDbService gutHealthDbService;

    public FoodCategoryService(GutHealthDbService gutHealthDbService) {
        this.gutHealthDbService = gutHealthDbService;
    }

    public void createFoodCategory() {
        //get food category data
        FoodCategory foodCategory = new FoodCategory();
        foodCategory.setName(Util.getStringFromUser("Please enter the new food category name"));
        foodCategory.setDescription(Util.getStringFromUser("Please enter the new food category description"));
        //todo - validate data
        //insert into database
        foodCategory = gutHealthDbService.saveFoodCategory(foodCategory);
        //return result
        System.out.println(foodCategory);
    }

    public void updateFoodCategory() {

        //get the record that needs to be updated
        int id = Util.getIntFromUser("Please enter the food category id");
        FoodCategory foodCategory = gutHealthDbService.getFoodCategory(id);

        int choice = -1;
        String title = "=== UPDATE FOOD CATEGORY RECORD ===";
        String[] options = new String[3];
        options[0] = "to save the changes and exit";
        options[1] = "to discard all changes and exit";
        options[2] = "to change the description";

        if (foodCategory != null) {
            System.out.println("foodCategory = " + foodCategory);
            do {
                choice = CLIMenu.getChoice(title, options);

                switch (choice) {
                    case 0:
                        foodCategory = gutHealthDbService.saveFoodCategory(foodCategory);
                        System.out.println("SAVED : " + foodCategory);
                        break;
                    case 1:
                        foodCategory = gutHealthDbService.getFoodCategory(id);
                        System.out.println("CHANGES DISCARDED : " + foodCategory);
                        break;
                    case 2:
                        foodCategory.setDescription(Util.getStringFromUser("Please enter the updated food category description"));
                        System.out.println("UNSAVED : " + foodCategory);
                        break;
                }

            } while (choice < 0 || choice > 1);
        }

    }

    public void deleteFoodCategory() {
        //get the record that needs to be deleted
        int id = Util.getIntFromUser("Please enter the food category id");
        FoodCategory foodCategory = gutHealthDbService.getFoodCategory(id);
        if (foodCategory != null) {
            String title = "=== DELETE " + foodCategory + " ===";
            String[] options = new String[2];
            options[0] = "to exit without deleting";
            options[1] = "to delete the Food Category";

            int choice = CLIMenu.getChoice(title,options);
            switch (choice) {
                case 0:
                    System.out.println("DELETE ABANDONED");
                    break;
                case 1:
                    System.out.println("RECORD DELETED");
                    break;
            }
        }
        else {
            System.out.println("FoodCategory with primary key '"+id+"' not found");
        }
    }
}
