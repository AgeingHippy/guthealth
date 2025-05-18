package com.ageinghippy.service;

import com.ageinghippy.DataSetupHelper;
import com.ageinghippy.model.dto.FoodCategoryDTOComplex;
import com.ageinghippy.model.entity.FoodCategory;
import com.ageinghippy.model.entity.FoodType;
import com.ageinghippy.model.entity.UserPrinciple;
import com.ageinghippy.repository.FoodCategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
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
    public void copy_new_foodCategory_and_associated_food_types() {
        UserPrinciple userPrinciple = dsh.getPrinciple("basic");
        FoodCategory systemFoodCategory = dsh.getFoodCategory(6L);

//        when(foodCategoryRepository.findById(6L)).thenReturn(Optional.of(systemFoodCategory));
//        when(foodCategoryRepository.findByNameAndPrinciple(systemFoodCategory.getName(), userPrinciple))
//                .thenReturn(Optional.empty());

        doAnswer(returnsFirstArg()).when(foodCategoryService).saveFoodCategory(any(FoodCategory.class));

        //GIVEN - user does not have a system food category

        //WHEN - the user chooses to import the food category
        FoodCategory newFoodCategory = foodCategoryService.copyFoodCategory(systemFoodCategory, userPrinciple);
//        FoodCategory newFoodCategory = foodCategoryService.copyFoodCategory(6L, userPrinciple);

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

    @Test
    void append_new_food_types_to_existing_foodCategory() {
        //GIVEN a user has a food category with the same name and at least 1 matching foodType
        FoodCategory systemFoodCategory = dsh.getFoodCategory(6L);
        FoodCategory targetFoodCategory = FoodCategory.builder()
                .name(systemFoodCategory.getName())
                .description(systemFoodCategory.getDescription())
                .foodTypes(new ArrayList<>())
                .build();
        targetFoodCategory.getFoodTypes().add(
                FoodType.builder()
                        .name("UnmatchedType")
                        .description("temp")
                        .foodCategory(targetFoodCategory)
                        .build());
        targetFoodCategory.getFoodTypes().add(
                FoodType.builder()
                        .name(systemFoodCategory.getFoodTypes().get(3).getName())
                        .description("alternative description")
                        .foodCategory(targetFoodCategory)
                        .build());

        doAnswer(returnsFirstArg()).when(foodCategoryService).saveFoodCategory(any(FoodCategory.class));

        //WHEN we choose to copy the food category
        FoodCategory resultFoodCategory = foodCategoryService.appendFoodTypesToFoodCategory(targetFoodCategory,systemFoodCategory);

        //THEN unmatched food types are appended to the users food category
        List<String> resultFoodTypeNames = resultFoodCategory.getFoodTypes().stream().map(FoodType::getName).toList();
        systemFoodCategory.getFoodTypes().stream()
                .map(FoodType::getName)
                .forEach(name -> {
                    assert(resultFoodTypeNames.contains(name));
                });

        //verify original unmatched type still exists in list
        assert(resultFoodTypeNames.contains("UnmatchedType"));

        //verify original matched type is unmodified
        FoodType originalMatchedFoodType = resultFoodCategory.getFoodTypes().stream()
                .filter(ft -> systemFoodCategory.getFoodTypes().get(3).getName().equals(ft.getName()))
                .findFirst().orElseThrow();
        assertEquals("alternative description",originalMatchedFoodType.getDescription());

    }
}