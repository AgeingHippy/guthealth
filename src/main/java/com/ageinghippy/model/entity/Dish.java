package com.ageinghippy.model.entity;

import com.ageinghippy.validation.DishComponentFoodTypeBelongs;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@DishComponentFoodTypeBelongs
public class Dish implements Serializable {

    @Serial
    private static final long serialVersionUID = 7214811498965048416L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    private String name;

    @NotEmpty
    private String description;

    @ManyToOne(optional = false)
    @JoinColumn(name = "preparation_technique_id")
    private PreparationTechnique preparationTechnique;

    @OneToMany(mappedBy = "dish", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("dish")
    private List<DishComponent> dishComponents;

    @ManyToOne(optional = false)
    @JoinColumn
    @JsonIgnore
    private UserPrinciple principle;

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
