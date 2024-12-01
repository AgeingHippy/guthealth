package com.ageinghippy.data.model;

public class Dish {
    private int id;
    private String name;
    private String description;
    private String preparationTechniqueCode;

    public Dish() {
    }

    public Dish(int id, String name, String description, String preparationTechniqueCode) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.preparationTechniqueCode = preparationTechniqueCode;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getPreparationTechniqueCode() {
        return preparationTechniqueCode;
    }

    public void setPreparationTechniqueCode(String preparationTechniqueCode) {
        this.preparationTechniqueCode = preparationTechniqueCode;
    }

    @Override
    public String toString() {
        return "Dish{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", preparationTechniqueCode='" + preparationTechniqueCode + '\'' +
                '}';
    }
}
