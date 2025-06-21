package com.ageinghippy;

import com.ageinghippy.model.dto.*;
import com.ageinghippy.model.entity.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

public class DataSetupHelper {
    private Map<Long, Role> roleMap = new HashMap<>();

    private Map<Long, UserPrinciple> userPrincipleMap = new HashMap<>();
    private Map<Long, UserPrincipleDTOSimple> userPrincipleDTOSimpleMap = new HashMap<>();

    private Map<Long, PreparationTechnique> preparationTechniqueMap = new HashMap<>();
    private Map<Long, PreparationTechniqueDTO> preparationTechniqueDTOMap = new HashMap<>();

    private Map<Long, FoodCategory> foodCategoryMap = new HashMap<>();
    private Map<Long, FoodCategoryDTOSimple> foodCategoryDTOSimpleMap = new HashMap<>();
    private Map<Long, FoodCategoryDTOComplex> foodCategoryDTOComplexMap = new HashMap<>();

    private Map<Long, FoodType> foodTypeMap = new HashMap<>();
    private Map<Long, FoodTypeDTOSimple> foodTypeDTOSimpleMap = new HashMap<>();
    private Map<Long, FoodTypeDTOComplex> foodTypeDTOComplexMap = new HashMap<>();

    private Map<Long, Dish> dishMap = new HashMap<>();
    private Map<Long, DishDTOSimple> dishDTOSimpleMap = new HashMap<>();
    private Map<Long, DishDTOComplex> dishDTOComplexMap = new HashMap<>();

    private Map<Long, Meal> mealMap = new HashMap<>();
    private Map<Long, MealDTOSimple> mealDTOSimpleMap = new HashMap<>();
    private Map<Long, MealDTOComplex> mealDTOComplexMap = new HashMap<>();

    private Map<Long, MealComponent> mealComponentMap = new HashMap<>();
    private Map<Long, MealComponentDTO> mealComponentDTOMap = new HashMap<>();

    public DataSetupHelper() {
        initialiseRoles();
        initialiseUserPrinciples();
        initialisePreparationTechniques();
        initialiseFoodCategories();
        initialiseFoodTypes();
        initialiseDishes();
        initialiseMeals();
    }

    public Role getRole(Long id) {
        return roleMap.get(id);
    }

    public Role getRole(String authority) {
        return roleMap.values().stream()
                .filter(role -> authority.equals(role.getAuthority()))
                .findFirst()
                .orElse(null);
    }

    public UserPrinciple getPrinciple(Long id) {
        return userPrincipleMap.get(id);
    }

    public UserPrinciple getPrinciple(String username) {
        return userPrincipleMap.values().stream()
                .filter(principle -> username.equals(principle.getUsername()))
                .findFirst()
                .orElse(null);
    }

    public UserPrincipleDTOSimple getPrincipleDTOSimple(Long id) {
        return userPrincipleDTOSimpleMap.get(id);
    }

    public PreparationTechnique getPreparationTechnique(Long id) {
        return preparationTechniqueMap.get(id);
    }

    public PreparationTechniqueDTO getPreparationTechniqueDTO(Long id) {
        return preparationTechniqueDTOMap.get(id);
    }

    public FoodType getFoodType(Long id) {
        return foodTypeMap.get(id);
    }

    public FoodTypeDTOSimple getFoodTypeDTOSimple(Long id) {
        return foodTypeDTOSimpleMap.get(id);
    }

