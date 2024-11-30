package com.ageinghippy.model;

public class MealComponent {
    private int id;
    private int mealId;
    private int foodTypeId;
    private String preparationTechniqueCode;
    private int volume;

    public MealComponent() {
    }

    public MealComponent(int id, int mealId, int foodTypeId, String preparationTechniqueCode, int volume) {
        this.id = id;
        this.mealId = mealId;
        this.foodTypeId = foodTypeId;
        this.preparationTechniqueCode = preparationTechniqueCode;
        this.volume = volume;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMealId() {
        return mealId;
    }

    public void setMealId(int mealId) {
        this.mealId = mealId;
    }

    public int getFoodTypeId() {
        return foodTypeId;
    }

    public void setFoodTypeId(int foodTypeId) {
        this.foodTypeId = foodTypeId;
    }

    public String getPreparationTechniqueCode() {
        return preparationTechniqueCode;
    }

    public void setPreparationTechniqueCode(String preparationTechniqueCode) {
        this.preparationTechniqueCode = preparationTechniqueCode;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    @Override
    public String toString() {
        return "MealComponent{" +
                "id=" + id +
                ", mealId=" + mealId +
                ", foodTypeId=" + foodTypeId +
                ", preparationTechniqueCode='" + preparationTechniqueCode + '\'' +
                ", volume=" + volume +
                '}';
    }
}
