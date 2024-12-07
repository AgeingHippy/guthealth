package com.ageinghippy.controller;

import com.ageinghippy.data.model.FoodCategory;
import com.ageinghippy.data.model.FoodType;
import com.ageinghippy.service.FoodTypeService;
import com.ageinghippy.util.Util;

import java.util.ArrayList;

public class CLIFoodTypeMenu {
    private final FoodTypeService foodTypeService;
    private final CLIFoodCategoryMenu cliFoodCategoryMenu;

    public CLIFoodTypeMenu(FoodTypeService foodTypeService, CLIFoodCategoryMenu cliFoodCategoryMenu) {
        this.foodTypeService = foodTypeService;
        this.cliFoodCategoryMenu = cliFoodCategoryMenu;
    }

    public void createFoodType() {
        //get food type data
        FoodType foodType = new FoodType();
        //todo - prevent attempt to create when no food category returned - only possible if no food categories in the database
        foodType.setFoodCategoryId(cliFoodCategoryMenu.selectFoodCategoryMenuOption().getId());
        foodType.setName(Util.getStringFromUser("Please enter the new food type name"));
        foodType.setDescription(Util.getStringFromUser("Please enter the new food type description"));
        //todo - validate data
        //insert into database
        foodType = foodTypeService.saveFoodType(foodType);
        //return result
        printFoodType(foodType);
    }

    public void printFoodType(FoodType foodType) {
        System.out.println(foodTypeService.foodTypePrintString(foodType));
    }

    void foodTypeDataManipulationMenu(CLIMenu cliMenu) {
        int choice;
        String title = "=== FOOD TYPE DATA MANIPULATION MENU ===";
        String[] options = new String[5];
        options[0] = "to exit";
        options[1] = "to add new food type";
        options[2] = "to update an existing food type";
        options[3] = "to delete an existing food type";
        options[4] = "to view existing food types";

        do {
            choice = CLIMenu.getChoice(title, options);

            switch (choice) {
                case 0: //exit
                    System.out.println("You have chosen " + options[choice]);
                    break;
                case 1: //insert
                    System.out.println("You have chosen " + options[choice]);
                    createFoodType();
                    break;
                case 2: //update
                    System.out.println("You have chosen " + options[choice]);
                    updateFoodTypeMenuOption();
                    break;
                case 3: //delete
                    System.out.println("You have chosen " + options[choice]);
                    deleteFoodTypeMenuOption();
                    break;
                case 4:
                    System.out.println("You have chosen " + options[choice]);
                    foodTypeDataViewMenu();
                    break;
                default:
                    System.out.println("You have made an invalid choice. Please try again.");
            }

        } while (choice != 0);
    }

    public FoodType selectFoodTypeMenuOption() {
        return selectFoodTypeMenuOption(null);
    }

    public FoodType selectFoodTypeMenuOption(FoodCategory foodCategory) {
        String whereClause = "";
        String[] options;
        int choice;
        FoodType foodType = null;

        if (foodCategory != null) {
            whereClause = String.format("WHERE food_category_id = %d", foodCategory.getId());
        }

        ArrayList<FoodType> foodTypes = foodTypeService.getFoodTypes(whereClause);
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

    public void updateFoodTypeMenuOption() {
        //get the record that needs to be updated
        int id = Util.getIntFromUser("Please enter the food type id");
        FoodType foodType = foodTypeService.getFoodType(id);

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
                        foodType = foodTypeService.getFoodType(id);
                        System.out.println("CHANGES DISCARDED : ");
                        break;
                    case 1:
                        foodType = foodTypeService.saveFoodType(foodType);
                        System.out.println("SAVED : ");
                        break;
                    case 2:
                        foodType.setFoodCategoryId(cliFoodCategoryMenu.selectFoodCategoryMenuOption().getId());
                        break;
                    case 3:
                        foodType.setName(Util.getStringFromUser("Please enter the updated food type name"));
                        break;
                    case 4:
                        foodType.setDescription(Util.getStringFromUser("Please enter the updated food type description"));
                        System.out.println("UNSAVED : ");
                        break;
                }

            } while (choice < 0 || choice > 1);
        }
    }

    public void deleteFoodTypeMenuOption() {
        //get the record that needs to be deleted
        int id = Util.getIntFromUser("Please enter the food type id");
        FoodType foodType = foodTypeService.getFoodType(id);
        if (foodType != null) {
            String title = "=== DELETE " + foodTypeService.foodTypePrintString(foodType) + " ===";
            String[] options = new String[2];
            options[0] = "to exit without deleting";
            options[1] = "to delete the Food Category";

            int choice = CLIMenu.getChoice(title, options);
            switch (choice) {
                case 0:
                    System.out.println("DELETE ABANDONED");
                    break;
                case 1:
                    foodTypeService.deleteFoodType(foodType);
                    System.out.println("RECORD DELETED");
                    break;
            }
        } else {
            System.out.println("FoodType with primary key '" + id + "' not found");
        }
    }

    public void printFoodTypesMenuOption(String whereClause) {
        ArrayList<FoodType> foodTypes = foodTypeService.getFoodTypes(whereClause);
        System.out.println("=== " + foodTypes.size() + " FoodType records returned ===");
        foodTypes.forEach(this::printFoodType);
        System.out.println("=== ========= ===");
    }

    void foodTypeDataViewMenu() {
        int choice;
        String title = "=== FOOD TYPE DATA VIEW MENU ===";
        String[] options = new String[5];
        options[0] = "to exit";
        options[1] = "to filter by Food Category";
        options[2] = "to filter by name (LIKE)";
        options[3] = "to filter by description (LIKE)";
        options[4] = "to view all existing food types";

        do {
            choice = CLIMenu.getChoice(title, options);

            switch (choice) {
                case 0: //exit
                    System.out.println("You have chosen " + options[choice]);
                    break;
                case 1: //filter by Food Category
                    System.out.println("You have chosen " + options[choice]);
                    FoodCategory foodCategory = cliFoodCategoryMenu.selectFoodCategoryMenuOption();
                    printFoodTypesMenuOption(String.format("WHERE food_category_id = %s", foodCategory.getId()));
                    break;
                case 2: //to filter by name (LIKE)
                    System.out.println("You have chosen " + options[choice]);
                    printFoodTypesMenuOption(String.format("WHERE UPPER(name) LIKE '%%%s%%'", Util.getStringFromUser("Please enter the partial name match").toUpperCase()));
                    break;
                case 3: //to filter by description (LIKE)
                    System.out.println("You have chosen " + options[choice]);
                    printFoodTypesMenuOption(String.format("WHERE UPPER(description) LIKE '%%%s%%'", Util.getStringFromUser("Please enter the partial description match").toUpperCase()));
                    break;
                case 4: //view all existing food types
                    System.out.println("You have chosen " + options[choice]);
                    printFoodTypesMenuOption("");
                    break;
                default:
                    System.out.println("You have made an invalid choice. Please try again.");
            }

        } while (choice != 0);
    }
}
