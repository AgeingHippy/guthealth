package com.ageinghippy.controller;

import com.ageinghippy.data.model.Dish;
import com.ageinghippy.data.model.DishComponent;
import com.ageinghippy.data.model.FullDish;
import com.ageinghippy.service.FullDishService;
import com.ageinghippy.util.Util;

import java.util.ArrayList;
import java.util.function.Function;

public class CLIDishMenu {
    private final FullDishService fullDishService;
    private final CLIPreparationTechniqueMenu cliPreparationTechniqueMenu;
    private final CLIFoodTypeMenu cliFoodTypeMenu;
    private final CLIFoodCategoryMenu cliFoodCategoryMenu;

    public CLIDishMenu(FullDishService fullDishService, CLIPreparationTechniqueMenu cliPreparationTechniqueMenu, CLIFoodCategoryMenu cliFoodCategoryMenu,CLIFoodTypeMenu cliFoodTypeMenu) {
        this.fullDishService = fullDishService;
        this.cliPreparationTechniqueMenu = cliPreparationTechniqueMenu;
        this.cliFoodCategoryMenu = cliFoodCategoryMenu;
        this.cliFoodTypeMenu = cliFoodTypeMenu;
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
            choice = CLIMenu.getChoice(title, options);

            switch (choice) {
                case 0: //exit
                    System.out.println("You have chosen " + options[choice]);
                    break;
                case 1: //filter dishes by preparation technique
                    System.out.println("You have chosen " + options[choice]);
                    printDishes(
                            String.format("WHERE preparation_technique_code = '%s'",
                                    cliPreparationTechniqueMenu.selectPreparationTechniqueMenuOption().getCode()));
                    break;
                case 2: //filter dishes by name (LIKE)
                    System.out.println("You have chosen " + options[choice]);
                    printDishes(
                            String.format("WHERE name LIKE '%%%s%%'",
                                    Util.getStringFromUser("Enter dish name partial match value")));
                    break;
                case 3: //filter dishes by description (LIKE)
                    System.out.println("You have chosen " + options[choice]);
                    printDishes(
                            String.format("WHERE description LIKE '%%%s%%'",
                                    Util.getStringFromUser("Enter dish description partial match value")));
                    break;
                case 4: //view all dishes
                    System.out.println("You have chosen " + options[choice]);
                    printDishes();
                    break;
                case 5: //view full details of a particular dish
                    System.out.println("You have chosen " + options[choice]);
                    printFullDish(Util.getIntFromUser("Please enter the id of the dish you wish to inspect"));
                    break;
                default:
                    System.out.println("You have made an invalid choice. Please try again.");
            }

        } while (choice != 0);
    }

    void dishDataManipulationMenu() {
        int choice;
        String title = "=== DISH DATA MANIPULATION MENU ===";
        String[] options = new String[5];
        options[0] = "to exit";
        options[1] = "to add a new dish";
        options[2] = "to update an existing dish";
        options[3] = "to delete an existing dish";
        options[4] = "to view existing dishes";

        do {
            choice = CLIMenu.getChoice(title, options);

            switch (choice) {
                case 0: //exit
                    System.out.println("You have chosen " + options[choice]);
                    break;
                case 1: //insert
                    System.out.println("You have chosen " + options[choice]);
                    createFullDishMenuOption();
                    break;
                case 2: //update
                    System.out.println("You have chosen " + options[choice]);
                    updateFullDishMenuOption();
                    break;
                case 3: //delete
                    System.out.println("You have chosen " + options[choice]);
                    deleteFullDishMenuOption();
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

    private Dish createDishMenuOption() {
        Dish dish = new Dish();
        dish.setName(Util.getStringFromUser("Please enter the new dish's name"));
        dish.setDescription(Util.getStringFromUser("Please enter the new dish's description"));
        dish.setPreparationTechniqueCode(cliPreparationTechniqueMenu.selectPreparationTechniqueMenuOption().getCode());
        return dish;
    }

    private void addDishComponentsMenuOption(FullDish fullDish) {
        String title = "=== ADD NEW DISH COMPONENT ===";
        int choice = -1;
        String[] options = new String[2];
        options[0] = "to exit";
        options[1] = "to add a new dish component";

        do {
            choice = CLIMenu.getChoice(title, options);
            switch (choice) {
                case 0:
                    System.out.println("You have chosen " + options[choice]);
                    break;
                case 1:
                    System.out.println("You have chosen " + options[choice]);
                    fullDish.addDishComponent(createDishComponentMenuOption(fullDish.getDish().getId()));
                    break;
                default:
                    System.out.println("You have made an invalid choice. Please try again.");
            }
        } while (choice != 0);
    }

    private DishComponent createDishComponentMenuOption(int dishId) {
        DishComponent dishComponent = new DishComponent();

        dishComponent.setDishId(dishId);
        dishComponent.setFoodTypeId(cliFoodTypeMenu.selectFoodTypeMenuOption(cliFoodCategoryMenu.selectFoodCategoryMenuOption()).getId());
        dishComponent.setProportion(Util.getIntFromUser("Please enter the proportion this component is of the full dish"));

        return dishComponent;
    }

    public void createFullDishMenuOption() {
        FullDish fullDish = new FullDish();

        fullDish.setDish(createDishMenuOption());

        addDishComponentsMenuOption(fullDish);

        printFullDish(fullDish);

        String title = "=== SAVE DISH AND COMPONENTS===";
        String[] options = new String[2];
        options[0] = "to exit without saving";
        options[1] = "to save changes to the dish and associated components";

        int choice = CLIMenu.getChoice(title, options);
        switch (choice) {
            case 0:
                System.out.println("SAVE ABANDONED");
                break;
            case 1:
                //save dish
                fullDishService.saveFullDish(fullDish);
                System.out.println("SAVE COMPLETED");
                break;
        }
    }

    public void updateFullDishMenuOption() {
        int id = Util.getIntFromUser("Please enter the dish id");
        FullDish fullDish = fullDishService.getFullDish(id);

        String title = "=== UPDATE DISH DETAILS ===";
        int choice;
        String[] options = new String[6];
        options[0] = "to exit without saving";
        options[1] = "to save changes and exit";
        options[2] = "to update dish details";
        options[3] = "to update existing dish component details";
        options[4] = "to delete an existing dish component";
        options[5] = "to add a new dish component";

        do {
            printFullDish(fullDish);
            choice = CLIMenu.getChoice(title, options);

            switch (choice) {
                case 0:
                    System.out.println("You have chosen " + options[choice]);
                    break;
                case 1:
                    System.out.println("You have chosen " + options[choice]);
                    fullDishService.saveFullDish(fullDish);
                    break;
                case 2:
                    System.out.println("You have chosen " + options[choice]);
                    updateDishMenuOption(fullDish.getDish());
                    break;
                case 3:
                    System.out.println("You have chosen " + options[choice]);
                    updateDishComponentsMenuOption(fullDish.getDishComponents());
                    break;
                case 4:
                    System.out.println("You have chosen " + options[choice]);
                    removeFullDishComponentsMenuOption(fullDish);
                    break;
                case 5:
                    System.out.println("You have chosen " + options[choice]);
                    fullDish.addDishComponent(createDishComponentMenuOption(fullDish.getDish().getId()));
                    break;
                default:
                    System.out.println("You have made an invalid choice. Please try again.");
                    break;
            }
        } while (choice < 0 || choice > 1);
    }

    private void updateDishMenuOption(Dish dish) {
        int choice;
        String title = "update dish";

        String[] options = new String[4];
        options[0] = "to exit";
        options[1] = "to update the name";
        options[2] = "to update the description";
        options[3] = "to update the preparation technique";

        do {
            choice = CLIMenu.getChoice(title, options);
            switch (choice) {
                case 0:
                    System.out.println("You have chosen " + options[choice]);
                    break;
                case 1:
                    dish.setName(Util.getStringFromUser("Please enter the updated dish name"));
                    break;
                case 2:
                    dish.setDescription(Util.getStringFromUser("Please enter the updated dish description"));
                    break;
                case 3:
                    dish.setPreparationTechniqueCode(cliPreparationTechniqueMenu.selectPreparationTechniqueMenuOption().getCode());
                    break;
                default:
                    System.out.println("You have made an invalid choice. Please try again.");
                    break;
            }

        } while (choice != 0);
    }

    private void updateDishComponentsMenuOption(ArrayList<DishComponent> dishComponents) {
        int choice;
        String title = "=== SELECT DISH COMPONENT TO UPDATE ===";

        String[] options = new String[dishComponents.size() + 1];
        options[0] = "to exit";
        for (int i = 0; i < dishComponents.size(); i++) {
            options[i + 1] = fullDishService.dishComponentPrintString(dishComponents.get(i));
        }

        do {
            choice = CLIMenu.getChoice(title, options);

            if (choice == 0) {
                System.out.println("You have chosen " + options[choice]);
            } else {
                updateDishComponentMenuOption(dishComponents.get(choice - 1)); //note options and dishComponents are out of sync by 1
                //update options array element to reflect changes
                options[choice] = fullDishService.dishComponentPrintString(dishComponents.get(choice - 1));
            }
        } while (choice != 0);
    }

    private void updateDishComponentMenuOption(DishComponent dishComponent) {
        int choice;
        String title = "=== UPDATE %s ===";
        String[] options = new String[3];
        options[0] = "to exit";
        options[1] = "to update the food type";
        options[2] = "to update the proportion";

        do {
            choice = CLIMenu.getChoice(String.format(title, fullDishService.dishComponentPrintString(dishComponent)), options);

            switch (choice) {
                case 0:
                    System.out.println("You have chosen " + options[choice]);
                    break;
                case 1:
                    System.out.println("You have chosen " + options[choice]);
                    dishComponent.setFoodTypeId(cliFoodTypeMenu.selectFoodTypeMenuOption(cliFoodCategoryMenu.selectFoodCategoryMenuOption()).getId());
                    break;
                case 2:
                    System.out.println("You have chosen " + options[choice]);
                    dishComponent.setProportion(Util.getIntFromUser("Please enter this component's proportion"));
                    break;
            }
        } while (choice != 0);
    }

    private void removeFullDishComponentsMenuOption(FullDish fullDish) {
        int choice;
        String[] options;
        String title = "=== Select dish component to remove from " + fullDish.getDish().getName() + " ===";

        //we need to rebuild the list each time a component is removed
        //Use a lambda for this
        Function<ArrayList<DishComponent>, String[]> buildOptionsList = (dishComponents) -> {
            String[] optionsList = new String[dishComponents.size() + 1];
            optionsList[0] = "To Exit";
            for (int i = 0; i < dishComponents.size(); i++) {
                optionsList[i + 1] = fullDishService.dishComponentPrintString(dishComponents.get(i));
            }
            return optionsList;
        };

        do {
            options = buildOptionsList.apply(fullDish.getDishComponents());
            choice = CLIMenu.getChoice(title, options);
            if (choice != 0) {
                fullDish.removeDishComponent(fullDish.getDishComponents().get(choice - 1));
            }
        } while (choice != 0);
    }

    public void deleteFullDishMenuOption() {
        int id = Util.getIntFromUser("Please enter the id of the dish you wish to delete");
        FullDish fullDish = fullDishService.getFullDish(id);

        if (fullDish != null) {
            String title = "=== DELETE " + fullDish.getDish().toString() + " ===";
            String[] options = new String[2];
            options[0] = "to exit without deleting";
            options[1] = "to delete the Dish and associated components";

            int choice = CLIMenu.getChoice(title, options);
            switch (choice) {
                case 0:
                    System.out.println("DELETE ABANDONED");
                    break;
                case 1:
                    fullDishService.deleteFullDish(fullDish);
                    System.out.println("RECORD DELETED");
                    break;
            }
        } else {
            System.out.println("Dish with primary key '" + id + "' not found");
        }
    }

    public void printDishes() {
        printDishes("");
    }

    public void printDishes(String whereClause) {
        ArrayList<Dish> dishes = fullDishService.getDishes(whereClause);
        System.out.println("=== " + dishes.size() + " DISHES FOUND ===");
        dishes.forEach(this::printDish);
    }

    public void printFullDish(int id) {
        FullDish fullDish = fullDishService.getFullDish(id);
        printFullDish(fullDish);
    }

    private void printFullDish(FullDish fullDish) {
        printDish(fullDish.getDish());
        fullDish.getDishComponents().forEach(this::printDishComponent);
    }

    private void printDish(Dish dish) {
        System.out.println(dish);
    }

    private void printDishComponent(DishComponent dishComponent) {
        System.out.println(fullDishService.dishComponentPrintString(dishComponent));
    }

}
