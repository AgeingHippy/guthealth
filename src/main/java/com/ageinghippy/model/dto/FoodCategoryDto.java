package com.ageinghippy.model.dto;

import java.util.List;

public record FoodCategoryDto(Long id, String name, String description, List<FoodTypeDTO> foodTypes) {
}
