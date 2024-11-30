package com.ageinghippy.model;

public class DishComponent {
    private int id;
    private int dishId;
    private int foodTypeId;
    private int proportion;

    public DishComponent() {
    }

    public DishComponent(int id, int dishId, int foodTypeId, int proportion) {
        this.id = id;
        this.dishId = dishId;
        this.foodTypeId = foodTypeId;
        this.proportion = proportion;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDishId() {
        return dishId;
    }

    public void setDishId(int dishId) {
        this.dishId = dishId;
    }

    public int getFoodTypeId() {
        return foodTypeId;
    }

    public void setFoodTypeId(int foodTypeId) {
        this.foodTypeId = foodTypeId;
    }

    public int getProportion() {
        return proportion;
    }

    public void setProportion(int proportion) {
        this.proportion = proportion;
    }

    @Override
    public String toString() {
        return "DishComponent{" +
                "id=" + id +
                ", dishId=" + dishId +
                ", foodTypeId=" + foodTypeId +
                ", proportion=" + proportion +
                '}';
    }
}
