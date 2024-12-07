package com.ageinghippy.service;

import com.ageinghippy.controller.CLIMenu;
import com.ageinghippy.data.GutHealthDAO;
import com.ageinghippy.data.model.*;
import com.ageinghippy.util.Util;

import java.util.ArrayList;
import java.util.function.Function;

/**
 * Manage all that is related to dishes including dish components
 */
public class FullDishService {
    private final GutHealthDAO gutHealthDAO;
    private final PreparationTechniqueService preparationTechniqueService;
    private final FoodCategoryService foodCategoryService;
    private final FoodTypeService foodTypeService;

    public FullDishService(GutHealthDAO gutHealthDAO, PreparationTechniqueService preparationTechniqueService, FoodCategoryService foodCategoryService, FoodTypeService foodTypeService) {
        this.gutHealthDAO = gutHealthDAO;
        this.preparationTechniqueService = preparationTechniqueService;
        this.foodCategoryService = foodCategoryService;
        this.foodTypeService = foodTypeService;
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
                saveFullDish(fullDish);
                System.out.println("SAVE COMPLETED");
                break;
        }
    }

    //todo move to CLIMenu
    public void updateFullDishMenuOption() {
        int id = Util.getIntFromUser("Please enter the dish id");
        FullDish fullDish = getFullDish(id);

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
                    saveFullDish(fullDish);
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
                    dish.setPreparationTechniqueCode(preparationTechniqueService.selectPreparationTechniqueMenuOption().getCode());
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
            options[i + 1] = dishComponentPrintString(dishComponents.get(i));
        }

        do {
            choice = CLIMenu.getChoice(title, options);

            if (choice == 0) {
                System.out.println("You have chosen " + options[choice]);
            } else {
                updateDishComponentMenuOption(dishComponents.get(choice - 1)); //note options and dishComponents are out of sync by 1
                //update options array element to reflect changes
                options[choice] = dishComponentPrintString(dishComponents.get(choice - 1));
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
            choice = CLIMenu.getChoice(String.format(title, dishComponentPrintString(dishComponent)), options);

            switch (choice) {
                case 0:
                    System.out.println("You have chosen " + options[choice]);
                    break;
                case 1:
                    System.out.println("You have chosen " + options[choice]);
                    dishComponent.setFoodTypeId(foodTypeService.selectFoodTypeMenuOption(foodCategoryService.selectFoodCategory()).getId());
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
                optionsList[i + 1] = dishComponentPrintString(dishComponents.get(i));
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
        FullDish fullDish = getFullDish(id);

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
                    fullDish.getDishComponents().forEach(gutHealthDAO::deleteDishComponent);
                    gutHealthDAO.deleteDish(fullDish.getDish());
                    System.out.println("RECORD DELETED");
                    break;
            }
        } else {
            System.out.println("Dish with primary key '" + id + "' not found");
        }
    }

    public FullDish getFullDish(int id) {
        FullDish fullDish = new FullDish();
        fullDish.setDish(gutHealthDAO.getDish(id));
        fullDish.setDishComponents(gutHealthDAO.getDishComponents("WHERE dish_id = " + fullDish.getDish().getId()));
        return fullDish;
    }

    public ArrayList<FullDish> getFullDishes() {
        ArrayList<FullDish> fullDishes = new ArrayList<>();
        gutHealthDAO.getDishes().forEach(dish -> {
            fullDishes.add(new FullDish(dish, gutHealthDAO.getDishComponents("WHERE dish_id = " + dish.getId())));
        });
        return fullDishes;
    }

    private Dish createDishMenuOption() {
        Dish dish = new Dish();
        dish.setName(Util.getStringFromUser("Please enter the new dish's name"));
        dish.setDescription(Util.getStringFromUser("Please enter the new dish's description"));
        dish.setPreparationTechniqueCode(preparationTechniqueService.selectPreparationTechniqueMenuOption().getCode());
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
        dishComponent.setFoodTypeId(foodTypeService.selectFoodTypeMenuOption(foodCategoryService.selectFoodCategory()).getId());
        dishComponent.setProportion(Util.getIntFromUser("Please enter the proportion this component is of the full dish"));

        return dishComponent;
    }

    private FullDish saveFullDish(FullDish fullDish) {

        //save dish
        fullDish.setDish(saveDish(fullDish.getDish()));

        //save current dish components
        fullDish.getDishComponents().forEach(dishComponent -> {
            dishComponent.setDishId(
                    dishComponent.getDishId() == 0 ? fullDish.getDish().getId() : dishComponent.getDishId());
            dishComponent = saveDishComponent(dishComponent);
        });

        //delete removed dish components
        fullDish.getRemovedDishComponents().forEach(this::deleteDishComponent);
        //reset deletedDishComponents
        fullDish.getRemovedDishComponents().clear();

        return fullDish;
    }

    private Dish saveDish(Dish dish) {
        int id;
        if (dish.getId() == 0) {
            id = gutHealthDAO.insertDish(dish);
        } else {
            gutHealthDAO.updateDish(dish);
            id = dish.getId();
        }
        return gutHealthDAO.getDish(id);
    }

    private DishComponent saveDishComponent(DishComponent dishComponent) {
        int id;
        if (dishComponent.getId() == 0) {
            id = gutHealthDAO.insertDishComponent(dishComponent);
        } else {
            gutHealthDAO.updateDishComponent(dishComponent);
            id = dishComponent.getId();
        }
        return gutHealthDAO.getDishComponent(id);
    }

    private void deleteDishComponent(DishComponent dishComponent) {
        if (dishComponent.getId() != 0) {
            gutHealthDAO.deleteDishComponent(dishComponent);
        }
    }

    public void printDishes() {
        printDishes("");
    }

    public void printDishes(String whereClause) {
        ArrayList<Dish> dishes = gutHealthDAO.getDishes(whereClause);
        System.out.println("=== " + dishes.size() + " DISHES FOUND ===");
        dishes.forEach(this::printDish);
    }

    public void printFullDishDetails() {
        int id = Util.getIntFromUser("Please enter the id of the dish you wish to inspect");
        printFullDish(getFullDish(id));
    }

    public void printFullDishes() {
        ArrayList<FullDish> fullDishes = getFullDishes();
        System.out.println("=== " + fullDishes.size() + " DISHES FOUND ===");
        fullDishes.forEach(this::printFullDish);
    }

    public void printFullDish(int id) {
        FullDish fullDish = getFullDish(id);
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
        System.out.println(dishComponentPrintString(dishComponent));
    }

    public String dishComponentPrintString(DishComponent dishComponent) {
        FoodType foodType = gutHealthDAO.getFoodType(dishComponent.getFoodTypeId());
        return "DishComponent{" +
                "id=" + dishComponent.getId() +
                ", foodType=" + foodType.getName() + '\'' +
                ", proportion=" + dishComponent.getProportion() +
                '}';
    }

}
