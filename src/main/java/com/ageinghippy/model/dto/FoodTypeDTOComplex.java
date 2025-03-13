package com.ageinghippy.model.dto;

public record FoodTypeDTOComplex(Long id, String name, String description, FoodCategoryDTOSimple foodCategory) {
}
