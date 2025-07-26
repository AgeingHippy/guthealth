package com.ageinghippy.util;

import com.ageinghippy.DataSetupHelper;
import com.ageinghippy.model.dto.DishComponentDTO;
import com.ageinghippy.model.dto.DishDTOComplex;
import com.ageinghippy.model.dto.MealComponentDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ManipulateMealComponentsTest {

    private final DataSetupHelper dsh = new DataSetupHelper();

    @BeforeEach
    void setUp() {
    }

    @Test
    void buildMealComponentsFromDish_emptyList() {
        DishDTOComplex dish = dsh.getDishDTOComplex(4L);
        List<MealComponentDTO> expected = List.of();

        List<MealComponentDTO> result = ManipulateMealComponents.buildMealComponentsFromDish(dish, 100);

        assertEquals(expected, result);
    }

    @Test
    void buildMealComponentsFromDish_singleItem() {
        DishDTOComplex dish4 = dsh.getDishDTOComplex(4L);
        DishComponentDTO dishComponent = new DishComponentDTO(
                99L,
                dsh.getFoodTypeDTOSimple(1L),
                1
        );
        DishDTOComplex dish = new DishDTOComplex(
                dish4.id(),
                dish4.name(),
                dish4.description(),
                dish4.preparationTechnique(),
                List.of(dishComponent)
        );

        List<MealComponentDTO> expected = List.of(
                new MealComponentDTO(null, dishComponent.foodType(), dish.preparationTechnique(), 300)
        );

        List<MealComponentDTO> result = ManipulateMealComponents.buildMealComponentsFromDish(dish, 300);

        assertEquals(expected, result);
    }

    @Test
    void buildMealComponentsFromDish_multipleItems() {
        DishDTOComplex dish = dsh.getDishDTOComplex(3L);

        List<MealComponentDTO> expected = List.of(
                new MealComponentDTO(
                        null,
                        dish.dishComponents().get(0).foodType(),
                        dish.preparationTechnique(),
                        200
                        ),
                new MealComponentDTO(
                        null,
                        dish.dishComponents().get(1).foodType(),
                        dish.preparationTechnique(),
                        400
                )
        );

        List<MealComponentDTO> result = ManipulateMealComponents.buildMealComponentsFromDish(dish, 600);

        assertEquals(expected,result);
    }
}