    private List<FoodTypeDTOSimple> getFoodTypeDTOSimpleList(Long foodCategoryId) {
        List<Long> foodTypeIds = foodTypeMap.values().stream()
                .filter(foodType -> foodCategoryId.equals(foodType.getFoodCategory().getId()))
                .map(FoodType::getId).toList();

        return foodTypeDTOSimpleMap
                .values()
                .stream()
                .filter(ft -> foodTypeIds.contains(ft.id()))
                .toList();
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

    public Dish getDish(Long id) {
        return dishMap.get(id);
    }

    public DishDTOSimple getDishDTOSimple(Long id) {
        return dishDTOSimpleMap.get(id);
    }

    public DishDTOComplex getDishDTOComplex(Long id) {
        return dishDTOComplexMap.get(id);
    }

    public Meal getMeal(Long id) {
        return mealMap.get(id);
    }

    public MealDTOSimple getMealDTOSimple(Long id) {
        return mealDTOSimpleMap.get(id);
    }

    public MealDTOComplex getMealDTOComplex(long id) {
        return mealDTOComplexMap.get(id);
    }

    //********************************** DATA INITIALISATION ROUTINES *****************************
    private void initialiseRoles() {
        initialiseRole(1L, "ROLE_ADMIN");
        initialiseRole(2L, "ROLE_USER");
        initialiseRole(3L, "ROLE_GUEST");
    }

    private void initialiseRole(Long id, String authority) {
        roleMap.put(id, Role.builder().id(id).authority(authority).build());
    }

    private void initialiseUserPrinciples() {
        initialiseUserPrinciple(1L, "admin", "adminName", List.of(getRole("ROLE_ADMIN")));
        initialiseUserPrinciple(2L, "basic", "basicName", List.of(getRole("ROLE_GUEST"), getRole("ROLE_USER")));
        initialiseUserPrinciple(3L, "guest", "guestName", List.of(getRole("ROLE_GUEST")));
        initialiseUserPrinciple(4L, "system", "systemName", List.of(getRole("ROLE_USER")));
        initialiseUserPrinciple(5L, "alternative", "alternativeName", List.of(getRole("ROLE_USER")));
    }

    private void initialiseUserPrinciple(Long id, String userName, String name, List<Role> authorities) {
        userPrincipleMap.put(id,
                UserPrinciple.builder()
                        .id(id)
                        .username(userName)
                        .authorities(new ArrayList<>(authorities))
                        .userMeta(new UserMeta(id, name, null, null))
                        .build()
        );
        userPrincipleDTOSimpleMap.put(id, new UserPrincipleDTOSimple(id, userName, name));
    }

    private void initialisePreparationTechniques() {
        initialisePreparationTechnique(1L, 2L, "PrepType1", "Preparation type one description");
        initialisePreparationTechnique(2L, 2L, "PrepType2", "Preparation type two description");
        initialisePreparationTechnique(3L, 2L, "PrepType3", "Preparation type three description");
        initialisePreparationTechnique(4L, 2L, "PrepType4", "Preparation type four description");

        initialisePreparationTechnique(5L, 4L, "PrepType5", "Preparation type five description");
        initialisePreparationTechnique(6L, 4L, "PrepType6", "Preparation type six description");
    }

    private void initialisePreparationTechnique(Long id, Long principleId, String code, String description) {
        preparationTechniqueMap.put(id,
                PreparationTechnique.builder()
                        .id(id)
                        .principle(userPrincipleMap.get(principleId))
                        .code(code)
                        .description(description)
                        .build());

        preparationTechniqueDTOMap.put(id, new PreparationTechniqueDTO(id, code, description));
    }

    private void initialiseFoodCategories() {
        initialiseFoodCategory(1L, 2L, "foodCategory1_name", "Food Category one description");
        initialiseFoodCategory(2L, 2L, "foodCategory2_name", "Food Category two description");
        initialiseFoodCategory(3L, 2L, "foodCategory3_name", "Food Category three description");
        initialiseFoodCategory(4L, 2L, "foodCategory4_name", "Food Category four description");
        initialiseFoodCategory(5L, 2L, "foodCategory5_name", "Food Category five description");

        initialiseFoodCategory(6L, 4L, "foodCategory6_name", "Food Category six description");
        initialiseFoodCategory(7L, 4L, "foodCategory7_name", "Food Category seven description");
        initialiseFoodCategory(8L, 4L, "foodCategory8_name", "Food Category eight description");
        initialiseFoodCategory(9L, 4L, "foodCategory9_name", "Food Category nine description");
    }

    //Must be initialised AFTER foodTypes have been initialised as foodTypeDTOComplexMap is immutable
    private void initialiseFoodCategoryDTOComplexMap() {
        foodCategoryMap.values().forEach(foodCategory ->
                foodCategoryDTOComplexMap.put(
                        foodCategory.getId(),
                        new FoodCategoryDTOComplex(
                                foodCategory.getId(),
                                foodCategory.getName(),
                                foodCategory.getDescription(),
                                getFoodTypeDTOSimpleList(foodCategory.getId()))
                )
        );
    }

    private void populateFoodCategoriesWithFoodTypes() {
        foodCategoryMap.values().forEach(foodCategory -> {
            foodTypeMap.values()
                    .stream()
                    .filter(foodType -> Objects.equals(foodType.getFoodCategory().getId(), foodCategory.getId()))
                    .forEach(foodType -> foodCategory.getFoodTypes().add(foodType));
        });
    }

    private void initialiseFoodCategory(Long id, Long userPrincipleId, String name, String description) {
        foodCategoryMap.put(
                id,
                FoodCategory.builder()
                        .id(id)
                        .name(name)
                        .description(description)
                        .principle(userPrincipleMap.get(userPrincipleId))
                        .foodTypes(new ArrayList<FoodType>())
                        .build()
        );

        foodCategoryDTOSimpleMap.put(
                id,
                new FoodCategoryDTOSimple(id, name, description)
        );

        //foodTypeDTOComplexMap contains foodTypes which have not yet been initialised.
    }

    private void initialiseFoodTypes() {
        initialiseFoodType(1L, 1L, "foodType1", "Food Type one Description");
        initialiseFoodType(2L, 1L, "foodType2", "Food Type two Description");
        initialiseFoodType(3L, 1L, "foodType3", "Food Type three Description");
        initialiseFoodType(4L, 1L, "foodType4", "Food Type four Description");
        initialiseFoodType(5L, 1L, "foodType5", "Food Type five Description");
        initialiseFoodType(6L, 2L, "foodType6", "Food Type six Description");
        initialiseFoodType(7L, 3L, "foodType7", "Food Type seven Description");
        initialiseFoodType(8L, 3L, "foodType8", "Food Type eight Description");
        initialiseFoodType(9L, 3L, "foodType9", "Food Type nine Description");
        initialiseFoodType(10L, 3L, "foodType10", "Food Type ten Description");
        initialiseFoodType(11L, 4L, "foodType11", "Food Type eleven Description");
        initialiseFoodType(12L, 4L, "foodType12", "Food Type twelve Description");
        initialiseFoodType(13L, 4L, "foodType13", "Food Type thirteen Description");
        initialiseFoodType(14L, 4L, "foodType14", "Food Type fourteen Description");
        initialiseFoodType(15L, 4L, "foodType15", "Food Type fifteen Description");
        initialiseFoodType(16L, 6L, "foodType16", "Food Type sixteen Description");
        initialiseFoodType(17L, 7L, "foodType17", "Food Type seventeen Description");
        initialiseFoodType(18L, 7L, "foodType18", "Food Type eighteen Description");
        initialiseFoodType(19L, 8L, "foodType19", "Food Type nineteen Description");
        initialiseFoodType(20L, 8L, "foodType20", "Food Type twenty Description");
        initialiseFoodType(21L, 8L, "foodType21", "Food Type twentyOne Description");

        initialiseFoodCategoryDTOComplexMap();
        populateFoodCategoriesWithFoodTypes();
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
                new FoodTypeDTOComplex(id, foodCategoryDTOSimpleMap.get(foodCategoryId), name, description));
    }

