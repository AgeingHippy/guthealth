package com.ageinghippy.model.dto;

import java.io.Serializable;

public record MealComponentFromDishDTO(Long dishId, Integer portionSize) implements Serializable {
}
