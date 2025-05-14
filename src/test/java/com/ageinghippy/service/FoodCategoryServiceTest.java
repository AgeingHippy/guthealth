package com.ageinghippy.service;

import com.ageinghippy.DataSetupHelper;
import com.ageinghippy.model.dto.FoodCategoryDTOComplex;
import com.ageinghippy.model.entity.FoodCategory;
import com.ageinghippy.model.entity.UserPrinciple;
import com.ageinghippy.repository.FoodCategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FoodCategoryServiceTest {
    @Mock
    private FoodCategoryRepository foodCategoryRepository;
//    private final DTOMapper dTOMapper;
//    private final EntityManager entityManager;
//    private final CacheManager cacheManager;

    private FoodCategoryService foodCategoryService;

    private DataSetupHelper dsh = new DataSetupHelper();

    @BeforeEach
    void setUp() {
        FoodCategoryService service =
                new FoodCategoryService(
                        foodCategoryRepository,
                        null,
                        null,
                        null);
        foodCategoryService = spy(service);
    }

    @Test
    public void copy_foodCategory_and_associated_food_types() {
        UserPrinciple userPrinciple = dsh.getPrinciple("basic");
        FoodCategory systemFoodCategory = dsh.getFoodCategory(7L);

        doAnswer(returnsFirstArg()).when(foodCategoryService).saveFoodCategory(any(FoodCategory.class));

        //GIVEN - user does not have a system food category

        //WHEN - the user chooses to import the food category
        FoodCategory newFoodCategory = foodCategoryService.copyFoodCategory(systemFoodCategory, userPrinciple);

        //THEN - the food category and associate food types are imported and belong to the requesting user
        assertEquals(userPrinciple, newFoodCategory.getPrinciple());

        assertEquals(systemFoodCategory.getName(), newFoodCategory.getName());
        assertEquals(systemFoodCategory.getName(), newFoodCategory.getName());
        assertEquals(systemFoodCategory.getFoodTypes().size(), newFoodCategory.getFoodTypes().size());
        for (int i=0; i< systemFoodCategory.getFoodTypes().size(); i++ ){
            assertEquals(
                    systemFoodCategory.getFoodTypes().get(i).getName(),
                    newFoodCategory.getFoodTypes().get(i).getName());
        }

        verify(foodCategoryService, times(1)).saveFoodCategory(any(FoodCategory.class));
    }
}