    private void initialiseDishes() {
        initialiseDish(1L, 2L, "Dish1", "Dish one Description", 1L);
        initialiseDish(2L, 2L, "Dish2", "Dish two Description", 1L);
        initialiseDish(3L, 2L, "Dish3", "Dish three Description", 2L);
        initialiseDish(4L, 2L, "Dish4", "Dish four Description", 3L);
    }

    private void initialiseDish(Long id, Long userPrincipleId, String name,
                                String description, Long preparationTechniqueId) {
        dishMap.put(id,
                Dish.builder()
                        .id(id)
                        .principle(userPrincipleMap.get(userPrincipleId))
                        .name(name)
                        .description(description)
                        .preparationTechnique(preparationTechniqueMap.get(preparationTechniqueId))
                        .dishComponents(new ArrayList<>()) //todo
                        .build());

        dishDTOSimpleMap.put(id,
                new DishDTOSimple(id, name, description, preparationTechniqueDTOMap.get(preparationTechniqueId)));

        dishDTOComplexMap.put(id,
                new DishDTOComplex(id, name, description,
                        preparationTechniqueDTOMap.get(preparationTechniqueId),
                        List.of())); //todo
    }

    private void initialiseMeals() {
        initialiseMeal(1L, 2L, "Meal one description", LocalDate.parse("2025-06-17"), LocalTime.parse("09:30"));
        initialiseMeal(2L, 2L, "Meal two description", LocalDate.parse("2025-06-17"), LocalTime.parse("13:30"));
        initialiseMeal(3L, 2L, "Meal three description", LocalDate.parse("2025-06-18"), LocalTime.parse("09:00"));
        initialiseMeal(4L, 2L, "Meal four description", LocalDate.parse("2025-06-17"), LocalTime.parse("14:00"));

        initialiseMealComponents();

        addMealComponentsToMeals();

        initialiseMealDTOMaps();
    }

