package com.ageinghippy.controller;

import com.ageinghippy.data.model.FullDish;
import com.ageinghippy.data.model.FullMeal;
import com.ageinghippy.data.model.Meal;
import com.ageinghippy.data.model.MealComponent;
import com.ageinghippy.service.FullDishService;
import com.ageinghippy.service.FullMealService;
import com.ageinghippy.util.Util;

import java.util.ArrayList;
import java.util.function.Function;

public class CLIMealMenu {
    private final FullMealService fullMealService;
    private final FullDishService fullDishService;
    private final CLIPreparationTechniqueMenu cliPreparationTechniqueMenu;
    private final CLIFoodTypeMenu cliFoodTypeMenu;
    private final CLIFoodCategoryMenu cliFoodCategoryMenu;

    public CLIMealMenu(FullMealService fullMealService, FullDishService fullDishService, CLIPreparationTechniqueMenu cliPreparationTechniqueMenu, CLIFoodTypeMenu cliFoodTypeMenu, CLIFoodCategoryMenu cliFoodCategoryMenu) {
        this.fullMealService = fullMealService;
        this.fullDishService = fullDishService;
        this.cliPreparationTechniqueMenu = cliPreparationTechniqueMenu;
        this.cliFoodTypeMenu = cliFoodTypeMenu;
        this.cliFoodCategoryMenu = cliFoodCategoryMenu;
    }

