package com.ageinghippy.controller;


import com.ageinghippy.data.GutHealthDAO;
import com.ageinghippy.data.model.FoodCategory;
import com.ageinghippy.service.*;
import com.ageinghippy.util.Util;

import java.util.Scanner;

public class CLIMenu {
    private final PreparationTechniqueService preparationTechniqueService;
    private final FoodCategoryService foodCategoryService;
    private final FoodTypeService foodTypeService;
    private final FullDishService fullDishService;
    private final FullMealService fullMealService;

    public CLIMenu() {
        GutHealthDAO gutHealthDAO = new GutHealthDAO();
        preparationTechniqueService = new PreparationTechniqueService(gutHealthDAO);
        foodCategoryService = new FoodCategoryService(gutHealthDAO);
        foodTypeService = new FoodTypeService(gutHealthDAO, foodCategoryService);
        fullDishService = new FullDishService(gutHealthDAO, preparationTechniqueService, foodCategoryService, foodTypeService);
        fullMealService = new FullMealService(gutHealthDAO, preparationTechniqueService, foodCategoryService, foodTypeService);
    }

    public void showMainMenu() {
        int choice;
        String title = "=== MAIN MENU ===";
        String[] options = new String[3];
        options[0] = "to exit";
        options[1] = "to manipulate data (add/remove)";
        options[2] = "to query data (generate reports)";
        do {
            choice = getChoice(title, options);

            switch (choice) {
                case 0:
                    System.out.println("You have chosen " + options[choice]);
                    break;
                case 1:
                    System.out.println("You have chosen " + options[choice]);
                    showDataManipulationMenu();
                    break;
                case 2:
                    System.out.println("You have chosen " + options[choice]);
                    showDataViewMenu();
                    break;
                default:
                    System.out.println("You have made an invalid choice. Please try again.");
            }

        } while (choice != 0);
    }

    private void showDataManipulationMenu() {
        int choice;
        String title = "=== DATA MANIPULATION MENU ===";
        String[] options = new String[6];
        options[0] = "to exit";
        options[1] = "to manipulate preparation technique lookup data";
        options[2] = "to manipulate food category lookup data";
        options[3] = "to manipulate food type lookup data";
        options[4] = "to manipulate a dish";
        options[5] = "to manipulate a meal";

        do {
            choice = getChoice(title, options);

            switch (choice) {
                case 0: //exit
                    System.out.println("You have chosen " + options[choice]);
                    break;
                case 1: //manipulate preparation technique lookup data
                    System.out.println("You have chosen " + options[choice]);
                    preparationTechniqueDataManipulationMenu();
                    break;
                case 2: //manipulate food category lookup data
                    System.out.println("You have chosen " + options[choice]);
                    foodCategoryDataManipulationMenu();
                    break;
                case 3: //manipulate food type lookup data
                    System.out.println("You have chosen " + options[choice]);
                    foodTypeDataManipulationMenu();
                    break;
                case 4: //manipulate a dish
                    System.out.println("You have chosen " + options[choice]);
                    dishDataManipulationMenu();
                    break;
                case 5: //manipulate a meal
                    System.out.println("You have chosen " + options[choice]);
                    mealDataManipulationMenu();
                    break;
                default:
                    System.out.println("You have made an invalid choice. Please try again.");
            }

        } while (choice != 0);
    }

    private void showDataViewMenu() {
        int choice;
        String title = "=== DATA QUERY MENU ===";
        String[] options = new String[6];
        options[0] = "to exit";
        options[1] = "to view preparation technique lookup data";
        options[2] = "to view food category lookup data";
        options[3] = "to view food type lookup data";
        options[4] = "to view dish data";
        options[5] = "to view meal data";
        do {
            choice = getChoice(title, options);

            switch (choice) {
                case 0: //exit
                    System.out.println("You have chosen to exit. Thank you for coming.");
                    break;
                case 1: //view preparation technique lookup data
                    System.out.println("You have chosen " + options[choice]);
                    preparationTechniqueService.printPreparationTechniques();
                    break;
                case 2: //view food category lookup data
                    System.out.println("You have chosen " + options[choice]);
                    foodCategoryService.printFoodCategories();
                    break;
                case 3: //food type lookup data
                    System.out.println("You have chosen " + options[choice]);
                    foodTypeDataViewMenu();
                    break;
                case 4: //view dish data
                    System.out.println("You have chosen " + options[choice]);
                    dishDataViewMenu();
                    break;
                case 5:
                    System.out.println("You have chosen " + options[choice]);
                    System.err.println("Functionality not yet implemented");
                    break;
                default:
                    System.out.println("You have made an invalid choice. Please try again.");
            }

        } while (choice != 0);
    }