    private void initialiseMeal(Long id, Long userPrincipleId, String description, LocalDate date, LocalTime time) {
        mealMap.put(id,
                Meal.builder()
                        .id(id)
                        .principle(userPrincipleMap.get(userPrincipleId))
                        .description(description)
                        .date(date)
                        .time(time)
                        .mealComponents(new ArrayList<>())
                        .build()
        );
    }

    private void initialiseMealComponents() {
        initialiseMealComponent(1L, 1L, 1L, 1L, 100);
        initialiseMealComponent(2L, 1L, 2L, 1L, 100);
        initialiseMealComponent(3L, 1L, 3L, 1L, 1);

        initialiseMealComponent(4L, 2L, 3L, 2L, 200);
        initialiseMealComponent(5L, 2L, 4L, 2L, 200);

        initialiseMealComponent(6L, 3L, 5L, 3L, 100);
        initialiseMealComponent(7L, 3L, 6L, 4L, 200);
    }

    private void initialiseMealComponent(Long id, Long mealId, Long foodTypeId, Long preparationTechniqueId, int volume) {
        mealComponentMap.put(id,
                MealComponent.builder()
                        .id(id)
                        .meal(mealMap.get(mealId))
                        .foodType(foodTypeMap.get(foodTypeId))
                        .preparationTechnique(preparationTechniqueMap.get(preparationTechniqueId))
                        .volume(volume)
                        .build()
        );

        mealComponentDTOMap.put(id,
                new MealComponentDTO(
                        id,
                        foodTypeDTOSimpleMap.get(foodTypeId),
                        preparationTechniqueDTOMap.get(preparationTechniqueId),
                        volume));
    }

    private void addMealComponentsToMeals() {
        mealMap.values().forEach(meal -> {
            mealComponentMap.values()
                    .stream()
                    .filter(mealComponent -> Objects.equals(mealComponent.getMeal().getId(), meal.getId()))
                    .forEach(mealComponent -> meal.getMealComponents().add(mealComponent));
        });
    }

    private void initialiseMealDTOMaps() {
        mealMap.values().forEach(meal -> {
            mealDTOSimpleMap.put(meal.getId(), new MealDTOSimple(meal.getId(), meal.getDescription(), meal.getDate(), meal.getTime()));

            List<MealComponentDTO> mealComponentDTOList = new ArrayList<>();
            meal.getMealComponents().forEach(mealComponent -> {
                mealComponentDTOList.add( mealComponentDTOMap.get(mealComponent.getId()));
            });

            mealDTOComplexMap.put(meal.getId(),
                    new MealDTOComplex(meal.getId(), meal.getDescription(), meal.getDate(), meal.getTime(), mealComponentDTOList));
        });
    }


}
