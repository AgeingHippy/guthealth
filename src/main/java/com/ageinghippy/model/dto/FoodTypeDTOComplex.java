package com.ageinghippy.model.dto;

public record FoodTypeDTOComplex(Long id, FoodCategoryDTOSimple foodCategory, String name, String description) {
}
