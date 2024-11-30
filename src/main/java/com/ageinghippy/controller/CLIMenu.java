package com.ageinghippy.controller;


import com.ageinghippy.service.FoodCategoryService;
import com.ageinghippy.service.GutHealthDbService;

import java.util.Scanner;

public class CLIMenu {
    private GutHealthDbService gutHealthDbService;
    private FoodCategoryService foodCategoryService;

    public CLIMenu() {
        gutHealthDbService = new GutHealthDbService();
        foodCategoryService = new FoodCategoryService(gutHealthDbService);
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
        options[2] = "to query food type lookup data";
        options[3] = "to query food category lookup data";
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
                    gutHealthDbService.printFoodTypes();
                    break;
                case 3:
                    System.out.println("You have chosen "+options[choice]);
                    ;
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

    private void foodCategoryDataManipulationMenu() {
        int choice;
        String title = "=== DATA MANIPULATION MENU ===";
        String[] options = new String[5] ;
        options[0] = "to exit";
        options[1] = "to add new FoodCategory";
        options[2] = "to update an existing FoodCategory";
        options[3] = "to delete an existing FoodCategory";
        options[4] = "to view existing FoodCategories";

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
                    break;
                case 4:
                    System.out.println("You have chosen " + options[choice]);
                    gutHealthDbService.printFoodCategories();
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
