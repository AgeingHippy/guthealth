package com.ageinghippy.controller;


import com.ageinghippy.data.GutHealthDAO;
import com.ageinghippy.service.FoodCategoryService;
import com.ageinghippy.service.PreparationTechniqueService;

import java.util.Scanner;

public class CLIMenu {
    private final FoodCategoryService foodCategoryService;
    private final PreparationTechniqueService preparationTechniqueService;

    public CLIMenu() {
        GutHealthDAO gutHealthDAO = new GutHealthDAO();
        foodCategoryService = new FoodCategoryService(gutHealthDAO);
        preparationTechniqueService = new PreparationTechniqueService(gutHealthDAO);
    }

    public void showMainMenu() {
        int choice;
        String title = "=== MAIN MENU ===";
        String[] options = new String[3] ;
        options[0] = "to exit";
        options[1] = "to manipulate data (add/remove)";
        options[2] = "to query data (generate reports)";
        do {
            choice = getChoice(title,options);

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
                    showDataQueryMenu();
                    break;
                default:
                    System.out.println("You have made an invalid choice. Please try again.");
            }

        } while (choice != 0);
    }

    private void showDataManipulationMenu() {
        int choice;
        String title = "=== DATA MANIPULATION MENU ===";
        String[] options = new String[6] ;
        options[0] = "to exit";
        options[1] = "to manipulate preparation technique lookup data";
        options[2] = "to manipulate food category lookup data";
        options[3] = "to manipulate food type lookup data";
        options[4] = "to manipulate a meal (add a new meal, modify meal components etc.";
        options[5] = "to manipulate a dish (add a new dish, modify dish components etc.";

        do {
            choice = getChoice(title, options);

            switch (choice) {
                case 0:
                    System.out.println("You have chosen " + options[choice]);
                    break;
                case 1:
                    System.out.println("You have chosen " + options[choice]);
                    preparationTechniqueDataManipulationMenu();
                    break;
                case 2:
                    System.out.println("You have chosen " + options[choice]);
                    foodCategoryDataManipulationMenu();
                    break;
                case 3:
                    System.out.println("You have chosen " + options[choice]);
                    break;
                case 4:
                    System.out.println("You have chosen " + options[choice]);
                    break;
                case 5:
                    System.out.println("You have chosen " + options[choice]);
                    break;
                default:
                    System.out.println("You have made an invalid choice. Please try again.");
            }

        } while (choice != 0);
    }

    private void showDataQueryMenu() {
        int choice;
        String title = "=== DATA QUERY MENU ===";
        String[] options = new String[6] ;
        options[0] = "to exit";
        options[1] = "to query preparation technique lookup data";
        options[2] = "to query food category lookup data";
        options[3] = "to query food type lookup data";
        options[4] = "to query meal data";
        options[5] = "to query dish data";
        do {
            choice = getChoice(title,options);

            switch (choice) {
                case 0:
                    System.out.println("You have chosen to exit. Thank you for coming.");
                    break;
                case 1:
                    System.out.println("You have chosen "+options[choice]);
                    break;
                case 2:
                    System.out.println("You have chosen "+options[choice]);
//                    foodCategoryService.printFoodTypes();
                    break;
                case 3:
                    System.out.println("You have chosen "+options[choice]);
                    break;
                case 4:
                    System.out.println("You have chosen "+options[choice]);
                    break;
                case 5:
                    System.out.println("You have chosen "+options[choice]);
                    break;
                default:
                    System.out.println("You have made an invalid choice. Please try again.");
            }

        } while (choice != 0);
    }

    private void preparationTechniqueDataManipulationMenu() {
        int choice;
        String title = "=== PREPARATION TECHNIQUE DATA MANIPULATION MENU ===";
        String[] options = new String[5] ;
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
        String[] options = new String[5] ;
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

    private void foodTypeCategoryManipulationMenu() {

    }

    public static int getChoice(String title, String[] options) {
        Scanner scanner = new Scanner(System.in);
        String input;
        int choice = -1;
        boolean choiceNotMade = true;

        do {
            System.out.println("=== " + title + " ===");
            for (int i = 0; i < options.length; i++) {
                System.out.println("(" + i +") " + options[i]);
            }
            System.out.println("   ---   ");
            System.out.print("Please make your choice : ");

            input = scanner.nextLine();

            try {
                choice =  Integer.parseInt(input);
                if (choice < 0 || choice > options.length ) {
                    throw new NumberFormatException("Invalid choice");
                }
            }
            catch (NumberFormatException e) {
                System.err.println("Invalid choice entered. Please try again.");
            }

        } while (choice < 0 );
        return choice;
    }
}
