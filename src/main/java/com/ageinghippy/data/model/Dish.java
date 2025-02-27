package com.ageinghippy.data.model;

import com.ageinghippy.model.PreparationTechnique;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.ArrayList;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Dish {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    private String name;

    @NotEmpty
    private String description;

    private PreparationTechnique preparationTechnique;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name="dish_component_id")
    private ArrayList<DishComponent> dishComponents;

    @Override
    public String toString() {
        return "Dish{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", preparationTechniqueCode='" + preparationTechnique.getCode() + '\'' +
                '}';
    }
}
