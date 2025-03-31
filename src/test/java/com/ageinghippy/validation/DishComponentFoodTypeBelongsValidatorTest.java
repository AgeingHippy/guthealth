package com.ageinghippy.validation;

import com.ageinghippy.model.entity.*;
import com.ageinghippy.repository.FoodTypeRepository;
import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class DishComponentFoodTypeBelongsValidatorTest {

    private Role userRole;
    private Role adminRole;
    private UserPrinciple user1;
    private UserPrinciple user2;
    private FoodCategory foodCategory1;
    private FoodCategory foodCategory2;
    private FoodType foodType1_1;
    private FoodType foodType1_2;
    private FoodType foodType1_3;
    private FoodType foodType2_1;
    private PreparationTechnique preparationTechnique1;
    private PreparationTechnique preparationTechnique2;

    @Mock
    private DishComponentFoodTypeBelongs dishComponentFoodTypeBelongs;

    @Mock
    private ConstraintValidatorContext constraintValidatorContext;

    @Mock
    private FoodTypeRepository foodTypeRepository;

    @BeforeEach
    public void init() {
        adminRole = Role.builder().id(2L).authority("ROLE_ADMIN").build();
        userRole = Role.builder().id(1L).authority("ROLE_USER").build();

        user1 = UserPrinciple.builder()
                .id(1L)
                .username("basic")
                .password("password")
                .authorities(List.of(userRole))
                .userMeta(UserMeta.builder().id(1L).build())
                .build();

        user2 = UserPrinciple.builder()
                .id(2L)
                .username("admin")
                .password("password")
                .authorities(List.of(adminRole))
                .userMeta(UserMeta.builder().id(2L).build())
                .build();

        foodCategory1 = FoodCategory.builder()
                .id(1L)
                .name("foodCategory1")
                .description("foodCategory1 description")
                .principle(user1)
                .build();

        foodCategory2 = FoodCategory.builder()
                .id(2L)
                .name("foodCategory2")
                .description("foodCategory2 description")
                .principle(user2)
                .build();

        foodType1_1 = FoodType.builder()
                .id(11L)
                .name("foodType1_1")
                .description("foodType1_1 description")
                .foodCategory(foodCategory1)
                .build();

        foodType1_2 = FoodType.builder()
                .id(12L)
                .name("foodType1_2")
                .description("foodType1_2 description")
                .foodCategory(foodCategory1)
                .build();

        foodType1_3 = FoodType.builder()
                .id(13L)
                .name("foodType1_3")
                .description("foodType1_3 description")
                .foodCategory(foodCategory1)
                .build();

        foodType2_1 = FoodType.builder()
                .id(21L)
                .name("foodType2_1")
                .description("foodType2_1 description")
                .foodCategory(foodCategory2)
                .build();

        preparationTechnique1 = PreparationTechnique.builder().code("pt1").description("pt1 description").build();
        preparationTechnique2 = PreparationTechnique.builder().code("pt2").description("pt2 description").build();

    }

    @Test
    void isValid_no_child_entities() {
        //given a dish with no components
        Dish dish = Dish.builder()
                .name("Dish1")
                .description("Dish1 description")
                .principle(user1)
                .preparationTechnique(preparationTechnique1)
                .build();

        //when the dishComponents are validated
        DishComponentFoodTypeBelongsValidator validator = new DishComponentFoodTypeBelongsValidator(foodTypeRepository);
        validator.initialize(dishComponentFoodTypeBelongs);

        boolean result = validator.isValid(dish, constraintValidatorContext);

        //Then the dish is valid
        assertTrue(result);
    }

    @Test
    void isValid_with_full_child_entities() {
        //given a dish with valid dish components
        Dish dish = Dish.builder()
                .name("Dish1")
                .description("Dish1 description")
                .principle(user1)
                .preparationTechnique(preparationTechnique1)
                .dishComponents(List.of(
                        DishComponent.builder()
                                .foodType(foodType1_1)
                                .proportion(100)
                                .build(),
                        DishComponent.builder()
                                .foodType(foodType1_2)
                                .proportion(200)
                                .build(),
                        DishComponent.builder()
                                .foodType(foodType1_3)
                                .proportion(300)
                                .build()
                ))
                .build();

        //when the dishComponents are validated
        DishComponentFoodTypeBelongsValidator validator = new DishComponentFoodTypeBelongsValidator(foodTypeRepository);
        validator.initialize(dishComponentFoodTypeBelongs);

        boolean result = validator.isValid(dish, constraintValidatorContext);

        //Then the dish is valid
        assertTrue(result);
    }

    @Test
    void notValid_with_full_child_entities() {
        //given a dish with invalid dish components (foodType2_1)
        Dish dish = Dish.builder()
                .name("Dish1")
                .description("Dish1 description")
                .principle(user1)
                .preparationTechnique(preparationTechnique1)
                .dishComponents(List.of(
                        DishComponent.builder()
                                .foodType(foodType1_1)
                                .proportion(100)
                                .build(),
                        DishComponent.builder()
                                .foodType(foodType1_2)
                                .proportion(200)
                                .build(),
                        DishComponent.builder()
                                .foodType(foodType2_1)
                                .proportion(300)
                                .build()
                ))
                .build();

        //when the dishComponents are validated
        DishComponentFoodTypeBelongsValidator validator = new DishComponentFoodTypeBelongsValidator(foodTypeRepository);
        validator.initialize(dishComponentFoodTypeBelongs);

        boolean result = validator.isValid(dish, constraintValidatorContext);

        //Then the dish is invalid
        assertFalse(result);
    }

    @Test
    void isValid_with_partial_child_entities() {
        //mock foodTypeRepository responses
        doReturn(Optional.of(foodType1_1)).when(foodTypeRepository).findById(foodType1_1.getId());
        doReturn(Optional.of(foodType1_2)).when(foodTypeRepository).findById(foodType1_2.getId());
        doReturn(Optional.of(foodType1_3)).when(foodTypeRepository).findById(foodType1_3.getId());

        DishComponentFoodTypeBelongsValidator validator = new DishComponentFoodTypeBelongsValidator(foodTypeRepository);
        validator.initialize(dishComponentFoodTypeBelongs);

        //given a dish with partial components (as per CREATE)
        Dish dish = Dish.builder()
                .name("Dish1")
                .description("Dish1 description")
                .principle(user1)
                .preparationTechnique(preparationTechnique1)
                .dishComponents(List.of(
                        DishComponent.builder()
                                .foodType(FoodType.builder().id(foodType1_1.getId()).build())
                                .proportion(100)
                                .build(),
                        DishComponent.builder()
                                .foodType(FoodType.builder().id(foodType1_2.getId()).build())
                                .proportion(200)
                                .build(),
                        DishComponent.builder()
                                .foodType(FoodType.builder().id(foodType1_3.getId()).build())
                                .proportion(300)
                                .build()
                ))
                .build();

        //when the dishComponents are validated
        boolean result = validator.isValid(dish, constraintValidatorContext);

        //Then the dish is valid
        assertTrue(result);
    }

    @Test
    void notValid_with_partial_child_entities() {
        //mock foodTypeRepository responses
        doReturn(Optional.of(foodType1_1)).when(foodTypeRepository).findById(foodType1_1.getId());
        doReturn(Optional.of(foodType1_2)).when(foodTypeRepository).findById(foodType1_2.getId());
        doReturn(Optional.of(foodType2_1)).when(foodTypeRepository).findById(foodType2_1.getId());

        DishComponentFoodTypeBelongsValidator validator = new DishComponentFoodTypeBelongsValidator(foodTypeRepository);
        validator.initialize(dishComponentFoodTypeBelongs);

        //given a dish with partial components (as per CREATE) - invalid foodType2_1
        Dish dish = Dish.builder()
                .name("Dish1")
                .description("Dish1 description")
                .principle(user1)
                .preparationTechnique(preparationTechnique1)
                .dishComponents(List.of(
                        DishComponent.builder()
                                .foodType(FoodType.builder().id(foodType1_1.getId()).build())
                                .proportion(100)
                                .build(),
                        DishComponent.builder()
                                .foodType(FoodType.builder().id(foodType1_2.getId()).build())
                                .proportion(200)
                                .build(),
                        DishComponent.builder()
                                .foodType(FoodType.builder().id(foodType2_1.getId()).build())
                                .proportion(300)
                                .build()
                ))
                .build();

        //when the dishComponents are validated
        boolean result = validator.isValid(dish, constraintValidatorContext);

        //Then the dish is invalid
        assertFalse(result);
    }

}