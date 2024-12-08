package com.ageinghippy.service;

import com.ageinghippy.controller.CLIMenu;
import com.ageinghippy.data.GutHealthDAO;
import com.ageinghippy.data.model.*;
import com.ageinghippy.util.Util;

import java.util.ArrayList;

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



    public FullMeal getFullMeal(int id) {
        FullMeal fullMeal = new FullMeal();
        fullMeal.setMeal(gutHealthDAO.getMeal(id));
        fullMeal.setMealComponents(gutHealthDAO.getMealComponents("WHERE meal_id = " + fullMeal.getMeal().getId()));
        return fullMeal;
    }

    public void deleteFullMeal(FullMeal fullMeal) {
        fullMeal.getMealComponents().forEach(gutHealthDAO::deleteMealComponent);
        gutHealthDAO.deleteMeal(fullMeal.getMeal());
    }

    public ArrayList<FullMeal> getFullMeals() {
        ArrayList<FullMeal> fullMeals = new ArrayList<>();
        gutHealthDAO.getMeals().forEach(meal -> {
            fullMeals.add(new FullMeal(meal, gutHealthDAO.getMealComponents("WHERE meal_id = " + meal.getId())));
        });
        return fullMeals;
    }

    public ArrayList<Meal> getMeals(String whereClause) {
        return gutHealthDAO.getMeals(whereClause);
    }

    public ArrayList<MealComponent> createMealComponents(int mealId, int volume, FullDish fullDish) {
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

    public FullMeal saveFullMeal(FullMeal fullMeal) {
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

    public String mealComponentPrintString(MealComponent mealComponent) {
        FoodType foodType = gutHealthDAO.getFoodType(mealComponent.getFoodTypeId());
        return "MealComponent{" + " foodType=" + foodType.getName() + ", preparationTechniqueCode='" + mealComponent.getPreparationTechniqueCode() + '\'' + ", volume=" + mealComponent.getVolume() + '}';
    }
}
