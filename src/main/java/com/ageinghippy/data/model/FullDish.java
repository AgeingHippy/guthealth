package com.ageinghippy.data.model;

import java.util.ArrayList;

public class FullDish {
    private Dish dish;
    private ArrayList<DishComponent> dishComponents;

    public FullDish() {
        dishComponents = new ArrayList<>();
    }

    public FullDish(Dish dish, ArrayList<DishComponent> dishComponents) {
        this.dish = dish;
        this.dishComponents = dishComponents;
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
}
