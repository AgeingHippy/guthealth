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
    private final CLIDishMenu cliDishMenu;
    private final CLIMealMenu cliMealMenu;

    private final FileLoader fileLoader;

    public CLIMenu() {
        GutHealthDAO gutHealthDAO = new GutHealthDAO();
        preparationTechniqueService = new PreparationTechniqueService(gutHealthDAO);
        foodCategoryService = new FoodCategoryService(gutHealthDAO);
        foodTypeService = new FoodTypeService(gutHealthDAO);
        fullDishService = new FullDishService(gutHealthDAO);
        fullMealService = new FullMealService(gutHealthDAO, preparationTechniqueService, foodCategoryService, foodTypeService, fullDishService);
        cliPreparationTechniqueMenu = new CLIPreparationTechniqueMenu(preparationTechniqueService);
        cliFoodCategoryMenu = new CLIFoodCategoryMenu(foodCategoryService);
        cliFoodTypeMenu = new CLIFoodTypeMenu(foodTypeService, cliFoodCategoryMenu);
        cliDishMenu = new CLIDishMenu(fullDishService, cliPreparationTechniqueMenu, cliFoodCategoryMenu, cliFoodTypeMenu);
        cliMealMenu = new CLIMealMenu(fullMealService, fullDishService, cliPreparationTechniqueMenu, cliFoodTypeMenu, cliFoodCategoryMenu);
        fileLoader = new FileLoader(foodCategoryService, foodTypeService);
    }

    public void showMainMenu() {
        int choice;
        String title = "=== MAIN MENU ===";
        String[] options = new String[4];
        options[0] = "to exit";
        options[1] = "to manipulate data (add/remove)";
        options[2] = "to query data (generate reports)";
        options[3] = "to load data from file";
        do {
            choice = CLIMenu.getChoice(title, options);

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
                case 3:
                    System.out.println("You have chosen " + options[choice]);
                    loadDataMenu();
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
                    cliPreparationTechniqueMenu.preparationTechniqueDataManipulationMenu();
                    break;
                case 2: //manipulate food category lookup data
                    System.out.println("You have chosen " + options[choice]);
                    cliFoodCategoryMenu.foodCategoryDataManipulationMenu();
                    break;
                case 3: //manipulate food type lookup data
                    System.out.println("You have chosen " + options[choice]);
                    cliFoodTypeMenu.foodTypeDataManipulationMenuOption();
                    break;
                case 4: //manipulate a dish
                    System.out.println("You have chosen " + options[choice]);
                    cliDishMenu.dishDataManipulationMenu();
                    break;
                case 5: //manipulate a meal
                    System.out.println("You have chosen " + options[choice]);
                    cliMealMenu.mealDataManipulationMenu();
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
                    cliPreparationTechniqueMenu.printPreparationTechniquesMenuOption();
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
                    cliDishMenu.dishDataViewMenu();
                    break;
                case 5:
                    System.out.println("You have chosen " + options[choice]);
                    cliMealMenu.mealDataViewMenu();
                    break;
                default:
                    System.out.println("You have made an invalid choice. Please try again.");
            }

        } while (choice != 0);
    }

    private void loadDataMenu() {
        int choice;
        String title = "=== DATA FILE LOAD MENU ===";
        String[] options = new String[2];
        options[0] = "to exit";
        options[1] = "load food types file";

        do {
            choice = getChoice(title, options);

            switch (choice) {
                case 0: //exit
                    System.out.println("You have chosen " + options[choice]);
                    break;
                case 1:
                    System.out.println("You have chosen " + options[choice]);
                    String filePath = Util.getStringFromUser("Please enter the absolute file path of the load file");
                    fileLoader.loadFoodTypes(filePath);
                    break;
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