    public void mealDataManipulationMenu() {
        int choice;
        String title = "=== MEAL DATA MANIPULATION MENU ===";
        String[] options = new String[5];
        options[0] = "to exit";
        options[1] = "to add a new meal";
        options[2] = "to update an existing meal";
        options[3] = "to delete an existing meal";
        options[4] = "to view existing meals";

        do {
            choice = CLIMenu.getChoice(title, options);

            switch (choice) {
                case 0: //exit
                    System.out.println("You have chosen " + options[choice]);
                    break;
                case 1: //insert
                    System.out.println("You have chosen " + options[choice]);
                    createFullMealMenuOption();
                    break;
                case 2: //update
                    System.out.println("You have chosen " + options[choice]);
                    updateFullMealMenuOption();
                    break;
                case 3: //delete
                    System.out.println("You have chosen " + options[choice]);
                    deleteFullMealMenuOption();
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
            choice = CLIMenu.getChoice(title, options);

            switch (choice) {
                case 0: //exit
                    System.out.println("You have chosen " + options[choice]);
                    break;
                case 1: //filter meals by date
                    System.out.println("You have chosen " + options[choice]);
                    printMeals(
                            String.format("WHERE date = '%s'",
                                    Util.getDateFromUser("Enter meal date")));
                    break;
                case 2: //view all meals
                    System.out.println("You have chosen " + options[choice]);
                    printMeals();
                    break;
                case 3: //view full details of a particular meal
                    System.out.println("You have chosen " + options[choice]);
                    printFullMeal(Util.getIntFromUser("Please enter the id of the meal you wish to inspect"));
                    break;
                default:
                    System.out.println("You have made an invalid choice. Please try again.");
            }

        } while (choice != 0);
    }

    public void createFullMealMenuOption() {
        FullMeal fullMeal = new FullMeal();

        fullMeal.setMeal(createMealMenuOption());

        addMealComponentsMenuOption(fullMeal);

        printFullMeal(fullMeal);

        String title = "=== SAVE MEAL AND COMPONENTS===";
        String[] options = new String[2];
        options[0] = "to exit without saving";
        options[1] = "to save changes to the meal and associated components";

        int choice = CLIMenu.getChoice(title, options);
        switch (choice) {
            case 0:
                System.out.println("SAVE ABANDONED");
                break;
            case 1:
                //save dish
                fullMealService.saveFullMeal(fullMeal);
                System.out.println("SAVE COMPLETED");
                break;
        }

        fullMealService.saveFullMeal(fullMeal);
    }

    public void updateFullMealMenuOption() {
        int id = Util.getIntFromUser("Please enter the meal id");
        FullMeal fullMeal = fullMealService.getFullMeal(id);

        String title = "=== UPDATE MEAL DETAILS ===";
        int choice;
        String[] options = new String[6];
        options[0] = "to exit without saving";
        options[1] = "to save changes and exit";
        options[2] = "to update meal details";
        options[3] = "to update existing meal component details";
        options[4] = "to delete an existing meal component";
        options[5] = "to add new meal components";

        do {
            printFullMeal(fullMeal);
            choice = CLIMenu.getChoice(title, options);

            switch (choice) {
                case 0:
                    System.out.println("You have chosen " + options[choice]);
                    break;
                case 1:
                    System.out.println("You have chosen " + options[choice]);
                    fullMealService.saveFullMeal(fullMeal);
                    break;
                case 2:
                    System.out.println("You have chosen " + options[choice]);
                    updateMealMenuOption(fullMeal.getMeal());
                    break;
                case 3:
                    System.out.println("You have chosen " + options[choice]);
                    updateMealComponentsMenuOption(fullMeal.getMealComponents());
                    break;
                case 4:
                    System.out.println("You have chosen " + options[choice]);
                    removeFullMealComponentsMenuOption(fullMeal);
                    break;
                case 5:
                    System.out.println("You have chosen " + options[choice]);
                    addMealComponentsMenuOption(fullMeal);
                    break;
                default:
                    System.out.println("You have made an invalid choice. Please try again.");
                    break;
            }
        } while (choice < 0 || choice > 1);
    }

    private void updateMealMenuOption(Meal meal) {
        int choice;
        String title = "update meal";

        String[] options = new String[3];
        options[0] = "to exit";
        options[1] = "to update the meal date";
        options[2] = "to update the meal time";

        do {
            choice = CLIMenu.getChoice(title, options);
            switch (choice) {
                case 0: //exit
                    System.out.println("You have chosen " + options[choice]);
                    break;
                case 1: //update date
                    meal.setDate(Util.getDateFromUser("Please enter the meal date"));
                    break;
                case 2: //update time
                    meal.setTime(Util.getTimeFromUser("Please enter the meal time"));
                    break;
                default:
                    System.out.println("You have made an invalid choice. Please try again.");
                    break;
            }
        } while (choice != 0);
    }

    private void updateMealComponentsMenuOption(ArrayList<MealComponent> mealComponents) {
        int choice;
        String title = "=== SELECT MEAL COMPONENT TO UPDATE ===";

        String[] options = new String[mealComponents.size() + 1];
        options[0] = "to exit";
        for (int i = 0; i < mealComponents.size(); i++) {
            options[i + 1] = fullMealService.mealComponentPrintString(mealComponents.get(i));
        }

        do {
            choice = CLIMenu.getChoice(title, options);

            if (choice == 0) {
                System.out.println("You have chosen " + options[choice]);
            } else {
                updateMealComponentMenuOption(mealComponents.get(choice - 1)); //note options and mealComponents are out of sync by 1
                //update options array element to reflect changes to current meal component
                options[choice] = fullMealService.mealComponentPrintString(mealComponents.get(choice - 1));
            }
        } while (choice != 0);
    }

    private void updateMealComponentMenuOption(MealComponent mealComponent) {
        int choice;
        String title = "=== UPDATE %s ===";
        String[] options = new String[4];
        options[0] = "to exit";
        options[1] = "to update the food type";
        options[2] = "to update the preparation technique";
        options[3] = "to update the volume";

        do {
            choice = CLIMenu.getChoice(String.format(title, fullMealService.mealComponentPrintString(mealComponent)), options);

            switch (choice) {
                case 0:
                    System.out.println("You have chosen " + options[choice]);
                    break;
                case 1:
                    System.out.println("You have chosen " + options[choice]);
                    mealComponent.setFoodTypeId(cliFoodTypeMenu.selectFoodTypeMenuOption(cliFoodCategoryMenu.selectFoodCategoryMenuOption()).getId());
                    break;
                case 2:
                    System.out.println("You have chosen " + options[choice]);
                    mealComponent.setPreparationTechniqueCode(cliPreparationTechniqueMenu.selectPreparationTechniqueMenuOption().getCode());
                    break;
                case 3:
                    System.out.println("You have chosen " + options[choice]);
                    mealComponent.setVolume(Util.getIntFromUser("Please enter this component's volume"));
                    break;
            }
        } while (choice != 0);
    }

    private void removeFullMealComponentsMenuOption(FullMeal fullMeal) {
        int choice;
        String[] options;
        String title = "=== Select meal component to remove ===";

        //we need to rebuild the list each time a component is removed
        //Use a lambda for this
        Function<ArrayList<MealComponent>, String[]> buildOptionsList = (mealComponents) -> {
            String[] optionsList = new String[mealComponents.size() + 1];
            optionsList[0] = "To Exit";
            for (int i = 0; i < mealComponents.size(); i++) {
                optionsList[i + 1] = fullMealService.mealComponentPrintString(mealComponents.get(i));
            }
            return optionsList;
        };

        do {
            options = buildOptionsList.apply(fullMeal.getMealComponents());
            choice = CLIMenu.getChoice(title, options);
            if (choice != 0) {
                fullMeal.removeDishComponent(fullMeal.getMealComponents().get(choice - 1));
            }
        } while (choice != 0);
    }

    public void deleteFullMealMenuOption() {
        int id = Util.getIntFromUser("Please enter the id of the meal you wish to delete");
        FullMeal fullMeal = fullMealService.getFullMeal(id);

        if (fullMeal != null) {
            String title = "=== DELETE " + fullMeal.getMeal().toString() + " ===";
            String[] options = new String[2];
            options[0] = "to exit without deleting";
            options[1] = "to delete the Meal and associated components";

            int choice = CLIMenu.getChoice(title, options);
            switch (choice) {
                case 0:
                    System.out.println("DELETE ABANDONED");
                    break;
                case 1:
                    fullMealService.deleteFullMeal(fullMeal);
                    System.out.println("RECORD DELETED");
                    break;
            }
        } else {
            System.out.println("Meal with primary key '" + id + "' not found");
        }
    }

    private Meal createMealMenuOption() {
        Meal meal = new Meal();
        meal.setDate(Util.getDateFromUser("Please enter the meal date"));
        meal.setTime(Util.getTimeFromUser("Please enter the meal time"));
        return meal;
    }

    private void addMealComponentsMenuOption(FullMeal fullMeal) {
        String title = "=== ADD NEW MEAL COMPONENT ===";
        int choice = -1;
        String[] options = new String[3];
        options[0] = "to exit";
        options[1] = "to add a new component";
        options[2] = "to add meal components based on a dish";

        do {
            choice = CLIMenu.getChoice(title, options);
            switch (choice) {
                case 0:
                    System.out.println("You have chosen " + options[choice]);
                    break;
                case 1:
                    System.out.println("You have chosen " + options[choice]);
                    fullMeal.addMealComponent(createMealComponentMenuOption(fullMeal.getMeal().getId()));
                    break;
                case 2:
                    System.out.println("You have chosen " + options[choice]);
                    //select dish, specify volume, and then create meal components from dish components
                    FullDish fullDish = fullDishService.getFullDish(Util.getIntFromUser("Please enter the dish id"));
                    int volume = Util.getIntFromUser("Please enter the volume of this dish consumed in this meal");
                    ArrayList<MealComponent> additionalMealComponents = fullMealService.createMealComponents(fullMeal.getMeal().getId(), volume, fullDish);
                    fullMeal.addMealComponents(additionalMealComponents);
                    break;
                default:
                    System.out.println("You have made an invalid choice. Please try again.");
            }
        } while (choice != 0);
    }

    private MealComponent createMealComponentMenuOption(int mealId) {
        MealComponent mealComponent = new MealComponent();

        mealComponent.setMealId(mealId);
        mealComponent.setFoodTypeId(cliFoodTypeMenu.selectFoodTypeMenuOption(cliFoodCategoryMenu.selectFoodCategoryMenuOption()).getId());
        mealComponent.setPreparationTechniqueCode(cliPreparationTechniqueMenu.selectPreparationTechniqueMenuOption().getCode());
        mealComponent.setVolume(Util.getIntFromUser("Please enter the volume of this meal component consumed"));

        return mealComponent;
    }

    public void printMeals() {
        printMeals("");
    }

    public void printMeals(String whereClause) {
        ArrayList<Meal> meals = fullMealService.getMeals(whereClause);
        System.out.println("=== " + meals.size() + " MEALS FOUND ===");
        meals.forEach(this::printMeal);
    }

    public void printFullMeal(int id) {
        FullMeal fullMeal = fullMealService.getFullMeal(id);
        printFullMeal(fullMeal);
    }

    private void printFullMeal(FullMeal fullMeal) {
        printMeal(fullMeal.getMeal());
        fullMeal.getMealComponents().forEach(this::printMealComponent);
    }

    private void printMeal(Meal meal) {
        System.out.println(meal);
    }

    private void printMealComponent(MealComponent mealComponent) {
        System.out.println(fullMealService.mealComponentPrintString(mealComponent));
    }
}
