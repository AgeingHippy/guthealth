package com.ageinghippy.model.dto;

import java.io.Serializable;

public record DishComponentDTO(Long id, FoodTypeDTOSimple foodType, Integer proportion) implements Serializable {
}
