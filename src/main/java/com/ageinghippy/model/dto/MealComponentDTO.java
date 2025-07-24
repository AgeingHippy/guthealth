package com.ageinghippy.model.dto;

import java.io.Serializable;

public record MealComponentDTO(Long id, FoodTypeDTOSimple foodType, PreparationTechniqueDTO preparationTechnique, Integer volume) implements Serializable {
}
