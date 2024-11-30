package com.ageinghippy.model;

public class FoodType {
    private int id;
    private int foodCategoryId;
    private String name;
    private String description;

    public FoodType() {
    }

    public FoodType(int id, int foodCategoryId, String name, String description) {
        this.id = id;
        this.foodCategoryId = foodCategoryId;
        this.name = name;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFoodCategoryId() {
        return foodCategoryId;
    }

    public void setFoodCategoryId(int foodCategoryId) {
        this.foodCategoryId = foodCategoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "FoodType{" +
                "id=" + id +
                ", foodCategory_id=" + foodCategoryId +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
