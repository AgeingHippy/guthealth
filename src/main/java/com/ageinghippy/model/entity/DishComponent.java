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
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DishComponent implements Serializable {

    @Serial
    private static final long serialVersionUID = 6684213711576008332L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JsonIgnoreProperties("dishComponents")
    private Dish dish;

    @ManyToOne(optional = false)
    private FoodType foodType;

    private int proportion;

    @Override
    public String toString() {
        return "DishComponent{" +
                "id=" + id +
                ", foodType=" + foodType +
                ", proportion=" + proportion +
                '}';
    }
}
