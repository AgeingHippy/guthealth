package com.ageinghippy.model;

import com.ageinghippy.model.dto.*;
import com.ageinghippy.model.entity.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class DTOMapperTest {

    DTOMapper DTOMapper = new DTOMapper();

    @ParameterizedTest
    @MethodSource({
//            "verify_map_provider_PreparationTechniqueDTO",
//            "verify_map_provider_FoodCategoryDTOSimple",
//            "verify_map_provider_FoodTypeDTOSimple",
//            "verify_map_provider_FoodCategoryDTOComplex",
//            "verify_map_provider_FoodTypeDTOComplex",
            "verify_map_provider_DishComponentDTO"
    })
    <S, T> void verify_map(String testDescription, S source, T target, Class<T> classType) {

        //GIVEN an Entity

        //WHEN we map to a DTO
        T mappedTarget = DTOMapper.map(source, classType);

        //THEN the valid values are mapped
        assertEquals(mappedTarget, target, testDescription);
    }

    @ParameterizedTest
    @MethodSource({
//            "verify_mapList_provider_PreparationTechniqueDTO",
//            "verify_mapList_provider_FoodCategoryDTOSimple",
//            "verify_mapList_provider_FoodTypeDTOSimple",
//            "verify_mapList_provider_FoodCategoryDTOComplex",
//            "verify_mapList_provider_FoodTypeDTOComplex",
            "verify_mapList_provider_DishComponentDTO"
    })
    <S, T> void mapList(String testDescription, List<S> sourceList, List<T> targetList, Class<T> targetClassType) {

        //GIVEN a List of objects

        //WHEN we map the list
        List<T> mappedTargetList = DTOMapper.mapList(sourceList, targetClassType);

        //Then the elements of the list match
        assertEquals(mappedTargetList.size(), targetList.size(), testDescription);
        for (int i = 0; i < mappedTargetList.size(); i++) {
            assertEquals(mappedTargetList.get(i), targetList.get(i), testDescription);
        }
    }


    private static Stream<Object[]> verify_map_provider_PreparationTechniqueDTO() {
        return Stream.of(
                //testDescription, source, target, targetClass, validationLambda
                new Object[]{
                        "Map PreparationTechnique to PreparationTechniqueDTO",
                        PreparationTechnique.builder()
                                .code("testCode")
                                .description("testDescription")
                                .build(),
                        new PreparationTechniqueDTO("testCode", "testDescription"),
                        PreparationTechniqueDTO.class
                },
                new Object[]{
                        "Map PreparationTechniqueDTO to PreparationTechnique",
                        new PreparationTechniqueDTO("testCode", "testDescription"),
                        PreparationTechnique.builder()
                                .code("testCode")
                                .description("testDescription")
                                .build(),
                        PreparationTechnique.class
                }
        );
    }

    private static Stream<Object[]> verify_mapList_provider_PreparationTechniqueDTO() {
        return Stream.of(
                //testDescription, sourceList, targetList, targetClassType
                new Object[]{
                        "Map List<PreparationTechnique> to List<PreparationTechniqueDTO>",
                        List.of(
                                PreparationTechnique.builder().code("code1").description("description1").build(),
                                PreparationTechnique.builder().code("code2").description("description2").build(),
                                PreparationTechnique.builder().code("code3").description("description3").build()
                        ),
                        List.of(new PreparationTechniqueDTO("code1", "description1"),
                                new PreparationTechniqueDTO("code2", "description2"),
                                new PreparationTechniqueDTO("code3", "description3")
                        ),
                        PreparationTechniqueDTO.class
                },
                new Object[]{
                        "Map List<PreparationTechniqueDTO> to List<PreparationTechnique>",
                        List.of(new PreparationTechniqueDTO("code1", "description1"),
                                new PreparationTechniqueDTO("code2", "description2"),
                                new PreparationTechniqueDTO("code3", "description3")
                        ),
                        List.of(
                                PreparationTechnique.builder().code("code1").description("description1").build(),
                                PreparationTechnique.builder().code("code2").description("description2").build(),
                                PreparationTechnique.builder().code("code3").description("description3").build()
                        ),
                        PreparationTechnique.class
                }
        );
    }

    private static Stream<Object[]> verify_map_provider_FoodCategoryDTOSimple() {
        return Stream.of(
                //id, name, description
                new Object[]{
                        "Map FoodCategory to FoodCategoryDTOSimple",
                        FoodCategory.builder()
                                .id(1L)
                                .name("name1")
                                .description("description1")
                                .foodTypes(
                                        List.of(FoodType.builder().id(11L).build(),
                                                FoodType.builder().id(12L).build()))
                                .build(),
                        new FoodCategoryDTOSimple(1L, "name1", "description1"),
                        FoodCategoryDTOSimple.class
                },
                new Object[]{
                        "Map List<FoodCategoryDTOSimple> to FoodCategory",
                        new FoodCategoryDTOSimple(1L, "name1", "description1"),
                        FoodCategory.builder()
                                .id(1L)
                                .name("name1")
                                .description("description1")
                                .foodTypes(null)
                                .build(),
                        FoodCategory.class
                }
        );
    }

    private static Stream<Object[]> verify_mapList_provider_FoodCategoryDTOSimple() {
        return Stream.of(
                //id, name, description
                new Object[]{
                        "Map List<FoodCategory> to List<FoodCategoryDTOSimple>",
                        List.of(
                                FoodCategory.builder().id(1L).name("name1").description("description1")
                                        .foodTypes(List.of(FoodType.builder().id(11L).build(),
                                                FoodType.builder().id(12L).build()))
                                        .build(),
                                FoodCategory.builder().id(2L).name("name2").description("description2")
                                        .foodTypes(List.of(FoodType.builder().id(13L).build()))
                                        .build(),
                                FoodCategory.builder().id(3L).name("name3").description("description3")
                                        .foodTypes(null)
                                        .build()
                        ),
                        List.of(new FoodCategoryDTOSimple(1L, "name1", "description1"),
                                new FoodCategoryDTOSimple(2L, "name2", "description2"),
                                new FoodCategoryDTOSimple(3L, "name3", "description3")
                        ),
                        FoodCategoryDTOSimple.class
                },
                new Object[]{
                        "Map List<FoodCategoryDTOSimple> to List<FoodCategory>",
                        List.of(new FoodCategoryDTOSimple(1L, "name1", "description1"),
                                new FoodCategoryDTOSimple(2L, "name2", "description2"),
                                new FoodCategoryDTOSimple(3L, "name3", "description3")
                        ),
                        List.of(
                                FoodCategory.builder().id(1L).name("name1").description("description1")
                                        .foodTypes(null)
                                        .build(),
                                FoodCategory.builder().id(2L).name("name2").description("description2")
                                        .foodTypes(null)
                                        .build(),
                                FoodCategory.builder().id(3L).name("name3").description("description3")
                                        .foodTypes(null)
                                        .build()
                        ),
                        FoodCategory.class
                }
        );
    }

    private static Stream<Object[]> verify_map_provider_FoodTypeDTOSimple() {
        return Stream.of(
                //id, name, description
                new Object[]{
                        "Map FoodType to FoodTypeDTOSimple",
                        FoodType.builder()
                                .id(1L)
                                .name("name1")
                                .description("description1")
                                .foodCategory(
                                        FoodCategory.builder().id(22L).name("FC22").description("Desc 22").build()
                                )
                                .build(),
                        new FoodTypeDTOSimple(1L, "name1", "description1"),
                        FoodTypeDTOSimple.class
                },
                new Object[]{
                        "Map List<FoodTypeDTOSimple> to FoodType",
                        new FoodTypeDTOSimple(1L, "name1", "description1"),
                        FoodType.builder()
                                .id(1L)
                                .name("name1")
                                .description("description1")
                                .foodCategory(null)
                                .build(),
                        FoodType.class
                }
        );
    }

    private static Stream<Object[]> verify_mapList_provider_FoodTypeDTOSimple() {
        return Stream.of(
                //id, name, description
                new Object[]{
                        "Map List<FoodType> to List<FoodTypeDTOSimple>",
                        List.of(
                                FoodType.builder()
                                        .id(1L)
                                        .name("name1")
                                        .description("description1")
                                        .foodCategory(
                                                FoodCategory.builder().id(22L).name("FC22").description("Desc 22").build()
                                        )
                                        .build(),
                                FoodType.builder()
                                        .id(2L)
                                        .name("name2")
                                        .description("description2")
                                        .foodCategory(
                                                FoodCategory.builder().id(33L).name("FC33").description("Desc 33").build()
                                        )
                                        .build()
                        ),
                        List.of(
                                new FoodTypeDTOSimple(1L, "name1", "description1"),
                                new FoodTypeDTOSimple(2L, "name2", "description2")
                        ),
                        FoodTypeDTOSimple.class
                },
                new Object[]{
                        "Map List<FoodTypeDTOSimple> to List<FoodType>",
                        List.of(
                                new FoodTypeDTOSimple(1L, "name1", "description1"),
                                new FoodTypeDTOSimple(2L, "name2", "description2")
                        ),
                        List.of(
                                FoodType.builder()
                                        .id(1L)
                                        .name("name1")
                                        .description("description1")
                                        .foodCategory(null)
                                        .build(),
                                FoodType.builder()
                                        .id(2L)
                                        .name("name2")
                                        .description("description2")
                                        .foodCategory(null)
                                        .build()
                        ),
                        FoodType.class
                }
        );
    }

    private static Stream<Object[]> verify_map_provider_FoodCategoryDTOComplex() {
        return Stream.of(
                //id, name, description
                new Object[]{
                        "Map FoodCategory to FoodCategoryDTOComplex",
                        FoodCategory.builder()
                                .id(1L)
                                .name("name1")
                                .description("description1")
                                .foodTypes(
                                        List.of(FoodType.builder().id(11L).name("n1").description("d1").build(),
                                                FoodType.builder().id(22L).name("n2").description("d2").build()))
                                .build(),
                        new FoodCategoryDTOComplex(1L, "name1", "description1",
                                List.of(
                                        new FoodTypeDTOSimple(11L, "n1", "d1"),
                                        new FoodTypeDTOSimple(22L, "n2", "d2")
                                )
                        ),
                        FoodCategoryDTOComplex.class
                },
                new Object[]{
                        "Map FoodCategoryDTOComplex to FoodCategory",
                        new FoodCategoryDTOComplex(1L, "name1", "description1",
                                List.of(
                                        new FoodTypeDTOSimple(11L, "n1", "d1"),
                                        new FoodTypeDTOSimple(22L, "n2", "d2")
                                )
                        ),
                        FoodCategory.builder()
                                .id(1L)
                                .name("name1")
                                .description("description1")
                                .foodTypes(
                                        List.of(FoodType.builder().id(11L).name("n1").description("d1").build(),
                                                FoodType.builder().id(22L).name("n2").description("d2").build()))
                                .build(),
                        FoodCategory.class
                }
        );
    }

    private static Stream<Object[]> verify_mapList_provider_FoodCategoryDTOComplex() {
        return Stream.of(
                //id, name, description
                new Object[]{
                        "Map List<FoodCategory> to List<FoodCategoryDTOComplex>",
                        List.of(
                                FoodCategory.builder()
                                        .id(1L)
                                        .name("name1")
                                        .description("description1")
                                        .foodTypes(
                                                List.of(FoodType.builder().id(11L).name("n1").description("d1").build(),
                                                        FoodType.builder().id(22L).name("n2").description("d2").build()))
                                        .build(),
                                FoodCategory.builder()
                                        .id(2L)
                                        .name("name2")
                                        .description("description2")
                                        .foodTypes(
                                                List.of(FoodType.builder().id(33L).name("n3").description("d3").build(),
                                                        FoodType.builder().id(44L).name("n4").description("d4").build()))
                                        .build()
                        ),
                        List.of(
                                new FoodCategoryDTOComplex(1L, "name1", "description1",
                                        List.of(
                                                new FoodTypeDTOSimple(11L, "n1", "d1"),
                                                new FoodTypeDTOSimple(22L, "n2", "d2")
                                        )
                                ),
                                new FoodCategoryDTOComplex(2L, "name2", "description2",
                                        List.of(
                                                new FoodTypeDTOSimple(33L, "n3", "d3"),
                                                new FoodTypeDTOSimple(44L, "n4", "d4")
                                        )
                                )
                        ),
                        FoodCategoryDTOComplex.class
                },
                new Object[]{
                        "Map List<FoodCategoryDTOComplex> to List<FoodCategory>",
                        List.of(
                                new FoodCategoryDTOComplex(1L, "name1", "description1",
                                        List.of(
                                                new FoodTypeDTOSimple(11L, "n1", "d1"),
                                                new FoodTypeDTOSimple(22L, "n2", "d2")
                                        )
                                ),
                                new FoodCategoryDTOComplex(2L, "name2", "description2",
                                        List.of(
                                                new FoodTypeDTOSimple(33L, "n3", "d3"),
                                                new FoodTypeDTOSimple(44L, "n4", "d4")
                                        )
                                )
                        ),
                        List.of(
                                FoodCategory.builder()
                                        .id(1L)
                                        .name("name1")
                                        .description("description1")
                                        .foodTypes(
                                                List.of(
                                                        FoodType.builder().id(11L).name("n1").description("d1").build(),
                                                        FoodType.builder().id(22L).name("n2").description("d2").build()
                                                ))
                                        .build(),
                                FoodCategory.builder()
                                        .id(2L)
                                        .name("name2")
                                        .description("description2")
                                        .foodTypes(
                                                List.of(
                                                        FoodType.builder().id(33L).name("n3").description("d3").build(),
                                                        FoodType.builder().id(44L).name("n4").description("d4").build()
                                                ))
                                        .build()
                        ),
                        FoodCategory.class
                }
        );
    }

    private static Stream<Object[]> verify_map_provider_FoodTypeDTOComplex() {
        return Stream.of(
                //id, name, description
                new Object[]{
                        "Map FoodType to FoodTypeDTOComplex",
                        FoodType.builder()
                                .id(1L)
                                .name("name1")
                                .description("description1")
                                .foodCategory(
                                        FoodCategory.builder().id(22L).name("FC22").description("Desc 22").build()
                                )
                                .build(),
                        new FoodTypeDTOComplex(1L,
                                new FoodCategoryDTOSimple(22L, "FC22", "Desc 22"),
                                "name1",
                                "description1"),
                        FoodTypeDTOComplex.class
                },
                new Object[]{
                        "Map FoodTypeDTOComplex to FoodType",
                        new FoodTypeDTOComplex(1L,
                                new FoodCategoryDTOSimple(22L, "FC22", "Desc 22"),
                                "name1",
                                "description1"),
                        FoodType.builder()
                                .id(1L)
                                .name("name1")
                                .description("description1")
                                .foodCategory(
                                        FoodCategory.builder().id(22L).name("FC22").description("Desc 22").build()
                                )
                                .build(),
                        FoodType.class
                }
        );
    }

    private static Stream<Object[]> verify_mapList_provider_FoodTypeDTOComplex() {
        return Stream.of(
                //id, name, description
                new Object[]{
                        "Map FoodType to FoodTypeDTOComplex",
                        List.of(
                                FoodType.builder()
                                        .id(1L)
                                        .name("name1")
                                        .description("description1")
                                        .foodCategory(
                                                FoodCategory.builder().id(11L).name("FC11").description("Desc 11").build()
                                        )
                                        .build(),
                                FoodType.builder()
                                        .id(2L)
                                        .name("name2")
                                        .description("description2")
                                        .foodCategory(
                                                FoodCategory.builder().id(22L).name("FC22").description("Desc 22").build()
                                        )
                                        .build(),
                                FoodType.builder()
                                        .id(3L)
                                        .name("name3")
                                        .description("description3")
                                        .build()
                        ),
                        List.of(
                                new FoodTypeDTOComplex(1L,
                                        new FoodCategoryDTOSimple(11L, "FC11", "Desc 11"),
                                        "name1",
                                        "description1"),
                                new FoodTypeDTOComplex(2L,
                                        new FoodCategoryDTOSimple(22L, "FC22", "Desc 22"),
                                        "name2",
                                        "description2"),
                                new FoodTypeDTOComplex(3L,
                                        null,
                                        "name3",
                                        "description3")
                        ),
                        FoodTypeDTOComplex.class
                },
                new Object[]{
                        "Map FoodTypeDTOComplex to FoodType",
                        List.of(
                                new FoodTypeDTOComplex(1L,
                                        new FoodCategoryDTOSimple(11L, "FC11", "Desc 11"),
                                        "name1",
                                        "description1"),
                                new FoodTypeDTOComplex(2L,
                                        new FoodCategoryDTOSimple(22L, "FC22", "Desc 22"),
                                        "name2",
                                        "description2"),
                                new FoodTypeDTOComplex(3L,
                                        null,
                                        "name3",
                                        "description3")
                        ),
                        List.of(
                                FoodType.builder()
                                        .id(1L)
                                        .name("name1")
                                        .description("description1")
                                        .foodCategory(
                                                FoodCategory.builder().id(11L).name("FC11").description("Desc 11").build()
                                        )
                                        .build(),
                                FoodType.builder()
                                        .id(2L)
                                        .name("name2")
                                        .description("description2")
                                        .foodCategory(
                                                FoodCategory.builder().id(22L).name("FC22").description("Desc 22").build()
                                        )
                                        .build(),
                                FoodType.builder()
                                        .id(3L)
                                        .name("name3")
                                        .description("description3")
                                        .build()
                        ),
                        FoodType.class
                }
        );
    }

    private static Stream<Object[]> verify_map_provider_DishComponentDTO() {
        return Stream.of(
                //id, name, description
                new Object[]{
                        "Map DishComponent to DishComponentDTO",
                        DishComponent.builder()
                                .id(1L)
                                .dish(Dish.builder().id(11L).build())
                                .foodType(FoodType.builder()
                                        .id(101L)
                                        .name("foodType101")
                                        .description("foodTypeDesc101")
                                        .foodCategory(FoodCategory.builder().id(111L).build())
                                        .build())
                                .proportion(100)
                                .build(),
                        new DishComponentDTO(1L,
                                new FoodTypeDTOSimple(101L, "foodType101", "foodTypeDesc101"),
                                100),
                        DishComponentDTO.class
                },
                new Object[]{
                        "Map DishComponentDTO to DishComponent",
                        new DishComponentDTO(1L,
                                new FoodTypeDTOSimple(101L, "foodType101", "foodTypeDesc101"),
                                100),
                        DishComponent.builder()
                                .id(1L)
                                .dish(null)
                                .foodType(FoodType.builder()
                                        .id(101L)
                                        .name("foodType101")
                                        .description("foodTypeDesc101")
                                        .foodCategory(null)
                                        .build())
                                .proportion(100)
                                .build(),
                        DishComponent.class
                }
        );
    }

    private static Stream<Object[]> verify_mapList_provider_DishComponentDTO() {
        return Stream.of(
                //id, name, description
                new Object[]{
                        "Map DishComponent to DishComponentDTO",
                        List.of(
                                DishComponent.builder()
                                        .id(1L)
                                        .dish(Dish.builder().id(11L).build())
                                        .foodType(FoodType.builder()
                                                .id(101L)
                                                .name("foodType101")
                                                .description("foodTypeDesc101")
                                                .foodCategory(FoodCategory.builder().id(111L).build())
                                                .build())
                                        .proportion(100)
                                        .build(),
                                DishComponent.builder()
                                        .id(2L)
                                        .dish(Dish.builder().id(11L).build())
                                        .foodType(FoodType.builder()
                                                .id(202L)
                                                .name("foodType202")
                                                .description("foodTypeDesc202")
                                                .foodCategory(FoodCategory.builder().id(222L).build())
                                                .build())
                                        .proportion(200)
                                        .build(),
                                DishComponent.builder()
                                        .id(3L)
                                        .dish(Dish.builder().id(11L).build())
                                        .foodType(FoodType.builder()
                                                .id(303L)
                                                .name("foodType303")
                                                .description("foodTypeDesc303")
                                                .foodCategory(FoodCategory.builder().id(333L).build())
                                                .build())
                                        .proportion(300)
                                        .build()
                        ),
                        List.of(
                                new DishComponentDTO(1L,
                                        new FoodTypeDTOSimple(101L, "foodType101", "foodTypeDesc101"),
                                        100),
                                new DishComponentDTO(2L,
                                        new FoodTypeDTOSimple(202L, "foodType202", "foodTypeDesc202"),
                                        200),
                                new DishComponentDTO(3L,
                                        new FoodTypeDTOSimple(303L, "foodType303", "foodTypeDesc303"),
                                        300)
                        ),
                        DishComponentDTO.class
                },
                new Object[]{
                        "Map DishComponentDTO to DishComponent",
                        List.of(
                                new DishComponentDTO(1L,
                                        new FoodTypeDTOSimple(101L, "foodType101", "foodTypeDesc101"),
                                        100),
                                new DishComponentDTO(2L,
                                        new FoodTypeDTOSimple(202L, "foodType202", "foodTypeDesc202"),
                                        200),
                                new DishComponentDTO(3L,
                                        new FoodTypeDTOSimple(303L, "foodType303", "foodTypeDesc303"),
                                        300)
                        ),
                        List.of(
                                DishComponent.builder()
                                        .id(1L)
                                        .dish(null)
                                        .foodType(FoodType.builder()
                                                .id(101L)
                                                .name("foodType101")
                                                .description("foodTypeDesc101")
                                                .foodCategory(null)
                                                .build())
                                        .proportion(100)
                                        .build(),
                                DishComponent.builder()
                                        .id(2L)
                                        .dish(null)
                                        .foodType(FoodType.builder()
                                                .id(202L)
                                                .name("foodType202")
                                                .description("foodTypeDesc202")
                                                .foodCategory(null)
                                                .build())
                                        .proportion(200)
                                        .build(),
                                DishComponent.builder()
                                        .id(3L)
                                        .dish(null)
                                        .foodType(FoodType.builder()
                                                .id(303L)
                                                .name("foodType303")
                                                .description("foodTypeDesc303")
                                                .foodCategory(null)
                                                .build())
                                        .proportion(300)
                                        .build()
                        ),
                        DishComponent.class
                }
        );
    }
}