package com.ageinghippy.util;

import com.ageinghippy.model.dto.DishComponentDTO;
import com.ageinghippy.model.dto.DishDTOComplex;
import com.ageinghippy.model.dto.MealComponentDTO;

import java.util.ArrayList;
import java.util.List;

public class ManipulateMealComponents {

    public static List<MealComponentDTO> buildMealComponentsFromDish(DishDTOComplex dish, Integer servingSize) {
        List<MealComponentDTO> mealComponents = new ArrayList<>();
        Integer dishProportionTotal = dish.dishComponents().stream()
                .map(DishComponentDTO::proportion).reduce(0, Integer::sum);
        dish.dishComponents().forEach(dishComponent -> {
            mealComponents.add(
                    new MealComponentDTO(null,
                            dishComponent.foodType(),
                            dish.preparationTechnique(),
                            servingSize * dishComponent.proportion() / dishProportionTotal
                    ));
        });
        return mealComponents;
    }

}
