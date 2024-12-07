package com.ageinghippy.controller;


import com.ageinghippy.data.GutHealthDAO;
import com.ageinghippy.service.*;
import com.ageinghippy.util.Util;

import java.util.Scanner;

public class CLIMenu {
    private final PreparationTechniqueService preparationTechniqueService;
    private final FoodCategoryService foodCategoryService;
    private final FoodTypeService foodTypeService;
    private final FullDishService fullDishService;
    private final FullMealService fullMealService;

    private final CLIFoodCategoryMenu cliFoodCategoryMenu;
    private final CLIFoodTypeMenu cliFoodTypeMenu;
    private final CLIPreparationTechniqueMenu cliPreparationTechniqueMenu;

    public CLIMenu() {
        GutHealthDAO gutHealthDAO = new GutHealthDAO();
        preparationTechniqueService = new PreparationTechniqueService(gutHealthDAO);
        foodCategoryService = new FoodCategoryService(gutHealthDAO);
        foodTypeService = new FoodTypeService(gutHealthDAO);
        fullDishService = new FullDishService(gutHealthDAO, preparationTechniqueService, foodCategoryService, foodTypeService);
        fullMealService = new FullMealService(gutHealthDAO, preparationTechniqueService, foodCategoryService, foodTypeService, fullDishService);
        cliPreparationTechniqueMenu = new CLIPreparationTechniqueMenu(preparationTechniqueService);
        cliFoodCategoryMenu = new CLIFoodCategoryMenu(foodCategoryService);
        cliFoodTypeMenu = new CLIFoodTypeMenu(foodTypeService,cliFoodCategoryMenu);
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
                    cliFoodCategoryMenu.foodCategoryDataManipulationMenu(this);
                    break;
                case 3: //manipulate food type lookup data
                    System.out.println("You have chosen " + options[choice]);
                    cliFoodTypeMenu.foodTypeDataManipulationMenu(this);
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
                    preparationTechniqueService.printPreparationTechniquesMenuOption();
                    break;
                case 2: //view food category lookup data
                    System.out.println("You have chosen " + options[choice]);
                    cliFoodCategoryMenu.printFoodCategoriesMenuOption();
                    break;
                case 3: //food type lookup data
                    System.out.println("You have chosen " + options[choice]);
                    cliFoodTypeMenu.foodTypeDataViewMenu();
                    break;
                case 4: //view dish data
                    System.out.println("You have chosen " + options[choice]);
                    dishDataViewMenu();
                    break;
                case 5:
                    System.out.println("You have chosen " + options[choice]);
                    mealDataViewMenu();
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
                    preparationTechniqueService.createPreparationTechniqueMenuOption();
                    break;
                case 2: //update
                    System.out.println("You have chosen " + options[choice]);
                    preparationTechniqueService.updatePreparationTechniqueMenuOption();
                    break;
                case 3: //delete
                    System.out.println("You have chosen " + options[choice]);
                    preparationTechniqueService.deletePreparationTechniqueMenuOption();
                    break;
                case 4:
                    System.out.println("You have chosen " + options[choice]);
                    preparationTechniqueService.printPreparationTechniquesMenuOption();
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
                    fullDishService.createFullDishMenuOption();
                    break;
                case 2: //update
                    System.out.println("You have chosen " + options[choice]);
                    fullDishService.updateFullDishMenuOption();
                    break;
                case 3: //delete
                    System.out.println("You have chosen " + options[choice]);
                    fullDishService.deleteFullDishMenuOption();
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
                                    preparationTechniqueService.selectPreparationTechniqueMenuOption().getCode()));
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
