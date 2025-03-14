package com.ageinghippy.model;

import com.ageinghippy.model.dto.*;
import com.ageinghippy.model.entity.FoodCategory;
import com.ageinghippy.model.entity.FoodType;
import com.ageinghippy.model.entity.PreparationTechnique;
import org.modelmapper.ModelMapper;
import org.modelmapper.record.RecordModule;

import java.util.List;

public class DTOMapper extends ModelMapper {

    public DTOMapper() {
        super();
        this.registerModule(new RecordModule());
    }

    @Override
    public <D> D map(Object source, Class<D> destinationType) {

        if (destinationType == PreparationTechniqueDTO.class) {
            return (D) toDto((PreparationTechnique) source);
        } else if (destinationType == FoodCategoryDTOSimple.class) {
            return (D) toDtoSimple((FoodCategory) source);
        } else if (destinationType == FoodCategoryDTOComplex.class) {
            return (D) toDtoComplex((FoodCategory) source);
        } else if (destinationType == FoodTypeDTOSimple.class) {
            return (D) toDtoSimple((FoodType) source);
        } else if (destinationType == FoodTypeDTOComplex.class) {
            return (D) toDtoComplex((FoodType) source);
        } else if (source.getClass() == FoodTypeDTOComplex.class && destinationType == FoodType.class) {
            //todo - sledgehammer solution - to refactor
            return (D) mapFoodTypeDTOComplextoFoodType((FoodTypeDTOComplex) source);
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

    private FoodTypeDTOSimple toDtoSimple(FoodType foodType) {
        return new FoodTypeDTOSimple(
                foodType.getId(),
                foodType.getName(),
                foodType.getDescription());
    }

    private FoodTypeDTOComplex toDtoComplex(FoodType foodType) {
        return new FoodTypeDTOComplex(
                foodType.getId(),
                foodType.getFoodCategory() != null ? toDtoSimple(foodType.getFoodCategory()) : null,
                foodType.getName(),
                foodType.getDescription());
    }

    private FoodCategoryDTOSimple toDtoSimple(FoodCategory foodCategory) {
        return new FoodCategoryDTOSimple(
                foodCategory.getId(),
                foodCategory.getName(),
                foodCategory.getDescription()
        );
    }

    private FoodCategoryDTOComplex toDtoComplex(FoodCategory foodCategory) {
        return new FoodCategoryDTOComplex(
                foodCategory.getId(),
                foodCategory.getName(),
                foodCategory.getDescription(),
                mapList(foodCategory.getFoodTypes(), FoodTypeDTOSimple.class)
        );
    }

    /*
    Sledgehammer solution to the fact that ModelMapper default map method is not mapping the contained food category
    todo - Refactor to use native ModelMapper functionality
     */
    private FoodType mapFoodTypeDTOComplextoFoodType(FoodTypeDTOComplex source) {
        return FoodType.builder()
                .id(source.id())
                .name(source.name())
                .description(source.description())
                .foodCategory(
                        source.foodCategory() != null
                                ? super.map(source.foodCategory(), FoodCategory.class)
                                : null
                )
                .build();
    }

}
