package com.ageinghippy.service;

import com.ageinghippy.DataSetupHelper;
import com.ageinghippy.model.DTOMapper;
import com.ageinghippy.model.dto.*;
import com.ageinghippy.model.entity.Meal;
import com.ageinghippy.model.entity.UserPrinciple;
import com.ageinghippy.repository.MealRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MealServiceTest {

    @Mock
    private MealRepository mealRepository;

    @Mock
    private EntityManager entityManager;

    private DataSetupHelper dsh = new DataSetupHelper();

    private MealService mealService;

    @BeforeEach
    void setup() {
        mealService = new MealService(mealRepository, entityManager, new DTOMapper());
    }

    @Test
    void getMeals_success_hasMeals() {
        UserPrinciple principle = dsh.getPrinciple(2L);
        List<Meal> meals = List.of(
                dsh.getMeal(1L),
                dsh.getMeal(2L),
                dsh.getMeal(3L),
                dsh.getMeal(4L));

        List<MealDTOSimple> expectedList = List.of(
                dsh.getMealDTOSimple(1L),
                dsh.getMealDTOSimple(2L),
                dsh.getMealDTOSimple(3L),
                dsh.getMealDTOSimple(4L));

        when(mealRepository.findAllByPrinciple(principle)).thenReturn(meals);

        List<MealDTOSimple> resultsList = mealService.getMeals(principle);

        assertEquals(expectedList, resultsList);

        verify(mealRepository, times(1)).findAllByPrinciple(principle);
    }

    @Test
    void getMeals_success_hasNoMeals() {
        UserPrinciple principle = dsh.getPrinciple(3L);
        List<Meal> meals = List.of();

        List<MealDTOSimple> expectedList = List.of();

        when(mealRepository.findAllByPrinciple(principle)).thenReturn(meals);

        List<MealDTOSimple> resultsList = mealService.getMeals(principle);

        assertEquals(expectedList, resultsList);

        verify(mealRepository, times(1)).findAllByPrinciple(principle);
    }

    @Test
    void getSpecificMeal_exists() {
        Meal meal = dsh.getMeal(1L);
        MealDTOComplex expected = dsh.getMealDTOComplex(1L);

        when(mealRepository.findById(1L)).thenReturn(Optional.ofNullable(meal));

        MealDTOComplex result = mealService.getMeal(1L);

        assertEquals(expected, result);

        verify(mealRepository, times(1)).findById(1L);
    }

    @Test
    void createMealFromSimpleDto() {
        MealDTOSimple mealDTOSimple = new MealDTOSimple(null,
                "Meal Description",
                LocalDate.parse("2025-06-17"),
                LocalTime.parse("09:30"));

        UserPrinciple principle = dsh.getPrinciple(2L);

        Meal newMeal = Meal.builder()
                .id(99L)
                .principle(principle)
                .description(mealDTOSimple.description())
                .date(mealDTOSimple.date())
                .time(mealDTOSimple.time())
                .mealComponents(List.of())
                .build();

        MealDTOComplex expected = new MealDTOComplex(
                99L,
                "Meal Description",
                LocalDate.parse("2025-06-17"),
                LocalTime.parse("09:30"),
                List.of());

        when(mealRepository.save(any(Meal.class))).thenAnswer(invocation -> {
            Meal meal = invocation.getArgument(0);
            assertEquals("Meal Description", meal.getDescription());
            assertEquals(principle, meal.getPrinciple());

            return newMeal;
        });

        MealDTOComplex result = mealService.createMeal(mealDTOSimple, principle);

        assertEquals(expected, result);

        verify(mealRepository, times(1)).save(any(Meal.class));
    }

    @Test
    void createMealFromComplexDto() {
        UserPrinciple principle = dsh.getPrinciple(2L);
        Meal newMeal = dsh.getMeal(1L);
        MealDTOComplex expected = dsh.getMealDTOComplex(1L);

        MealDTOComplex request =
                new MealDTOComplex(
                        null,
                        newMeal.getDescription(),
                        newMeal.getDate(),
                        newMeal.getTime(),
                        newMeal.getMealComponents().stream()
                                .map(mc -> new MealComponentDTO(
                                        null,
                                        dsh.getFoodTypeDTOSimple(mc.getFoodType().getId()),
                                        dsh.getPreparationTechniqueDTO(mc.getPreparationTechnique().getId()),
                                        mc.getVolume()
                                ))
                                .toList()
                );

        when(mealRepository.save(any(Meal.class)))
                .thenAnswer(invocation -> {
                    Meal meal = invocation.getArgument(0);
                    assertEquals(meal.getPrinciple(), principle);

                    return newMeal;
                });

        MealDTOComplex result = mealService.createMeal(request, principle);

        assertEquals(expected, result);

        verify(mealRepository, times(1)).save(any(Meal.class));
    }

    @Test
    void updateMeal_all_elements_updated() {
        Meal meal = dsh.getMeal(4L);
        MealDTOSimple mealDTOSimple = new MealDTOSimple(
                4L,
                "new description",
                LocalDate.parse("2025-06-01"),
                LocalTime.parse("10:30"));
        Meal updatedMeal = Meal.builder()
                .id(meal.getId())
                .description(mealDTOSimple.description())
                .date(mealDTOSimple.date())
                .time(mealDTOSimple.time())
                .build();
        MealDTOComplex expected = new MealDTOComplex(4L,
                "new description",
                LocalDate.parse("2025-06-01"),
                LocalTime.parse("10:30"),
                List.of());

        when(mealRepository.findById(4L)).thenReturn(Optional.of(meal));
        when(mealRepository.save(any(Meal.class))).then(returnsFirstArg());

        MealDTOComplex result = mealService.updateMeal(4L, mealDTOSimple);

        assertEquals(expected, result);

        verify(mealRepository, times(1)).findById(4L);
        verify(mealRepository, times(1)).save(any(Meal.class));

    }

    @Test
    void updateMeal_no_elements_updated() {
        Meal meal = dsh.getMeal(3L);
        MealDTOSimple mealDTOSimple = new MealDTOSimple(
                3L,
                null,
                null,
                null);
        MealDTOComplex expected =
                new MealDTOComplex(3L,
                        meal.getDescription(),
                        meal.getDate(),
                        meal.getTime(),
                        meal.getMealComponents().stream()
                                .map(mc ->
                                        new MealComponentDTO(
                                                mc.getId(),
                                                new FoodTypeDTOSimple(
                                                        mc.getFoodType().getId(),
                                                        mc.getFoodType().getName(),
                                                        mc.getFoodType().getDescription()),
                                                new PreparationTechniqueDTO(
                                                        mc.getPreparationTechnique().getId(),
                                                        mc.getPreparationTechnique().getCode(),
                                                        mc.getPreparationTechnique().getDescription()),
                                                mc.getVolume()
                                        ))
                                .toList()
                );

        when(mealRepository.findById(3L)).thenReturn(Optional.of(meal));
        when(mealRepository.save(any(Meal.class))).then(returnsFirstArg());

        MealDTOComplex result = mealService.updateMeal(3L, mealDTOSimple);

        assertEquals(expected, result);

        verify(mealRepository, times(1)).findById(3L);
        verify(mealRepository, times(1)).save(any(Meal.class));
    }

    @Test
    void updateMeal_notFound() {
        when(mealRepository.findById(99L)).thenThrow(new NoSuchElementException());

        assertThrows(
                NoSuchElementException.class,
                () -> mealService.updateMeal(99L, new MealDTOSimple(null, null, null, null))
        );
    }

    @Test
    void deleteMeal() {
        Meal meal = dsh.getMeal(4L);
        when(mealRepository.findById(4L)).thenReturn(Optional.of(meal));
        doNothing().when(mealRepository).deleteById(4L);

        mealService.deleteMeal(4L);

        verify(mealRepository,times(1)).deleteById(4L);
    }

}
