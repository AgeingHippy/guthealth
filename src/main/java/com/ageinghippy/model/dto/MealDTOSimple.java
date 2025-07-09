package com.ageinghippy.model.dto;

import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

public record MealDTOSimple(Long id,
                            String description,
                            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date,
                            @DateTimeFormat(pattern = "HH:mm") LocalTime time) implements Serializable {
}
