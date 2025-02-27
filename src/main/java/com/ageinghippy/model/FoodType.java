package com.ageinghippy.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FoodType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    private FoodCategory foodCategory;
    @NotNull
    private String name;
    @NotNull
    private String description;

    @Override
    public String toString() {
        return "FoodType{" +
                "id=" + id +
                ", foodCategory_id=" + foodCategory.getId() +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