    private void preparationTechniqueDataManipulationMenu() {
        int choice;
        String title = "=== PREPARATION TECHNIQUE DATA MANIPULATION MENU ===";
        String[] options = new String[5];
        options[0] = "to exit";
        options[1] = "to add new preparation technique";
        options[2] = "to update an existing preparation technique";
        options[3] = "to delete an existing preparation technique";
        options[4] = "to view existing preparation techniques";

        do {
            choice = getChoice(title, options);

            switch (choice) {
                case 0: //exit
                    System.out.println("You have chosen " + options[choice]);
                    break;
                case 1: //insert
                    System.out.println("You have chosen " + options[choice]);
                    preparationTechniqueService.createPreparationTechnique();
                    break;
                case 2: //update
                    System.out.println("You have chosen " + options[choice]);
                    preparationTechniqueService.updatePreparationTechnique();
                    break;
                case 3: //delete
                    System.out.println("You have chosen " + options[choice]);
                    preparationTechniqueService.deletePreparationTechnique();
                    break;
                case 4:
                    System.out.println("You have chosen " + options[choice]);
                    preparationTechniqueService.printPreparationTechniques();
                    break;
                default:
                    System.out.println("You have made an invalid choice. Please try again.");
            }

        } while (choice != 0);
    }

    private void foodCategoryDataManipulationMenu() {
        int choice;
        String title = "=== FOOD CATEGORY DATA MANIPULATION MENU ===";
        String[] options = new String[5];
        options[0] = "to exit";
        options[1] = "to add new food category";
        options[2] = "to update an existing food category";
        options[3] = "to delete an existing food category";
        options[4] = "to view existing food categories";

        do {
            choice = getChoice(title, options);

            switch (choice) {
                case 0: //exit
                    System.out.println("You have chosen " + options[choice]);
                    break;
                case 1: //insert
                    System.out.println("You have chosen " + options[choice]);
                    foodCategoryService.createFoodCategory();
                    break;
                case 2: //update
                    System.out.println("You have chosen " + options[choice]);
                    foodCategoryService.updateFoodCategory();
                    break;
                case 3: //delete
                    System.out.println("You have chosen " + options[choice]);
                    foodCategoryService.deleteFoodCategory();
                    break;
                case 4:
                    System.out.println("You have chosen " + options[choice]);
                    foodCategoryService.printFoodCategories();
                    break;
                default:
                    System.out.println("You have made an invalid choice. Please try again.");
            }

        } while (choice != 0);
    }

