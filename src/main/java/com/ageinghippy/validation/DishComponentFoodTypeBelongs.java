package com.ageinghippy.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = DishComponentFoodTypeBelongsValidator.class)
@Target( ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface DishComponentFoodTypeBelongs {
    String message() default "record owner remains consistent";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
