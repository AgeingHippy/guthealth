package com.ageinghippy.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
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
public class FoodType implements Serializable {

    @Serial
    private static final long serialVersionUID = 4725431147258817560L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(optional = false)
    @JsonIgnoreProperties("foodTypes")
    private FoodCategory foodCategory;

    @NotNull
    private String name;

    @NotNull
    private String description;

    @Override
    public String toString() {
        return "FoodType{" +
                "id=" + id +
                ", foodCategory=" + foodCategory +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
