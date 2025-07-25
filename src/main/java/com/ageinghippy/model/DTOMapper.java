package com.ageinghippy.model;

import com.ageinghippy.model.dto.*;
import com.ageinghippy.model.entity.*;
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
        } else if (destinationType == DishComponentDTO.class) {
            return (D) toDTO((DishComponent) source);
        } else if (source.getClass() == DishComponentDTO.class && destinationType == DishComponent.class) {
            //todo - sledgehammer solution - to refactor
            return (D) mapDishComponentDTODishComponent((DishComponentDTO) source);
        } else if (destinationType == DishDTOSimple.class) {
            return (D) toDtoSimple((Dish) source);
        } else if (source.getClass() == DishDTOSimple.class && destinationType == Dish.class) {
            //todo - sledgehammer solution - to refactor
            return (D) mapDishDTOSimpleToDish((DishDTOSimple) source);
        } else if (destinationType == DishDTOComplex.class) {
            return (D) toDtoComplex((Dish) source);
        } else if (source.getClass() == DishDTOComplex.class && destinationType == Dish.class) {
            //todo - sledgehammer solution - to refactor
            return (D) mapDishDTOComplexToDish((DishDTOComplex) source);
        } else if (destinationType == UserPrincipleDTOSimple.class) {
            return (D) toDtoSimple((UserPrinciple) source);
        } else if (destinationType == MealDTOSimple.class) {
            return (D) toDtoSimple((Meal) source);
        } else if (destinationType == MealDTOComplex.class) {
            return (D) toDtoComplex((Meal) source);
        } else if (destinationType == MealComponentDTO.class) {
            return (D) toDto((MealComponent) source);
        }else if (source.getClass() == MealDTOComplex.class && destinationType == Meal.class) {
            return (D) mapMealDTOComplexToMeal((MealDTOComplex) source);
        } else if (destinationType == MealComponent.class) {
            return (D) mapMealComponentDTOToMealComponent((MealComponentDTO) source);
        }
        else {
            return super.map(source, destinationType);
        }
    }

    public <D> List<D> mapList(List<?> sources, Class<D> destinationType) {
        return sources.stream()
                .map(item -> this.map(item, destinationType))
                .toList();
    }

    private PreparationTechniqueDTO toDto(PreparationTechnique preparationTechnique) {
        return new PreparationTechniqueDTO(preparationTechnique.getId(),
                preparationTechnique.getCode(),
                preparationTechnique.getDescription());
    }

    private DishComponentDTO toDTO(DishComponent dishComponent) {
        return new DishComponentDTO(dishComponent.getId(),
                map(dishComponent.getFoodType(), FoodTypeDTOSimple.class),
                dishComponent.getProportion());
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

    private DishDTOSimple toDtoSimple(Dish source) {
        return new DishDTOSimple(source.getId(),
                source.getName(),
                source.getDescription(),
                map(source.getPreparationTechnique(), PreparationTechniqueDTO.class));
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

    private DishComponent mapDishComponentDTODishComponent(DishComponentDTO source) {
        return DishComponent.builder()
                .id(source.id())
                .foodType(
                        source.foodType() != null
                                ? map(source.foodType(), FoodType.class)
                                : null)
                .proportion(source.proportion())
                .build();
    }

    private Dish mapDishDTOSimpleToDish(DishDTOSimple source) {
        return Dish.builder()
                .id(source.id())
                .name(source.name())
                .description(source.description())
                .preparationTechnique(map(source.preparationTechnique(), PreparationTechnique.class))
                .build();
    }

    private DishDTOComplex toDtoComplex(Dish source) {
        return new DishDTOComplex(
                source.getId(),
                source.getName(),
                source.getDescription(),
                map(source.getPreparationTechnique(), PreparationTechniqueDTO.class),
                source.getDishComponents() != null
                        ? mapList(source.getDishComponents(), DishComponentDTO.class)
                        : null);
    }

    private Dish mapDishDTOComplexToDish(DishDTOComplex source) {
        return Dish.builder()
                .id(source.id())
                .name(source.name())
                .description(source.description())
                .preparationTechnique(map(source.preparationTechnique(), PreparationTechnique.class))
                .dishComponents(
                        source.dishComponents() != null
                                ? mapList(source.dishComponents(), DishComponent.class)
                                : List.of()
                )
                .build();
    }

    private MealDTOSimple toDtoSimple(Meal source) {
        return new MealDTOSimple(
                source.getId(),
                source.getDescription(),
                source.getDate(),
                source.getTime());
    }

    private MealDTOComplex toDtoComplex(Meal source) {
        return new MealDTOComplex(
                source.getId(),
                source.getDescription(),
                source.getDate(),
                source.getTime(),
                mapList(source.getMealComponents(), MealComponentDTO.class)
        );
    }

    private MealComponentDTO toDto(MealComponent source) {
        return new MealComponentDTO(
                source.getId(),
                map(source.getFoodType(),FoodTypeDTOSimple.class),
                map(source.getPreparationTechnique(),PreparationTechniqueDTO.class),
                source.getVolume()
        );
    }

    private Meal mapMealDTOComplexToMeal(MealDTOComplex source) {
        return Meal.builder()
                .id(source.id())
                .description(source.description())
                .date(source.date())
                .time(source.time())
                .mealComponents(
                        source.mealComponents() != null
                                ? mapList(source.mealComponents(), MealComponent.class)
                                : List.of()
                )
                .build();
    }

    private MealComponent mapMealComponentDTOToMealComponent(MealComponentDTO source) {
        return MealComponent.builder()
                .id(source.id())
                .foodType(
                        source.foodType() != null
                                ? map(source.foodType(), FoodType.class)
                                : null)
                .preparationTechnique(map(source.preparationTechnique(), PreparationTechnique.class))
                .volume(source.volume())
                .build();
    }

    private UserPrincipleDTOSimple toDtoSimple(UserPrinciple source) {
        return new UserPrincipleDTOSimple(source.getId(), source.getUsername(), source.getUserMeta().getName());
    }
}
