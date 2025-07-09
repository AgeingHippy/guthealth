package com.ageinghippy.model.dto;

import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public record MealDTOComplex(Long id,
                             String description,
                             @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date,
                             @DateTimeFormat(pattern = "HH:mm") LocalTime time,
                             List<MealComponentDTO> mealComponents) implements Serializable {
}
