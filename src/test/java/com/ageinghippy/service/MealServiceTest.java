package com.ageinghippy.service;

import com.ageinghippy.DataSetupHelper;
import com.ageinghippy.model.DTOMapper;
import com.ageinghippy.model.dto.MealDTOComplex;
import com.ageinghippy.model.dto.MealDTOSimple;
import com.ageinghippy.model.entity.Meal;
import com.ageinghippy.model.entity.MealComponent;
import com.ageinghippy.model.entity.UserPrinciple;
import com.ageinghippy.repository.MealRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
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

        verify(mealRepository,times(1)).findAllByPrinciple(principle);
    }

    @Test
    void getMeals_success_hasNoMeals() {
        UserPrinciple principle = dsh.getPrinciple(3L);
        List<Meal> meals = List.of();

        List<MealDTOSimple> expectedList = List.of();

        when(mealRepository.findAllByPrinciple(principle)).thenReturn(meals);

        List<MealDTOSimple> resultsList = mealService.getMeals(principle);

        assertEquals(expectedList, resultsList);

        verify(mealRepository,times(1)).findAllByPrinciple(principle);
    }

    @Test
    void getSpecificMeal_exists() {
        Meal meal = dsh.getMeal(1L);
        MealDTOComplex expected = dsh.getMealDTOComplex(1L);

        when(mealRepository.findById(1L)).thenReturn(Optional.ofNullable(meal));

        MealDTOComplex result = mealService.getMeal(1L);

        assertEquals(expected,result);

        verify(mealRepository,times(1)).findById(1L);
    }
}
