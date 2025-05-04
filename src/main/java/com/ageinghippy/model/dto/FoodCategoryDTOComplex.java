package com.ageinghippy.model.dto;

import java.io.Serializable;
import java.util.List;

public record FoodCategoryDTOComplex(Long id, String name, String description,
                                     List<FoodTypeDTOSimple> foodTypes) implements Serializable {
}
