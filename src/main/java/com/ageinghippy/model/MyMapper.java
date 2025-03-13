package com.ageinghippy.model;

import com.ageinghippy.model.dto.FoodCategoryDto;
import com.ageinghippy.model.dto.FoodTypeDTO;
import com.ageinghippy.model.dto.PreparationTechniqueDTO;
import com.ageinghippy.model.entity.FoodCategory;
import com.ageinghippy.model.entity.FoodType;
import com.ageinghippy.model.entity.PreparationTechnique;
import org.modelmapper.ModelMapper;
import org.modelmapper.record.RecordModule;

import java.util.List;

public class MyMapper extends ModelMapper {

    public MyMapper() {
        super();
        this.registerModule(new RecordModule());
    }

    @Override
    public <D> D map(Object source, Class<D> destinationType) {

        if (destinationType == PreparationTechniqueDTO.class) {
            return (D) toDto((PreparationTechnique) source);
        } else if (destinationType == FoodCategoryDto.class) {
            return (D) toDto((FoodCategory) source);
        } else if (destinationType == FoodTypeDTO.class) {
            return (D) toDto((FoodType) source);
        } else {
            return super.map(source, destinationType);
        }
    }

    public <D> List<D> mapList(List<?> sources, Class<D> destinationType) {
        return sources.stream()
                .map(item -> this.map(item, destinationType))
                .toList();
    }

    private PreparationTechniqueDTO toDto(PreparationTechnique preparationTechnique) {
        return new PreparationTechniqueDTO(preparationTechnique.getCode(), preparationTechnique.getDescription());
    }

    private FoodTypeDTO toDto(FoodType foodType) {
        return new FoodTypeDTO(
                foodType.getId(),
                foodType.getFoodCategory().getId(),
                foodType.getFoodCategory().getName(),
                foodType.getName(),
                foodType.getDescription());
    }

    private FoodCategoryDto toDto(FoodCategory foodCategory) {
        return new FoodCategoryDto(
                foodCategory.getId(),
                foodCategory.getName(),
                foodCategory.getDescription(),
                mapList(foodCategory.getFoodTypes(),FoodTypeDTO.class)
        );
    }

}
