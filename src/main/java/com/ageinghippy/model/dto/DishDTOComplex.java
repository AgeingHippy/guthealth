package com.ageinghippy.model.dto;

import java.util.List;

public record DishDTOComplex(Long id, String name, String description, PreparationTechniqueDTO preparationTechnique, List<DishComponentDTO> dishComponents) {
}
