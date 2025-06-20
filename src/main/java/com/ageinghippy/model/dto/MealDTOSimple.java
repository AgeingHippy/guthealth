package com.ageinghippy.model.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

public record MealDTOSimple(Long id, String description, LocalDate date, LocalTime time) implements Serializable {
}
