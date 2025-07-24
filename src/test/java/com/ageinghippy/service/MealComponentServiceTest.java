package com.ageinghippy.service;

import com.ageinghippy.DataSetupHelper;
import com.ageinghippy.model.DTOMapper;
import com.ageinghippy.model.dto.FoodTypeDTOSimple;
import com.ageinghippy.model.dto.MealComponentDTO;
import com.ageinghippy.model.dto.PreparationTechniqueDTO;
import com.ageinghippy.model.entity.FoodType;
import com.ageinghippy.model.entity.Meal;
import com.ageinghippy.model.entity.MealComponent;
import com.ageinghippy.model.entity.PreparationTechnique;
import com.ageinghippy.repository.FoodTypeRepository;
import com.ageinghippy.repository.MealComponentRepository;
import com.ageinghippy.repository.MealRepository;
import com.ageinghippy.repository.PreparationTechniqueRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MealComponentServiceTest {

    @Mock
    private MealRepository mealRepository;
    @Mock
    private MealComponentRepository mealComponentRepository;
    @Mock
    private FoodTypeRepository foodTypeRepository;
    @Mock
    private PreparationTechniqueRepository preparationTechniqueRepository;

    private DTOMapper dtoMapper = new DTOMapper();

    @Mock
    private EntityManager entityManager;

    private MealComponentService mealComponentService;

    private DataSetupHelper dsh = new DataSetupHelper();


    @BeforeEach
    void setup() {
        mealComponentService = new MealComponentService(mealRepository, mealComponentRepository,
                foodTypeRepository, preparationTechniqueRepository, dtoMapper, entityManager);
    }

    @Test
    void testCreateNewMealComponent_success() {
        MealComponentDTO mealComponent = new MealComponentDTO(null,
                new FoodTypeDTOSimple(1L, null, null),
                new PreparationTechniqueDTO(1L, null, null),
                100);

        Meal meal1 = dsh.getMeal(1L);
        FoodType foodType1 = dsh.getFoodType(1L);
        PreparationTechnique preparationTechnique1 = dsh.getPreparationTechnique(1L);

        MealComponent savedMealComponent = MealComponent.builder()
                .id(8L)
                .meal(meal1)
                .foodType(foodType1)
                .preparationTechnique(preparationTechnique1)
                .volume(100)
                .build();

        MealComponentDTO expectedMealComponent = new MealComponentDTO(8L,
                dsh.getFoodTypeDTOSimple(1L),
                dsh.getPreparationTechniqueDTO(1L),
                100);

        when(mealRepository.findById(1L)).thenReturn(Optional.of(meal1));
        when(foodTypeRepository.findById(1L)).thenReturn(Optional.of(foodType1));
        when(preparationTechniqueRepository.findById(1L)).thenReturn(Optional.of(preparationTechnique1));

        when(mealComponentRepository.save(any(MealComponent.class))).thenAnswer(invocation -> {
            MealComponent mealComponentParameter = invocation.getArgument(0);
            assertEquals(meal1, mealComponentParameter.getMeal());
            assertEquals(foodType1,mealComponentParameter.getFoodType());
            assertEquals(preparationTechnique1,mealComponentParameter.getPreparationTechnique());
            assertEquals(100,mealComponentParameter.getVolume());
            return savedMealComponent;
        });

        MealComponentDTO createdMealComponent =
                mealComponentService.createNewMealComponent(1L, mealComponent);

        assertEquals(expectedMealComponent,createdMealComponent);
    }
}
