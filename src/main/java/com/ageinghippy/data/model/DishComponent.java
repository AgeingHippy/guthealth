package com.ageinghippy.data.model;

import com.ageinghippy.model.FoodType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DishComponent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    private int dishId;
    private FoodType foodType;
    private int proportion;



    @Override
    public String toString() {
        return "DishComponent{" +
                "id=" + id +
//                ", dishId=" + dishId +
                ", foodType=" + foodType +
                ", proportion=" + proportion +
                '}';
    }
}
