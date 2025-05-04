package com.ageinghippy.model.dto;

import java.io.Serializable;

public record FoodCategoryDTOSimple(Long id, String name, String description) implements Serializable {
}
