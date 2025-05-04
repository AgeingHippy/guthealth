package com.ageinghippy.model.dto;

import java.io.Serializable;

public record FoodTypeDTOComplex(Long id, FoodCategoryDTOSimple foodCategory, String name,
                                 String description) implements Serializable {
}
