package com.ageinghippy.data.model;

import com.ageinghippy.model.Meal;
import com.ageinghippy.model.MealComponent;

import java.util.ArrayList;
import java.util.Collection;

public class FullMeal {
    private Meal meal;
    private ArrayList<MealComponent> mealComponents;
    private ArrayList<MealComponent> removedMealComponents;

    public FullMeal() {
        mealComponents = new ArrayList<MealComponent>();
        removedMealComponents = new ArrayList<MealComponent>();
    }

    public FullMeal(Meal dish, ArrayList<MealComponent> dishComponents) {
        this.meal = dish;
        this.mealComponents = dishComponents;
        this.removedMealComponents = new ArrayList<MealComponent>();
    }

    public Meal getMeal() {
        return meal;
    }

    public void setMeal(Meal meal) {
        this.meal = meal;
    }

    public ArrayList<MealComponent> getMealComponents() {
        return mealComponents;
    }

    public void setMealComponents(ArrayList<MealComponent> mealComponents) {
        this.mealComponents = mealComponents;
    }

    public void addMealComponent(MealComponent mealComponent) {
        mealComponents.add(mealComponent);
    }

    public void addMealComponents(Collection<MealComponent> mealComponents) {
        this.mealComponents.addAll(mealComponents);
    }

    public ArrayList<MealComponent> getRemovedMealComponents() {
        return removedMealComponents;
    }

    public void setRemovedMealComponents(ArrayList<MealComponent> removedMealComponents) {
        this.removedMealComponents = removedMealComponents;
    }

    public void removeDishComponent(MealComponent dishComponent) {
        if (mealComponents.contains(dishComponent)) {
            mealComponents.remove(dishComponent);
            removedMealComponents.add(dishComponent);
        }
    }
}
