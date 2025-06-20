package com.ageinghippy.model.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public record MealDTOComplex(Long id, String description, LocalDate date, LocalTime time, List<MealComponentDTO> mealComponents) implements Serializable {
}
