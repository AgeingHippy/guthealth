package com.ageinghippy.model.dto;

import java.io.Serializable;

public record DishDTOSimple(Long id, String name, String description,
                            PreparationTechniqueDTO preparationTechnique) implements Serializable {
}
