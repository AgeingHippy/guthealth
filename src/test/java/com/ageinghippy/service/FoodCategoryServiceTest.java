package com.ageinghippy.service;

import com.ageinghippy.DataSetupHelper;
import com.ageinghippy.model.DTOMapper;
import com.ageinghippy.model.dto.FoodCategoryDTOComplex;
import com.ageinghippy.model.dto.FoodTypeDTOSimple;
import com.ageinghippy.model.entity.FoodCategory;
import com.ageinghippy.model.entity.FoodType;
import com.ageinghippy.model.entity.UserPrinciple;
import com.ageinghippy.repository.FoodCategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cache.CacheManager;

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

    @Mock
    private UserPrincipleService userPrincipleService;

    @Mock
    CacheManager cacheManager;

    private FoodCategoryService foodCategoryService;

    private final DataSetupHelper dsh = new DataSetupHelper();

    @BeforeEach
    void setUp() {
        FoodCategoryService service =
                new FoodCategoryService(
                        foodCategoryRepository,
                        userPrincipleService,
                        new DTOMapper(),
                        null,
                        cacheManager);
        foodCategoryService = spy(service);
    }

    @Test
    public void copy_new_foodCategory_and_associated_food_types() {
        UserPrinciple userPrinciple = dsh.getPrinciple("basic");
        FoodCategory systemFoodCategory = dsh.getFoodCategory(6L);

        when(foodCategoryRepository.findById(6L)).thenReturn(Optional.of(systemFoodCategory));
        when(foodCategoryRepository.findByNameAndPrinciple(systemFoodCategory.getName(), userPrinciple))
                .thenReturn(Optional.empty());
        when(cacheManager.getCache(anyString())).thenReturn(null);

        doAnswer(invocation -> {
            FoodCategory fc = (FoodCategory) invocation.getArguments()[0];
            assertEquals(userPrinciple, fc.getPrinciple());
            return fc;
        }).when(foodCategoryService).saveFoodCategory(any(FoodCategory.class));

        //GIVEN - user does not have a system food category

        //WHEN - the user chooses to import the food category
        FoodCategoryDTOComplex resultFoodCategoryDTOComplex = foodCategoryService.copyFoodCategory(6L, userPrinciple);

        //THEN - the food category and associate food types are imported
        assertEquals(systemFoodCategory.getName(), resultFoodCategoryDTOComplex.name());
        assertEquals(systemFoodCategory.getName(), resultFoodCategoryDTOComplex.name());
        assertEquals(systemFoodCategory.getFoodTypes().size(), resultFoodCategoryDTOComplex.foodTypes().size());
        for (int i = 0; i < systemFoodCategory.getFoodTypes().size(); i++) {
            assertEquals(
                    systemFoodCategory.getFoodTypes().get(i).getName(),
                    resultFoodCategoryDTOComplex.foodTypes().get(i).name());
        }

        verify(foodCategoryService, times(1)).saveFoodCategory(any(FoodCategory.class));
    }

    @Test
    void append_new_food_types_to_existing_foodCategory() {
        //GIVEN a user has a food category with the same name and at least 1 matching foodType
        UserPrinciple userPrinciple = dsh.getPrinciple("basic");
        FoodCategory systemFoodCategory = dsh.getFoodCategory(8L);
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
                        .name(systemFoodCategory.getFoodTypes().get(2).getName())
                        .description("alternative description")
                        .foodCategory(targetFoodCategory)
                        .build());

        doAnswer(returnsFirstArg()).when(foodCategoryService).saveFoodCategory(any(FoodCategory.class));

        when(foodCategoryRepository.findById(8L)).thenReturn(Optional.of(systemFoodCategory));
        when(foodCategoryRepository.findByNameAndPrinciple(systemFoodCategory.getName(), userPrinciple))
                .thenReturn(Optional.of(targetFoodCategory));
        when(cacheManager.getCache(anyString())).thenReturn(null);

        //WHEN we choose to copy the food category
        FoodCategoryDTOComplex resultFoodCategoryDTOComplex = foodCategoryService.copyFoodCategory(8L, userPrinciple);

        //THEN unmatched food types are appended to the users food category
        List<String> resultFoodTypeNames = resultFoodCategoryDTOComplex.foodTypes().stream().map(FoodTypeDTOSimple::name).toList();
        systemFoodCategory.getFoodTypes().stream()
                .map(FoodType::getName)
                .forEach(name -> {
                    assert (resultFoodTypeNames.contains(name));
                });

        //verify original unmatched type still exists in list
        assert (resultFoodTypeNames.contains("UnmatchedType"));

        //verify original matched type is unmodified
        FoodTypeDTOSimple originalMatchedFoodTypeDTOSimple = resultFoodCategoryDTOComplex.foodTypes().stream()
                .filter(ft -> systemFoodCategory.getFoodTypes().get(2).getName().equals(ft.name()))
                .findFirst().orElseThrow();
        assertEquals("alternative description", originalMatchedFoodTypeDTOSimple.description());
    }

    @Test
    void copy_all_system_foodCategories_and_associated_food_types() {
        UserPrinciple principle = dsh.getPrinciple("basic");
        UserPrinciple systemPrinciple = dsh.getPrinciple("system");
        List<FoodCategory> systemFoodCategories = List.of(
                dsh.getFoodCategory(6L),
                dsh.getFoodCategory(7L),
                dsh.getFoodCategory(8L),
                dsh.getFoodCategory(9L)
        );

        when(userPrincipleService.loadUserByUsername("system")).thenReturn(systemPrinciple);
        when(foodCategoryRepository.findAllByPrinciple(systemPrinciple)).thenReturn(systemFoodCategories);

        lenient().doReturn(new FoodCategoryDTOComplex(null,null,null,null))
                .when(foodCategoryService).copyFoodCategory(anyLong(),any(UserPrinciple.class));

        foodCategoryService.copyFoodCategories(principle);

        verify(foodCategoryService, times(1)).copyFoodCategory(6L, principle);
        verify(foodCategoryService, times(1)).copyFoodCategory(7L, principle);
        verify(foodCategoryService, times(1)).copyFoodCategory(8L, principle);
        verify(foodCategoryService, times(1)).copyFoodCategory(9L, principle);

    }
}