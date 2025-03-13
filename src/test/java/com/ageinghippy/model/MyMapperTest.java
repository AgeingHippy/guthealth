package com.ageinghippy.model;

import com.ageinghippy.model.dto.PreparationTechniqueDTO;
import com.ageinghippy.model.entity.PreparationTechnique;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class MyMapperTest {

    MyMapper myMapper = new MyMapper();

    @ParameterizedTest
    @MethodSource("verify_map_provider")
    <S, T> void verify_map(String testDescription, S source, T target, Class<T> classType) {

        //GIVEN an Entity

        //WHEN we map to a DTO
        T mappedTarget = myMapper.map(source, classType);

        //THEN the valid values are mapped
        assertEquals(mappedTarget, target, testDescription);
    }

    @ParameterizedTest
    @MethodSource("verify_mapList_provider")
    <S, T> void mapList(String testDescription, List<S> sourceList, List<T> targetList, Class<T> targetClassType) {

        //GIVEN a List of objects

        //WHEN we map the list
        List<T> mappedTargetList = myMapper.mapList(sourceList,targetClassType);

        //Then the elements of the list match
        assertEquals(mappedTargetList.size(), targetList.size(), testDescription);
        for (int i = 0; i < mappedTargetList.size(); i++){
            assertEquals(mappedTargetList.get(i), targetList.get(i), testDescription);
        }
    }


    private static Stream<Object[]> verify_map_provider() {
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

    private static Stream<Object[]> verify_mapList_provider() {
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

}