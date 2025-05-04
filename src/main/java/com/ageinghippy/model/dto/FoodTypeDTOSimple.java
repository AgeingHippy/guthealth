package com.ageinghippy.model.dto;

import java.io.Serializable;

public record FoodTypeDTOSimple(Long id, String name, String description) implements Serializable {
}
