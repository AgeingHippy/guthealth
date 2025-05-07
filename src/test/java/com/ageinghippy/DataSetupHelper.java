package com.ageinghippy;

import com.ageinghippy.model.dto.*;
import com.ageinghippy.model.entity.FoodCategory;
import com.ageinghippy.model.entity.FoodType;
import com.ageinghippy.model.entity.PreparationTechnique;
import com.ageinghippy.model.entity.UserPrinciple;

import java.util.HashMap;
import java.util.Map;

public class DataSetupHelper {
    private Map<Long, UserPrinciple> userPrincipleMap = new HashMap<>();

    private Map<String, PreparationTechnique> preparationTechniqueMap = new HashMap<>();
    private Map<String, PreparationTechniqueDTO> preparationTechniqueDTOMap = new HashMap<>();

    private Map<Long, FoodCategory> foodCategoryMap = new HashMap<>();
    private Map<Long, FoodCategoryDTOSimple> foodCategoryDTOSimpleMap = new HashMap<>();
    private Map<Long, FoodCategoryDTOComplex> foodCategoryDTOComplexMap = new HashMap<>();


    private Map<Long, FoodType> foodTypeMap = new HashMap<>();
    private Map<Long, FoodTypeDTOSimple> foodTypeDTOSimpleMap = new HashMap<>();
    private Map<Long, FoodTypeDTOComplex> foodTypeDTOComplexMap = new HashMap<>();


    public DataSetupHelper() {
        initialisePrinciple();
        initialisePreparationTechniques();
        initialiseFoodCategories();
        initialiseFoodTypes();
    }

    private void initialisePreparationTechniques() {
        initialisePreparationTechnique("PrepType1","Preparation type one description");
        initialisePreparationTechnique("PrepType2","Preparation type two description");
        initialisePreparationTechnique("PrepType3","Preparation type three description");
        initialisePreparationTechnique("PrepType4","Preparation type four description");
    }

    private void initialisePreparationTechnique(String code, String description) {
        preparationTechniqueMap.put(code,
                PreparationTechnique.builder().code(code).description(description).build());

        preparationTechniqueDTOMap.put(code, new PreparationTechniqueDTO(code,description));
    }

    private void initialisePrinciple() {
        userPrincipleMap.put(1L, UserPrinciple.builder().id(1L).build());
        userPrincipleMap.put(2L, UserPrinciple.builder().id(1L).build());
        userPrincipleMap.put(3L, UserPrinciple.builder().id(1L).build());
    }

    private void initialiseFoodCategories() {
        initialiseFoodCategory(1L, 2L, "foodCategory1_name", "Food Category one description");
        initialiseFoodCategory(2L, 2L, "foodCategory2_name", "Food Category two description");
        initialiseFoodCategory(3L, 2L, "foodCategory3_name", "Food Category three description");
        initialiseFoodCategory(4L, 2L, "foodCategory4_name", "Food Category four description");
        initialiseFoodCategory(5L, 2L, "foodCategory5_name", "Food Category five description");
        initialiseFoodCategory(6L, 1L, "foodCategory6_name", "Food Category six description");
    }

    private void initialiseFoodCategory(Long id, Long userPrincipleId, String name, String description) {
        FoodCategory foodCategory = FoodCategory.builder()
                .id(id)
                .name(name)
                .description(description)
                .principle(userPrincipleMap.get(userPrincipleId))
                .build();

        FoodCategoryDTOSimple foodCategoryDTOSimple = new FoodCategoryDTOSimple(id, name, description);

        foodCategoryMap.put(id, foodCategory);
        foodCategoryDTOSimpleMap.put(id, foodCategoryDTOSimple);
    }

    private void initialiseFoodTypes() {
        initialiseFoodType(1L,1L,"foodType1","Food Type one Description");
        initialiseFoodType(2L,1L,"foodType2","Food Type two Description");
        initialiseFoodType(3L,1L,"foodType3","Food Type three Description");
        initialiseFoodType(4L,1L,"foodType4","Food Type four Description");
        initialiseFoodType(5L,1L,"foodType5","Food Type five Description");
        initialiseFoodType(6L,2L,"foodType6","Food Type six Description");
        initialiseFoodType(7L,3L,"foodType7","Food Type seven Description");
        initialiseFoodType(8L,3L,"foodType8","Food Type eight Description");
        initialiseFoodType(9L,3L,"foodType9","Food Type nine Description");
        initialiseFoodType(10L,3L,"foodType10","Food Type ten Description");
        initialiseFoodType(11L,4L,"foodType11","Food Type eleven Description");
        initialiseFoodType(12L,4L,"foodType12","Food Type twelve Description");
        initialiseFoodType(13L,4L,"foodType13","Food Type thirteen Description");
        initialiseFoodType(14L,4L,"foodType14","Food Type fourteen Description");
        initialiseFoodType(15L,4L,"foodType15","Food Type fifteen Description");
        initialiseFoodType(16L,6L,"foodType16","Food Type sixteen Description");
    }

    private void initialiseFoodType(Long id, Long foodCategoryId, String name, String description) {
        foodTypeMap.put(id,
                FoodType.builder()
                        .id(id)
                        .name(name)
                        .description(description)
                        .foodCategory(foodCategoryMap.get(foodCategoryId))
                        .build());

        foodTypeDTOSimpleMap.put(id,
                new FoodTypeDTOSimple(id, name, description));

        foodTypeDTOComplexMap.put(id,
                new FoodTypeDTOComplex(id,foodCategoryDTOSimpleMap.get(foodCategoryId),name,description));
    }

    public UserPrinciple getPrinciple(Long id) {
        return userPrincipleMap.get(id);
    }

    public PreparationTechnique getPreparationTechnique(String code) {
        return preparationTechniqueMap.get(code);
    }
    public PreparationTechniqueDTO getPreparationTechniqueDTO(String code) {
        return preparationTechniqueDTOMap.get(code);
    }

    public FoodType getFoodType(Long id) {
        return foodTypeMap.get(id);
    }

    public FoodTypeDTOSimple getFoodTypeDTOSimple(Long id) {
        return foodTypeDTOSimpleMap.get(id);
    }

    public FoodTypeDTOComplex getFoodTypeDTOComplex(Long id) {
        return foodTypeDTOComplexMap.get(id);
    }

    public FoodCategory getFoodCategory(Long id) {
        return foodCategoryMap.get(id);
    }

    public FoodCategoryDTOSimple getFoodCategoryDTOSimple(Long id) {
        return foodCategoryDTOSimpleMap.get(id);
    }

    public FoodCategoryDTOComplex getFoodCategoryDTOComplex(Long id) {
        return foodCategoryDTOComplexMap.get(id);
    }

}
