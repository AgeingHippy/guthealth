package com.ageinghippy.data.model;

import com.ageinghippy.model.Dish;
import com.ageinghippy.model.DishComponent;

import java.util.ArrayList;

public class FullDish {
    private Dish dish;
    private ArrayList<DishComponent> dishComponents;
    private ArrayList<DishComponent> removedDishComponents;

    public FullDish() {
        dishComponents = new ArrayList<>();
        removedDishComponents = new ArrayList<>();
    }

    public FullDish(Dish dish, ArrayList<DishComponent> dishComponents) {
        this.dish = dish;
        this.dishComponents = dishComponents;
        this.removedDishComponents = new ArrayList<>();
    }

    public Dish getDish() {
        return dish;
    }

    public void setDish(Dish dish) {
        this.dish = dish;
    }

    public ArrayList<DishComponent> getDishComponents() {
        return dishComponents;
    }

    public void setDishComponents(ArrayList<DishComponent> dishComponents) {
        this.dishComponents = dishComponents;
    }

    public void addDishComponent(DishComponent dishComponent) {
        dishComponents.add(dishComponent);
    }

    public ArrayList<DishComponent> getRemovedDishComponents() {
        return removedDishComponents;
    }

    public void setRemovedDishComponents(ArrayList<DishComponent> removedDishComponents) {
        this.removedDishComponents = removedDishComponents;
    }

    public void removeDishComponent(DishComponent dishComponent) {
        if (dishComponents.contains(dishComponent)) {
            dishComponents.remove(dishComponent);
            removedDishComponents.add(dishComponent);
        }
    }
}
