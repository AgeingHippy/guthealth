package com.ageinghippy.service;

import com.ageinghippy.DataSetupHelper;
import com.ageinghippy.model.DTOMapper;
import com.ageinghippy.model.dto.FoodCategoryDTOSimple;
import com.ageinghippy.model.dto.FoodTypeDTOComplex;
import com.ageinghippy.model.dto.FoodTypeDTOSimple;
import com.ageinghippy.model.entity.FoodCategory;
import com.ageinghippy.model.entity.FoodType;
import com.ageinghippy.model.entity.UserPrinciple;
import com.ageinghippy.repository.FoodCategoryRepository;
import com.ageinghippy.repository.FoodTypeRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FoodTypeServiceTest {

    @Mock
    private FoodTypeRepository foodTypeRepository;
    @Mock
    private FoodCategoryRepository foodCategoryRepository;
    @Mock
    private DTOMapper dtoMapper;
    @Mock
    private EntityManager entityManager;
    @Mock
    private CacheManager cacheManager;
    @Mock
    Cache foodCategoryCache;
    @Mock
    Cache foodTypeCache;

    private FoodTypeService foodTypeService;

    private DataSetupHelper dsh = new DataSetupHelper();

    @BeforeEach
    void setUp() {
        FoodTypeService service = new FoodTypeService(foodTypeRepository,
                foodCategoryRepository,
                dtoMapper,
                entityManager,
                cacheManager);

        lenient().when(cacheManager.getCache(anyString())).thenReturn(null);

        foodTypeService = spy(service);
    }

    @Test
    public void getFoodType_success() {
        FoodType foodType = dsh.getFoodType(1L);
        FoodTypeDTOComplex foodTypeDTOComplex = dsh.getFoodTypeDTOComplex(1L);

        when(foodTypeRepository.findById(1L)).thenReturn(Optional.of(foodType));
        when(dtoMapper.map(foodType, FoodTypeDTOComplex.class)).thenReturn(foodTypeDTOComplex);

        FoodTypeDTOComplex result = foodTypeService.getFoodType(1L);

        assertEquals(foodTypeDTOComplex, result);
    }

    @Test
    public void getFoodType_fail() {
        when(foodTypeRepository.findById(11L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> foodTypeService.getFoodType(11L));
    }

    @Test
    public void getFoodTypes_success() {
        List<FoodType> foodTypeList = List.of(
                dsh.getFoodType(1L),
                dsh.getFoodType(2L),
                dsh.getFoodType(3L),
                dsh.getFoodType(4L),
                dsh.getFoodType(5L)
        );

        List<FoodTypeDTOSimple> foodTypeDTOSimpleList = List.of(
                dsh.getFoodTypeDTOSimple(1L),
                dsh.getFoodTypeDTOSimple(2L),
                dsh.getFoodTypeDTOSimple(3L),
                dsh.getFoodTypeDTOSimple(4L),
                dsh.getFoodTypeDTOSimple(5L)
        );

        when(foodTypeRepository.findAllByFoodCategory_id(1L)).thenReturn(foodTypeList);

        when(dtoMapper.mapList(foodTypeList, FoodTypeDTOSimple.class)).thenReturn(foodTypeDTOSimpleList);

        foodTypeService.getFoodTypes(1L).forEach(foodTypeDTOSimple -> {
            assert (foodTypeDTOSimpleList.contains(foodTypeDTOSimple));
        });
    }

    @Test
    void getFoodTypes_success_none_found() {
        when(foodTypeRepository.findAllByFoodCategory_id(99L)).thenReturn(List.of());

        assertEquals(0, foodTypeService.getFoodTypes(99L).size());
    }

    @Test
    void createFoodType_success() {
        FoodTypeDTOComplex foodTypeDTOComplex = dsh.getFoodTypeDTOComplex(1L);
        FoodTypeDTOComplex newFoodTypeDTOComplex =
                new FoodTypeDTOComplex(
                        null,
                        new FoodCategoryDTOSimple(1L, null, null),
                        foodTypeDTOComplex.name(),
                        foodTypeDTOComplex.description());

        FoodType foodType = dsh.getFoodType(1L);
        FoodType newFoodType = FoodType.builder()
                .id(null)
                .foodCategory(FoodCategory.builder().id(1L).build())
                .name(foodType.getName())
                .description(foodType.getDescription())
                .build();

        lenient().when(dtoMapper.map(newFoodTypeDTOComplex, FoodType.class)).thenReturn(newFoodType);
        lenient().when(dtoMapper.map(foodType, FoodTypeDTOComplex.class)).thenReturn(foodTypeDTOComplex);
        when(foodTypeService.saveFoodType(newFoodType)).thenReturn(foodType);

        assertEquals(foodTypeDTOComplex, foodTypeService.createFoodType(newFoodTypeDTOComplex));
    }

    @Test
    public void updateFoodType_success_withNewFoodCategory() {
        FoodType foodType = dsh.getFoodType(1L);
        FoodType updatedFoodType = FoodType.builder()
                .id(1L)
                .name("new name")
                .description("new description")
                .foodCategory(dsh.getFoodCategory(2L))
                .build();

        FoodTypeDTOComplex updatedFoodTypeDTOComplex =
                new FoodTypeDTOComplex(1L,
                        new FoodCategoryDTOSimple(2L, null, null),
                        "new name",
                        "new description");

        FoodTypeDTOComplex expectedResult = new FoodTypeDTOComplex(1L, dsh.getFoodCategoryDTOSimple(2L), "new name", "new description");

        when(foodTypeRepository.findById(1L)).thenReturn(Optional.of(foodType));
        when(foodCategoryRepository.findById(2L)).thenReturn(Optional.of(dsh.getFoodCategory(2L)));

        lenient().when(foodTypeService.saveFoodType(any(FoodType.class))).then(returnsFirstArg());
        lenient().doNothing().when(foodTypeService).evictFoodTypeListCacheForFoodCategory(1L);

        when(dtoMapper.map(any(FoodType.class), any())).thenReturn(expectedResult);

        assertEquals(expectedResult, foodTypeService.updateFoodType(1L,updatedFoodTypeDTOComplex ));
    }

    @Test
    public void updateFoodType_success_noNewFoodCategory() {
        FoodType foodType = dsh.getFoodType(1L);

        FoodTypeDTOComplex updatedFoodTypeDTOComplex =
                new FoodTypeDTOComplex(1L,
                        null,
                        "new name",
                        "new description");

        FoodTypeDTOComplex expectedResult = new FoodTypeDTOComplex(1L, dsh.getFoodCategoryDTOSimple(1L), "new name", "new description");

        when(foodTypeRepository.findById(1L)).thenReturn(Optional.of(foodType));

        when(foodTypeService.saveFoodType(any(FoodType.class))).then(returnsFirstArg());

        when(dtoMapper.map(any(FoodType.class), any())).thenReturn(expectedResult);

        assertEquals(expectedResult, foodTypeService.updateFoodType(1L,updatedFoodTypeDTOComplex ));
    }

    @Test
    public void updateFoodType_success_noNewData() {
        FoodType foodType = dsh.getFoodType(1L);

        FoodTypeDTOComplex updatedFoodTypeDTOComplex =
                new FoodTypeDTOComplex(1L,
                        null,
                        null,
                        null);

        FoodTypeDTOComplex expectedResult = dsh.getFoodTypeDTOComplex(1L);

        when(foodTypeRepository.findById(1L)).thenReturn(Optional.of(foodType));

        when(foodTypeService.saveFoodType(foodType)).thenReturn(foodType);

        when(dtoMapper.map(foodType, FoodTypeDTOComplex.class)).thenReturn(expectedResult);

        assertEquals(expectedResult, foodTypeService.updateFoodType(1L,updatedFoodTypeDTOComplex ));
    }

    @Test
    public void deleteFoodType_success() {
        FoodType foodType = dsh.getFoodType(1L);

        when(foodTypeRepository.findById(1L)).thenReturn(Optional.of(foodType));
        doNothing().when(foodTypeService).deleteFoodType(foodType);

        foodTypeService.deleteFoodType(1L);

        verify(foodTypeService, times(1)).deleteFoodType(foodType);
    }

    @Test
    public void deleteFoodType_notFound() {
        when(foodTypeRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class,() -> foodTypeService.deleteFoodType(99L));
    }

    @Test
    public void deleteFoodType_protected() {
        FoodType foodType = dsh.getFoodType(1L);

        doNothing().when(foodTypeRepository).deleteById(1L);
        doNothing().when(foodTypeService).evictFoodTypeListCacheForFoodCategory(1L);

        foodTypeService.deleteFoodType(foodType);

        verify(foodTypeService, times(1)).deleteFoodType(foodType);
        verify(foodTypeService, times(1)).evictFoodTypeListCacheForFoodCategory(1L);
    }

    @Test
    public void saveFoodType_protected() {
        FoodType foodType = dsh.getFoodType(1L);

        when(foodTypeRepository.save(any(FoodType.class))).thenReturn(foodType);

        assertEquals(foodType, foodTypeService.saveFoodType(new FoodType()));
    }

    @Test
    public void evictFoodTypeListCacheForFoodCategory_success() {
        when(cacheManager.getCache("foodTypeList")).thenReturn(foodTypeCache);
        when(cacheManager.getCache("foodCategory")).thenReturn(foodCategoryCache);

        when(foodTypeCache.evictIfPresent("foodCategoryId=1")).thenReturn(true);
        when(foodCategoryCache.evictIfPresent(1L)).thenReturn(true);

        foodTypeService.evictFoodTypeListCacheForFoodCategory(1L);

        verify(foodTypeCache, times(1)).evictIfPresent("foodCategoryId=1");
        verify(foodCategoryCache, times(1)).evictIfPresent(1L);
    }

    @Test
    void getAllFoodTypesByPrinciple() {
        UserPrinciple principle = dsh.getPrinciple("basic");
        List<FoodType> foodTypes = List.of (
                dsh.getFoodType(1L),
                dsh.getFoodType(2L),
                dsh.getFoodType(3L),
                dsh.getFoodType(4L),
                dsh.getFoodType(5L),
                dsh.getFoodType(6L),
                dsh.getFoodType(7L),
                dsh.getFoodType(8L),
                dsh.getFoodType(9L),
                dsh.getFoodType(10L),
                dsh.getFoodType(11L),
                dsh.getFoodType(12L),
                dsh.getFoodType(13L),
                dsh.getFoodType(14L),
                dsh.getFoodType(15L)
        );
        List<FoodTypeDTOComplex> foodTypeDTOComplexList = List.of (
                dsh.getFoodTypeDTOComplex(1L),
                dsh.getFoodTypeDTOComplex(2L),
                dsh.getFoodTypeDTOComplex(3L),
                dsh.getFoodTypeDTOComplex(4L),
                dsh.getFoodTypeDTOComplex(5L),
                dsh.getFoodTypeDTOComplex(6L),
                dsh.getFoodTypeDTOComplex(7L),
                dsh.getFoodTypeDTOComplex(8L),
                dsh.getFoodTypeDTOComplex(9L),
                dsh.getFoodTypeDTOComplex(10L),
                dsh.getFoodTypeDTOComplex(11L),
                dsh.getFoodTypeDTOComplex(12L),
                dsh.getFoodTypeDTOComplex(13L),
                dsh.getFoodTypeDTOComplex(14L),
                dsh.getFoodTypeDTOComplex(15L)
        );

        when(foodTypeRepository.findAllByPrincipleId(principle.getId())).thenReturn(foodTypes);

        List<FoodTypeDTOComplex> result = foodTypeService.getFoodTypesByPrinciple(principle);

        verify(foodTypeRepository,times(1)).findAllByPrincipleId(principle.getId());
    }
}