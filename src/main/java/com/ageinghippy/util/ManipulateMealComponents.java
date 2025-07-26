package com.ageinghippy.util;

import com.ageinghippy.model.dto.*;
import com.ageinghippy.model.entity.PreparationTechnique;

import java.util.*;

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

    public static List<MealComponentDTO> aggregateMealComponents(MealDTOComplex meal) {
        class KeySet {
            FoodTypeDTOSimple foodType;
            PreparationTechniqueDTO preparationTechnique;

            @Override
            public boolean equals(Object o) {
                if (o == null || getClass() != o.getClass()) return false;
                KeySet keySet = (KeySet) o;
                return Objects.equals(foodType, keySet.foodType) && Objects.equals(preparationTechnique, keySet.preparationTechnique);
            }

            @Override
            public int hashCode() {
                return Objects.hash(foodType, preparationTechnique);
            }
        }

        Set<KeySet> keys = new HashSet<>();

        meal.mealComponents().forEach(mc -> {
            KeySet keySet = new KeySet();
            keySet.foodType = mc.foodType();
            keySet.preparationTechnique = mc.preparationTechnique();
            keys.add(keySet);
        });

        List<MealComponentDTO> mealComponents;

        if (keys.size() == meal.mealComponents().size()) {
            mealComponents = meal.mealComponents();
        } else {
            mealComponents = new ArrayList<>();
            keys.forEach(keySet -> {
                Integer totalVolume = meal.mealComponents().stream()
                        .filter(mc ->
                                mc.foodType().equals(keySet.foodType) && mc.preparationTechnique().equals(keySet.preparationTechnique)
                        )
                        .map(MealComponentDTO::volume)
                        .reduce(0, Integer::sum);

                mealComponents.add(new MealComponentDTO(null,keySet.foodType, keySet.preparationTechnique, totalVolume));
            });
        }

        return mealComponents;
    }

}
