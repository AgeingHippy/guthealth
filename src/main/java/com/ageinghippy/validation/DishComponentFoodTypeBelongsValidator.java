package com.ageinghippy.validation;

import com.ageinghippy.model.entity.Dish;
import com.ageinghippy.model.entity.DishComponent;
import com.ageinghippy.model.entity.UserPrinciple;
import com.ageinghippy.repository.FoodTypeRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

import java.util.ListIterator;

@RequiredArgsConstructor
public class DishComponentFoodTypeBelongsValidator implements ConstraintValidator<DishComponentFoodTypeBelongs,Dish> {
//todo - somehow make this more generic. Possibly using reflection
//      see https://stackoverflow.com/questions/40353638/spring-custom-annotation-validation-with-multiple-field as a start
    private final FoodTypeRepository foodTypeRepository;

    @Override
    public void initialize(DishComponentFoodTypeBelongs constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Dish dish, ConstraintValidatorContext constraintValidatorContext) {
        boolean isValid = true;

        Long dishOwner = dish.getPrinciple().getId();
        Long foodTypeOwner;
        DishComponent dishComponent;

        ListIterator<DishComponent> dishComponentsIterator = dish.getDishComponents().listIterator();

        while (isValid && dishComponentsIterator.hasNext()) {
            dishComponent = dishComponentsIterator.next();

            if (dishComponent.getFoodType() != null &&
                dishComponent.getFoodType().getFoodCategory() != null &&
                dishComponent.getFoodType().getFoodCategory().getPrinciple() !=null) {
                foodTypeOwner = dishComponent.getFoodType().getFoodCategory().getPrinciple().getId();
            } else {
                foodTypeOwner = foodTypeRepository.findById(
                                dishComponent.getFoodType().getId())
                        .orElseThrow()
                        .getFoodCategory().getPrinciple().getId();
            }
            isValid = isValid & dishOwner.equals(foodTypeOwner);
        }

        return isValid;
    }
}
