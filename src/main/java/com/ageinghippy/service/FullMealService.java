package com.ageinghippy.service;

import com.ageinghippy.controller.CLIMenu;
import com.ageinghippy.data.GutHealthDAO;
import com.ageinghippy.data.model.*;
import com.ageinghippy.util.Util;

import java.util.ArrayList;
import java.util.function.Function;

public class FullMealService {
    private final GutHealthDAO gutHealthDAO;
    private final PreparationTechniqueService preparationTechniqueService;
    private final FoodCategoryService foodCategoryService;
    private final FoodTypeService foodTypeService;
    private final FullDishService fullDishService;

    public FullMealService(GutHealthDAO gutHealthDAO, PreparationTechniqueService preparationTechniqueService, FoodCategoryService foodCategoryService, FoodTypeService foodTypeService, FullDishService fullDishService) {
        this.gutHealthDAO = gutHealthDAO;
        this.preparationTechniqueService = preparationTechniqueService;
        this.foodCategoryService = foodCategoryService;
        this.foodTypeService = foodTypeService;
        this.fullDishService = fullDishService;
    }

    public void createFullMeal() {
        FullMeal fullMeal = new FullMeal();

        fullMeal.setMeal(createMeal());

        addMealComponents(fullMeal);

        printFullMeal(fullMeal);

        saveFullMeal(fullMeal);
    }