    private void foodTypeDataManipulationMenu() {
        int choice;
        String title = "=== FOOD TYPE DATA MANIPULATION MENU ===";
        String[] options = new String[5];
        options[0] = "to exit";
        options[1] = "to add new food type";
        options[2] = "to update an existing food type";
        options[3] = "to delete an existing food type";
        options[4] = "to view existing food types";

        do {
            choice = getChoice(title, options);

            switch (choice) {
                case 0: //exit
                    System.out.println("You have chosen " + options[choice]);
                    break;
                case 1: //insert
                    System.out.println("You have chosen " + options[choice]);
                    foodTypeService.createFoodType();
                    break;
                case 2: //update
                    System.out.println("You have chosen " + options[choice]);
                    foodTypeService.updateFoodType();
                    break;
                case 3: //delete
                    System.out.println("You have chosen " + options[choice]);
                    foodTypeService.deleteFoodType();
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

    private void foodTypeDataViewMenu() {
        int choice;
        String title = "=== FOOD TYPE DATA VIEW MENU ===";
        String[] options = new String[5];
        options[0] = "to exit";
        options[1] = "to filter by Food Category";
        options[2] = "to filter by name (LIKE)";
        options[3] = "to filter by description (LIKE)";
        options[4] = "to view all existing food types";

        do {
            choice = getChoice(title, options);

            switch (choice) {
                case 0: //exit
                    System.out.println("You have chosen " + options[choice]);
                    break;
                case 1: //filter by Food Category
                    System.out.println("You have chosen " + options[choice]);
                    FoodCategory foodCategory = foodCategoryService.selectFoodCategory();
                    foodTypeService.printFoodTypes(String.format("WHERE food_category_id = %s", foodCategory.getId()));
                    break;
                case 2: //to filter by name (LIKE)
                    System.out.println("You have chosen " + options[choice]);
                    foodTypeService.printFoodTypes(String.format("WHERE UPPER(name) LIKE '%%%s%%'", Util.getStringFromUser("Please enter the partial name match").toUpperCase()));
                    break;
                case 3: //to filter by description (LIKE)
                    System.out.println("You have chosen " + options[choice]);
                    foodTypeService.printFoodTypes(String.format("WHERE UPPER(description) LIKE '%%%s%%'", Util.getStringFromUser("Please enter the partial description match").toUpperCase()));
                    break;
                case 4: //view all existing food types
                    System.out.println("You have chosen " + options[choice]);
                    foodTypeService.printFoodTypes("");
                    break;
                default:
                    System.out.println("You have made an invalid choice. Please try again.");
            }

        } while (choice != 0);
    }

    private void dishDataManipulationMenu() {
        int choice;
        String title = "=== DISH DATA MANIPULATION MENU ===";
        String[] options = new String[5];
        options[0] = "to exit";
        options[1] = "to add a new dish";
        options[2] = "to update an existing dish";
        options[3] = "to delete an existing dish";
        options[4] = "to view existing dishes";

        do {
            choice = getChoice(title, options);

            switch (choice) {
                case 0: //exit
                    System.out.println("You have chosen " + options[choice]);
                    break;
                case 1: //insert
                    System.out.println("You have chosen " + options[choice]);
                    fullDishService.createFullDish();
                    break;
                case 2: //update
                    System.out.println("You have chosen " + options[choice]);
                    fullDishService.updateFullDish();
                    break;
                case 3: //delete
                    System.out.println("You have chosen " + options[choice]);
                    fullDishService.deleteFullDish();
                    break;
                case 4: //view
                    System.out.println("You have chosen " + options[choice]);
                    dishDataViewMenu();
                    break;
                default:
                    System.out.println("You have made an invalid choice. Please try again.");
            }

        } while (choice != 0);
    }

    public void dishDataViewMenu() {
        int choice;
        String title = "=== DISH DATA VIEW MENU ===";
        String[] options = new String[6];
        options[0] = "to exit";
        options[1] = "to filter dishes by preparation technique";
        options[2] = "to filter dishes by name (LIKE)";
        options[3] = "to filter dishes by description (LIKE)";
        options[4] = "to view all dishes";
        options[5] = "to view full details of a particular dish";

        do {
            choice = getChoice(title, options);

            switch (choice) {
                case 0: //exit
                    System.out.println("You have chosen " + options[choice]);
                    break;
                case 1: //filter dishes by preparation technique
                    System.out.println("You have chosen " + options[choice]);
                    fullDishService.printDishes(
                            String.format("WHERE preparation_technique_code = '%s'",
                                    preparationTechniqueService.selectPreparationTechnique().getCode()));
                    break;
                case 2: //filter dishes by name (LIKE)
                    System.out.println("You have chosen " + options[choice]);
                    fullDishService.printDishes(
                            String.format("WHERE name LIKE '%%%s%%'",
                                    Util.getStringFromUser("Enter dish name partial match value")));
                    break;
                case 3: //filter dishes by description (LIKE)
                    System.out.println("You have chosen " + options[choice]);
                    fullDishService.printDishes(
                            String.format("WHERE description LIKE '%%%s%%'",
                                    Util.getStringFromUser("Enter dish description partial match value")));
                    break;
                case 4: //view all dishes
                    System.out.println("You have chosen " + options[choice]);
                    fullDishService.printDishes();
                    break;
                case 5: //view full details of a particular dish
                    System.out.println("You have chosen " + options[choice]);
                    fullDishService.printFullDish(Util.getIntFromUser("Please enter the id of the dish you wish to inspect"));
                    break;
                default:
                    System.out.println("You have made an invalid choice. Please try again.");
            }

        } while (choice != 0);
    }

    private void mealDataManipulationMenu() {
        int choice;
        String title = "=== MEAL DATA MANIPULATION MENU ===";
        String[] options = new String[5];
        options[0] = "to exit";
        options[1] = "to add a new meal";
        options[2] = "to update an existing meal";
        options[3] = "to delete an existing meal";
        options[4] = "to view existing meals";

        do {
            choice = getChoice(title, options);

            switch (choice) {
                case 0: //exit
                    System.out.println("You have chosen " + options[choice]);
                    break;
                case 1: //insert
                    System.out.println("You have chosen " + options[choice]);
                    fullMealService.createFullMeal();
                    break;
                case 2: //update
                    System.out.println("You have chosen " + options[choice]);
                    fullMealService.updateFullMeal();
                    break;
                case 3: //delete
                    System.out.println("You have chosen " + options[choice]);
                    fullMealService.deleteFullMeal();
                    break;
                case 4: //view
                    System.out.println("You have chosen " + options[choice]);
                    mealDataViewMenu();
                    break;
                default:
                    System.out.println("You have made an invalid choice. Please try again.");
            }

        } while (choice != 0);
    }

    public void mealDataViewMenu() {
        int choice;
        String title = "=== MEAL DATA VIEW MENU ===";
        String[] options = new String[4];
        options[0] = "to exit";
        options[1] = "to filter meals by date";
        options[2] = "to view all meals";
        options[3] = "to view full details of a particular meal";

        do {
            choice = getChoice(title, options);

            switch (choice) {
                case 0: //exit
                    System.out.println("You have chosen " + options[choice]);
                    break;
                case 1: //filter meals by date
                    System.out.println("You have chosen " + options[choice]);
                    fullMealService.printMeals(
                            String.format("WHERE date = '%s'",
                                    Util.getDateFromUser("Enter meal date")));
                    break;
                case 2: //view all meals
                    System.out.println("You have chosen " + options[choice]);
                    fullMealService.printMeals();
                    break;
                case 3: //view full details of a particular meal
                    System.out.println("You have chosen " + options[choice]);
                    fullMealService.printFullMeal(Util.getIntFromUser("Please enter the id of the meal you wish to inspect"));
                    break;
                default:
                    System.out.println("You have made an invalid choice. Please try again.");
            }

        } while (choice != 0);
    }
    public static int getChoice(String title, String[] options) {
        Scanner scanner = new Scanner(System.in);
        String input;
        int choice = -1;

        do {
            System.out.println("=== " + title + " ===");
            for (int i = 0; i < options.length; i++) {
                System.out.println("(" + i + ") " + options[i]);
            }
            System.out.println("   ---   ");
            System.out.print("Please make your choice : ");

            input = scanner.nextLine();

            try {
                choice = Integer.parseInt(input);
                if (choice < 0 || choice > options.length) {
                    throw new NumberFormatException("Choice out of bounds");
                }
            } catch (NumberFormatException e) {
                System.err.println("Invalid choice entered. Please try again.");
                try {
                    //we sleep long enough for the error thread to have time to write the error before we continue.
                    //(I like the red 'invalid choice' message)
                    Thread.sleep(200);
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
                choice = -1;
            }

        } while (choice < 0);
        return choice;
    }
}
