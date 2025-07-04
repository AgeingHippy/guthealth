package com.ageinghippy.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MealComponent implements Serializable {

    @Serial
    private static final long serialVersionUID = 3639997783415665869L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JsonIgnoreProperties("meals")
    private Meal meal;

    @ManyToOne
    private FoodType foodType;
    @ManyToOne
    private PreparationTechnique preparationTechnique;

    private int volume;


    @Override
    public String toString() {
        return "MealComponent{" +
                "id=" + id +
                ", mealId=" + meal.getId() +
                ", foodType=" + foodType +
                ", preparationTechnique='" + preparationTechnique + '\'' +
                ", volume=" + volume +
                '}';
    }
}