    //todo move to CLIMenu?
    public void updateFullMeal() {
        int id = Util.getIntFromUser("Please enter the meal id");
        FullMeal fullMeal = getFullMeal(id);

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
                    saveFullMeal(fullMeal);
                    break;
                case 2:
                    System.out.println("You have chosen " + options[choice]);
                    updateMeal(fullMeal.getMeal());
                    break;
                case 3:
                    System.out.println("You have chosen " + options[choice]);
                    updateMealComponents(fullMeal.getMealComponents());
                    break;
                case 4:
                    System.out.println("You have chosen " + options[choice]);
                    removeFullMealComponents(fullMeal);
                    break;
                case 5:
                    System.out.println("You have chosen " + options[choice]);
//                    fullMeal.addMealComponent(createMealComponent(fullMeal.getMeal().getId()));
                    addMealComponents(fullMeal);
                    break;
                default:
                    System.out.println("You have made an invalid choice. Please try again.");
                    break;
            }
        } while (choice < 0 || choice > 1);
    }

    private void updateMeal(Meal meal) {
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

    private void updateMealComponents(ArrayList<MealComponent> mealComponents) {
        int choice;
        String title = "=== SELECT MEAL COMPONENT TO UPDATE ===";

        String[] options = new String[mealComponents.size() + 1];
        options[0] = "to exit";
        for (int i = 0; i < mealComponents.size(); i++) {
            options[i + 1] = mealComponentPrintString(mealComponents.get(i));
        }

        do {
            choice = CLIMenu.getChoice(title, options);

            if (choice == 0) {
                System.out.println("You have chosen " + options[choice]);
            } else {
                updateMealComponent(mealComponents.get(choice - 1)); //note options and mealComponents are out of sync by 1
                //update options array element to reflect changes to current meal component
                options[choice] = mealComponentPrintString(mealComponents.get(choice - 1));
            }
        } while (choice != 0);
    }

    private void updateMealComponent(MealComponent mealComponent) {
        int choice;
        String title = "=== UPDATE %s ===";
        String[] options = new String[4];
        options[0] = "to exit";
        options[1] = "to update the food type";
        options[2] = "to update the preparation technique";
        options[3] = "to update the volume";

        do {
            choice = CLIMenu.getChoice(String.format(title, mealComponentPrintString(mealComponent)), options);

            switch (choice) {
                case 0:
                    System.out.println("You have chosen " + options[choice]);
                    break;
                case 1:
                    System.out.println("You have chosen " + options[choice]);
                    mealComponent.setFoodTypeId(foodTypeService.selectFoodType(foodCategoryService.selectFoodCategory()).getId());
                    break;
                case 2:
                    System.out.println("You have chosen " + options[choice]);
                    mealComponent.setPreparationTechniqueCode(preparationTechniqueService.selectPreparationTechnique().getCode());
                    break;
                case 3:
                    System.out.println("You have chosen " + options[choice]);
                    mealComponent.setVolume(Util.getIntFromUser("Please enter this component's volume"));
                    break;
            }
        } while (choice != 0);
    }

    private void removeFullMealComponents(FullMeal fullMeal) {
        int choice;
        String[] options;
        String title = "=== Select meal component to remove ===";

        //we need to rebuild the list each time a component is removed
        //Use a lambda for this
        Function<ArrayList<MealComponent>, String[]> buildOptionsList = (mealComponents) -> {
            String[] optionsList = new String[mealComponents.size() + 1];
            optionsList[0] = "To Exit";
            for (int i = 0; i < mealComponents.size(); i++) {
                optionsList[i + 1] = mealComponentPrintString(mealComponents.get(i));
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

    public FullMeal getFullMeal(int id) {
        FullMeal fullMeal = new FullMeal();
        fullMeal.setMeal(gutHealthDAO.getMeal(id));
        fullMeal.setMealComponents(gutHealthDAO.getMealComponents("WHERE meal_id = " + fullMeal.getMeal().getId()));
        return fullMeal;
    }

    public void deleteFullMeal() {
        int id = Util.getIntFromUser("Please enter the id of the meal you wish to delete");
        FullMeal fullMeal = getFullMeal(id);

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
                    fullMeal.getMealComponents().forEach(gutHealthDAO::deleteMealComponent);
                    gutHealthDAO.deleteMeal(fullMeal.getMeal());
                    System.out.println("RECORD DELETED");
                    break;
            }
        } else {
            System.out.println("Meal with primary key '" + id + "' not found");
        }
    }

    public ArrayList<FullMeal> getFullMeals() {
        ArrayList<FullMeal> fullMeals = new ArrayList<>();
        gutHealthDAO.getMeals().forEach(meal -> {
            fullMeals.add(new FullMeal(meal, gutHealthDAO.getMealComponents("WHERE meal_id = " + meal.getId())));
        });
        return fullMeals;
    }

    private Meal createMeal() {
        Meal meal = new Meal();
        meal.setDate(Util.getDateFromUser("Please enter the meal date"));
        meal.setTime(Util.getTimeFromUser("Please enter the meal time"));
        return meal;
    }

    private void addMealComponents(FullMeal fullMeal) {
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
                    fullMeal.addMealComponent(createMealComponent(fullMeal.getMeal().getId()));
                    break;
                case 2:
                    System.out.println("You have chosen " + options[choice]);
                    //select dish, specify volume, and then create meal components from dish components
                    FullDish fullDish = fullDishService.getFullDish(Util.getIntFromUser("Please enter the dish id"));
                    int volume = Util.getIntFromUser("Please enter the volume of this dish consumed in this meal");
                    ArrayList<MealComponent> additionalMealComponents = createMealComponents(fullMeal.getMeal().getId(), volume, fullDish);
                    fullMeal.addMealComponents(additionalMealComponents);
                    break;
                default:
                    System.out.println("You have made an invalid choice. Please try again.");
            }
        } while (choice != 0);
    }

    private MealComponent createMealComponent(int mealId) {
        MealComponent mealComponent = new MealComponent();

        mealComponent.setMealId(mealId);
        mealComponent.setFoodTypeId(foodTypeService.selectFoodType(foodCategoryService.selectFoodCategory()).getId());
        mealComponent.setPreparationTechniqueCode(preparationTechniqueService.selectPreparationTechnique().getCode());
        mealComponent.setVolume(Util.getIntFromUser("Please enter the volume of this meal component consumed"));

        return mealComponent;
    }

    private ArrayList<MealComponent> createMealComponents(int mealId, int volume, FullDish fullDish) {
        int baseVolume = fullDish.getDishComponents().stream().mapToInt(DishComponent::getProportion).sum();
        ArrayList<MealComponent> mealComponents = new ArrayList<>();

        fullDish.getDishComponents().forEach(dish -> {
            MealComponent mc = new MealComponent();
            mc.setMealId(mealId);
            mc.setFoodTypeId(dish.getFoodTypeId());
            mc.setPreparationTechniqueCode(fullDish.getDish().getPreparationTechniqueCode());
            mc.setVolume(volume * dish.getProportion() / baseVolume);
            mealComponents.add(mc);
        });

        return mealComponents;
    }

    private FullMeal saveFullMeal(FullMeal fullMeal) {
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
                //save meal
                fullMeal.setMeal(saveMeal(fullMeal.getMeal()));

                //save current meal components
                fullMeal.getMealComponents().forEach(mealComponent -> {
                    mealComponent.setMealId(mealComponent.getMealId() == 0 ? fullMeal.getMeal().getId() : mealComponent.getMealId());
                    mealComponent = saveMealComponent(mealComponent);
                });

                //delete removed meal components
                fullMeal.getRemovedMealComponents().forEach(this::deleteMealComponent);
                //reset removedMealComponents to
                fullMeal.getRemovedMealComponents().clear();

                System.out.println("SAVE COMPLETED");
                break;
        }
        return fullMeal;
    }

    private Meal saveMeal(Meal meal) {
        int id;
        if (meal.getId() == 0) {
            id = gutHealthDAO.insertMeal(meal);
        } else {
            gutHealthDAO.updateMeal(meal);
            id = meal.getId();
        }
        return gutHealthDAO.getMeal(id);
    }

    private MealComponent saveMealComponent(MealComponent mealComponent) {
        int id;
        if (mealComponent.getId() == 0) {
            id = gutHealthDAO.insertMealComponent(mealComponent);
        } else {
            gutHealthDAO.updateMealComponent(mealComponent);
            id = mealComponent.getId();
        }
        return gutHealthDAO.getMealComponent(id);
    }

    private void deleteMealComponent(MealComponent mealComponent) {
        if (mealComponent.getId() != 0) {
            gutHealthDAO.deleteMealComponent(mealComponent);
        }
    }

    public void printMeals() {
        printMeals("");
    }

    public void printMeals(String whereClause) {
        ArrayList<Meal> meals = gutHealthDAO.getMeals(whereClause);
        System.out.println("=== " + meals.size() + " MEALS FOUND ===");
        meals.forEach(this::printMeal);
    }

    public void printFullMealDetails() {
        int id = Util.getIntFromUser("Please enter the id of the meal you wish to inspect");
        printFullMeal(getFullMeal(id));
    }

    public void printFullDishes() {
        ArrayList<FullMeal> fullMeals = getFullMeals();
        System.out.println("=== " + fullMeals.size() + " MEALS FOUND ===");
        fullMeals.forEach(this::printFullMeal);
    }

    public void printFullMeal(int id) {
        FullMeal fullMeal = getFullMeal(id);
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
        System.out.println(mealComponentPrintString(mealComponent));
    }

    public String mealComponentPrintString(MealComponent mealComponent) {
        FoodType foodType = gutHealthDAO.getFoodType(mealComponent.getFoodTypeId());
        return "MealComponent{" + " foodType=" + foodType.getName() + ", preparationTechniqueCode='" + mealComponent.getPreparationTechniqueCode() + '\'' + ", volume=" + mealComponent.getVolume() + '}';
    }
}
