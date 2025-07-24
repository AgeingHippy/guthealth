package com.ageinghippy.service;

import com.ageinghippy.model.dto.FoodTypeDTOSimple;
import com.ageinghippy.model.dto.MealComponentDTO;
import com.ageinghippy.model.dto.PreparationTechniqueDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class MealComponentServiceTest {


    private MealComponentService mealComponentService;


    @BeforeEach
    void setup() {
        mealComponentService = new MealComponentService();
    }

    void testCreateNewMealComponent_success() {
        MealComponentDTO mealComponent = new MealComponentDTO(null,
                new FoodTypeDTOSimple(1L,null,null),
                new PreparationTechniqueDTO(1L, null, null),
                100);

        MealComponentDTO createdMealComponent = mealComponentService.createNewMealComponent(mealId, mealComponent);

    }
}
