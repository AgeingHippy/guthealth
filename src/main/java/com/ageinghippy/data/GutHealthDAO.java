package com.ageinghippy.data;

import com.ageinghippy.data.model.*;

import java.sql.*;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.NoSuchElementException;

/**
 * Data Access Object for the gutHealth database
 */
public class GutHealthDAO {
    private static final String server = "localhost";
    private static final String dbURL = "jdbc:mysql://{0}/gutHealth?user={1}&password={2}&useSSL=false";
    private static final String username = "user";
    private static final String password = "pass";

    private static final String SQL_GET_FOOD_CATEGORIES = "SELECT * FROM food_category %s";
    private static final String SQL_GET_FOOD_TYPES = "SELECT * FROM food_type %s";
    private static final String SQL_GET_PREPARATION_TECHNIQUES = "SELECT * FROM preparation_technique %s";
    private static final String SQL_GET_DISHES = "SELECT * FROM dish %s";
    private static final String SQL_GET_DISH_COMPONENTS = "SELECT * FROM dish_component %s";
    private static final String SQL_GET_MEALS = "SELECT * FROM meal %s";
    private static final String SQL_GET_MEAL_COMPONENTS = "SELECT * FROM meal_component %s";

    private static final String SQL_INSERT_FOOD_CATEGORY = """
            INSERT
            INTO    food_category
                (name, description)
            VALUES (?, ?)""";
    private static final String SQL_INSERT_FOOD_TYPE = """
            INSERT
            INTO    food_type
                (food_category_id, name, description)
            VALUES (?, ?, ?)""";
    private static final String SQL_INSERT_PREPARATION_TECHNIQUE = """
            INSERT
            INTO    preparation_technique
                (code, description)
            VALUES (?, ?)""";
    private static final String SQL_INSERT_DISH = """
            INSERT
            INTO    dish
                (name, description, preparation_technique_code)
            VALUES (?, ?, ?)""";
    private static final String SQL_INSERT_DISH_COMPONENT = """
            INSERT
            INTO    dish_component
                (dish_id, food_type_id, proportion)
            VALUES (?, ?, ?)""";
    private static final String SQL_INSERT_MEAL = """
            INSERT
            INTO    meal
                (date, time)
            VALUES (?, ?)""";
    private static final String SQL_INSERT_MEAL_COMPONENT = """
            INSERT
            INTO    meal_component
                (meal_id, food_type_id, preparation_technique_code, volume)
            VALUES (?, ?, ?, ?)""";

    private static final String SQL_UPDATE_FOOD_CATEGORY = """
            UPDATE  food_category
               SET  name = ?
                  , description = ?
            WHERE   id = ?""";
    private static final String SQL_UPDATE_FOOD_TYPE = """
            UPDATE  food_type
               SET  food_category_id = ?
                  , name = ?
                  , description = ?
            WHERE   id = ?""";
    private static final String SQL_UPDATE_PREPARATION_TECHNIQUE = """
            UPDATE  preparation_technique
               SET  description = ?
            WHERE   code = ?""";
    private static final String SQL_UPDATE_DISH = """
            UPDATE  dish
               SET  name = ?
                  , description = ?
                  , preparation_technique_code = ?
            WHERE   id = ?""";
    private static final String SQL_UPDATE_DISH_COMPONENT = """
            UPDATE  dish_component
               SET  dish_id = ?
                  , food_type_id = ?
                  , proportion = ?
            WHERE   id = ?""";
    private static final String SQL_UPDATE_MEAL = """
            UPDATE  meal
               SET  date = ?
                  , time = ?
            WHERE   id = ?""";
    private static final String SQL_UPDATE_MEAL_COMPONENT = """
            UPDATE  meal_component
               SET  meal_id = ?
                  , food_type_id = ?
                  , preparation_technique_code = ?
                  , volume = ?
            WHERE   id = ?""";

    private static final String SQL_DELETE_FOOD_CATEGORY = "DELETE FROM food_category WHERE id = ?";
    private static final String SQL_DELETE_FOOD_TYPE = "DELETE FROM food_type WHERE id = ?";
    private static final String SQL_DELETE_PREPARATION_TECHNIQUE = "DELETE FROM preparation_technique WHERE code = ?";
    private static final String SQL_DELETE_DISH = "DELETE FROM dish WHERE id = ?";
    private static final String SQL_DELETE_DISH_COMPONENT = "DELETE FROM dish_component WHERE id = ?";
    private static final String SQL_DELETE_MEAL = "DELETE FROM meal WHERE id = ?";
    private static final String SQL_DELETE_MEAL_COMPONENT = "DELETE FROM meal_component WHERE id = ?";

    private final Connection connection;

    public GutHealthDAO() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(MessageFormat.format(dbURL, server, username, password));
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }


    /**
     * Fetch all matching {@code FoodCategory} records from the database
     *
     * @param whereClause - a String containing an optional where clause including the word WHERE
     * @return an {@code Arraylist} of {@code FoodCategory} objects
     */
    public ArrayList<FoodCategory> getFoodCategories(String whereClause) {
        ArrayList<FoodCategory> al = new ArrayList<>();
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(String.format(SQL_GET_FOOD_CATEGORIES, whereClause));
            while (rs.next()) {
                al.add(new FoodCategory(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("description")
                ));

            }
        } catch (SQLException e) {
            System.err.println("Exception occurred while attempting to fetch FoodCategory");
            throw new RuntimeException(e);
        }
        return al;
    }

    public ArrayList<FoodCategory> getFoodCategories() {
        return getFoodCategories("");
    }

    public FoodCategory getFoodCategory(int id) {
        FoodCategory foodCategory;
        try {
            foodCategory = getFoodCategories(String.format("WHERE id = %s", id)).getFirst();
        } catch (NoSuchElementException e) {
            //element does not exist so we return null
            foodCategory = null;
        }
        return foodCategory;
    }

    /**
     * Fetch all matching {@code FoodType} records from the database
     *
     * @param whereClause - a String containing an optional where clause including the word WHERE
     * @return an {@code Arraylist} of {@code FoodType} objects
     */
    public ArrayList<FoodType> getFoodTypes(String whereClause) {
        ArrayList<FoodType> al = new ArrayList<>();
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(String.format(SQL_GET_FOOD_TYPES, whereClause));
            while (rs.next()) {
                al.add(new FoodType(
                        rs.getInt("id"),
                        rs.getInt("food_category_id"),
                        rs.getString("name"),
                        rs.getString("description")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Exception occurred while attempting to fetch FoodType");
            throw new RuntimeException(e);
        }
        return al;
    }

    public ArrayList<FoodType> getFoodTypes() {
        return getFoodTypes("");
    }

    public FoodType getFoodType(int id) {
        FoodType foodType;
        try {
            foodType = getFoodTypes(String.format("WHERE id = %d", id)).getFirst();
        } catch (NoSuchElementException e) {
            //element does not exist so we return null
            foodType = null;
        }
        return foodType;
    }

    /**
     * Fetch all matching {@code PreparationTechnique} records from the database
     *
     * @param whereClause - a String containing an optional where clause including the word WHERE
     * @return an {@code Arraylist} of {@code PreparationTechnique} objects
     */
    public ArrayList<PreparationTechnique> getPreparationTechniques(String whereClause) {
        ArrayList<PreparationTechnique> al = new ArrayList<>();
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(String.format(SQL_GET_PREPARATION_TECHNIQUES, whereClause));
            while (rs.next()) {
                al.add(new PreparationTechnique(
                        rs.getString("code"),
                        rs.getString("description")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Exception occurred while attempting to fetch PreparationTechnique");
            throw new RuntimeException(e);
        }
        return al;
    }

    public ArrayList<PreparationTechnique> getPreparationTechniques() {
        return getPreparationTechniques("");
    }

    public PreparationTechnique getPreparationTechnique(String code) {
        PreparationTechnique preparationTechnique;
        try {
            preparationTechnique = getPreparationTechniques(String.format("WHERE code = '%s'", code)).getFirst();
        } catch (NoSuchElementException e) {
            //element does not exist so we return null
            preparationTechnique = null;
        }
        return preparationTechnique;
    }

    /**
     * Fetch all matching {@code Meal} records from the database
     *
     * @param whereClause - a String containing an optional where clause including the word WHERE
     * @return an {@code Arraylist} of {@code Meal} objects
     */
    public ArrayList<Meal> getMeals(String whereClause) {
        ArrayList<Meal> al = new ArrayList<>();
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(String.format(SQL_GET_MEALS, whereClause));
            while (rs.next()) {
                al.add(new Meal(
                        rs.getInt("id"),
                        rs.getDate("date").toLocalDate(),
                        rs.getTime("time").toLocalTime()
                ));
            }
        } catch (SQLException e) {
            System.err.println("Exception occurred while attempting to fetch Meal");
            throw new RuntimeException(e);
        }
        return al;
    }

    public ArrayList<Meal> getMeals() {
        return getMeals("");
    }

    public Meal getMeal(int id) {
        Meal meal;
        try {
            meal = getMeals(String.format("WHERE id = %d", id)).getFirst();
        } catch (NoSuchElementException e) {
            //element does not exist so we return null
            meal = null;
        }
        return meal;
    }

    /**
     * Fetch all matching {@code MealComponent} records from the database
     *
     * @param whereClause - a String containing an optional where clause including the word WHERE
     * @return an {@code Arraylist} of {@code MealComponent} objects
     */
    public ArrayList<MealComponent> getMealComponents(String whereClause) {
        ArrayList<MealComponent> al = new ArrayList<>();
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(String.format(SQL_GET_MEAL_COMPONENTS, whereClause));
            while (rs.next()) {
                al.add(new MealComponent(
                        rs.getInt("id"),
                        rs.getInt("meal_id"),
                        rs.getInt("foor_type_id"),
                        rs.getString("preparation_technique_code"),
                        rs.getInt("volume")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Exception occurred while attempting to fetch MealComponent");
            throw new RuntimeException(e);
        }
        return al;
    }

    public ArrayList<MealComponent> getMealComponents() {
        return getMealComponents("");
    }

    public MealComponent getMealComponent(int id) {
        MealComponent mealComponent;
        try {
            mealComponent = getMealComponents(String.format("WHERE id = %d", id)).getFirst();
        } catch (NoSuchElementException e) {
            //element does not exist so we return null
            mealComponent = null;
        }
        return mealComponent;
    }

    /**
     * Fetch all matching {@code Dish} records from the database
     *
     * @param whereClause - a String containing an optional where clause including the word WHERE
     * @return an {@code Arraylist} of {@code Dish} objects
     */
    public ArrayList<Dish> getDishes(String whereClause) {
        ArrayList<Dish> al = new ArrayList<>();
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(String.format(SQL_GET_DISHES, whereClause));
            while (rs.next()) {
                al.add(new Dish(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getString("preparation_technique_code")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Exception occurred while attempting to fetch Dish");
            throw new RuntimeException(e);
        }
        return al;
    }

    public ArrayList<Dish> getDishes() {
        return getDishes("");
    }

    public Dish getDish(int id) {
        Dish dish;
        try {
            dish = getDishes(String.format("WHERE id = %d", id)).getFirst();
        } catch (NoSuchElementException e) {
            //element does not exist so we return null
            dish = null;
        }
        return dish;
    }

    /**
     * Fetch all matching {@code DishComponent} records from the database
     *
     * @param whereClause - a String containing an optional where clause including the word WHERE
     * @return an {@code Arraylist} of {@code DishComponent} objects
     */
    public ArrayList<DishComponent> getDishComponents(String whereClause) {
        ArrayList<DishComponent> al = new ArrayList<>();
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(String.format(SQL_GET_DISH_COMPONENTS, whereClause));
            while (rs.next()) {
                al.add(new DishComponent(
                        rs.getInt("id"),
                        rs.getInt("dish_id"),
                        rs.getInt("food_type_id"),
                        rs.getInt("proportion")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Exception occurred while attempting to fetch DishComponent");
            throw new RuntimeException(e);
        }
        return al;
    }

    public ArrayList<DishComponent> getDishComponents() {
        return getDishComponents("");
    }

    public DishComponent getDishComponent(int id) {
        DishComponent dishComponent;
        try {
            dishComponent = getDishComponents(String.format("WHERE id = %d", id)).getFirst();
        } catch (NoSuchElementException e) {
            //element does not exist so we return null
            dishComponent = null;
        }
        return dishComponent;
    }

    /**
     * Insert a {@code FoodCategory} record into the database, returning the generated key
     *
     * @param foodCategory - the {@code FoodCategory} object to be inserted into the database
     * @return {@code int} value containing the generated primary key
     */
    public int insertFoodCategory(FoodCategory foodCategory) {
        int id = 0;
        try (PreparedStatement ps = connection.prepareStatement(SQL_INSERT_FOOD_CATEGORY, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, foodCategory.getName());
            ps.setString(2, foodCategory.getDescription());

            if (ps.executeUpdate() > 0) {
                ResultSet generatedKeys = ps.getGeneratedKeys();
                if (generatedKeys.next()) {
                    id = generatedKeys.getInt(1);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return id;
    }

    /**
     * Insert a {@code FoodType} record into the database, returning the generated key
     *
     * @param foodType - the {@code FoodType} object to be inserted into the database
     * @return {@code int} value containing the generated primary key
     */
    public int insertFoodType(FoodType foodType) {
        int id = 0;
        try (PreparedStatement ps = connection.prepareStatement(SQL_INSERT_FOOD_TYPE, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, foodType.getFoodCategoryId());
            ps.setString(2, foodType.getName());
            ps.setString(3, foodType.getDescription());

            if (ps.executeUpdate() > 0) {
                ResultSet generatedKeys = ps.getGeneratedKeys();
                if (generatedKeys.next()) {
                    id = generatedKeys.getInt(1);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return id;
    }

    /**
     * Insert a {@code PreparationTechnique} record into the database, returning the generated key
     *
     * @param preparationTechnique - the {@code PreparationTechnique} object to be inserted into the database
     * @return {@code code} value containing the provided primary key
     */
    public String insertPreparationTechnique(PreparationTechnique preparationTechnique) {
        try (PreparedStatement ps = connection.prepareStatement(SQL_INSERT_PREPARATION_TECHNIQUE, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, preparationTechnique.getCode());
            ps.setString(2, preparationTechnique.getDescription());

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return preparationTechnique.getCode();
    }

    /**
     * Insert a {@code Dish} record into the database, returning the generated key
     *
     * @param dish - the {@code Dish} object to be inserted into the database
     * @return {@code int} value containing the generated primary key
     */
    public int insertDish(Dish dish) {
        int id = 0;
        try (PreparedStatement ps = connection.prepareStatement(SQL_INSERT_DISH, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, dish.getName());
            ps.setString(2, dish.getDescription());
            ps.setString(3, dish.getPreparationTechniqueCode());

            if (ps.executeUpdate() > 0) {
                ResultSet generatedKeys = ps.getGeneratedKeys();
                if (generatedKeys.next()) {
                    id = generatedKeys.getInt(1);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return id;
    }

    /**
     * Insert a {@code DishComponent} record into the database, returning the generated key
     *
     * @param dishComponent - the {@code DishComponent} object to be inserted into the database
     * @return {@code int} value containing the generated primary key
     */
    public int insertDishComponent(DishComponent dishComponent) {
        int id = 0;
        try (PreparedStatement ps = connection.prepareStatement(SQL_INSERT_DISH_COMPONENT, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, dishComponent.getDishId());
            ps.setInt(2, dishComponent.getFoodTypeId());
            ps.setInt(3, dishComponent.getProportion());

            if (ps.executeUpdate() > 0) {
                ResultSet generatedKeys = ps.getGeneratedKeys();
                if (generatedKeys.next()) {
                    id = generatedKeys.getInt(1);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return id;
    }

    /**
     * Insert a {@code Meal} record into the database, returning the generated key
     *
     * @param meal - the {@code Meal} object to be inserted into the database
     * @return {@code int} value containing the generated primary key
     */
    public int insertMeal(Meal meal) {
        int id = 0;
        try (PreparedStatement ps = connection.prepareStatement(SQL_INSERT_MEAL, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setDate(1, Date.valueOf(meal.getDate()));
            ps.setTime(2, Time.valueOf(meal.getTime()));

            if (ps.executeUpdate() > 0) {
                ResultSet generatedKeys = ps.getGeneratedKeys();
                if (generatedKeys.next()) {
                    id = generatedKeys.getInt(1);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return id;
    }

    /**
     * Insert a {@code MealComponent} record into the database, returning the generated key
     *
     * @param mealComponent - the {@code MealComponent} object to be inserted into the database
     * @return {@code int} value containing the generated primary key
     */
    public int insertMealComponent(MealComponent mealComponent) {
        int id = 0;
        try (PreparedStatement ps = connection.prepareStatement(SQL_INSERT_MEAL_COMPONENT, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, mealComponent.getMealId());
            ps.setInt(2, mealComponent.getFoodTypeId());
            ps.setString(3, mealComponent.getPreparationTechniqueCode());
            ps.setInt(4, mealComponent.getVolume());

            if (ps.executeUpdate() > 0) {
                ResultSet generatedKeys = ps.getGeneratedKeys();
                if (generatedKeys.next()) {
                    id = generatedKeys.getInt(1);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return id;
    }

    /**
     * Update the corresponding {@code FoodCategory} record in the database
     *
     * @param foodCategory - the {@code FoodCategory} object to be updated
     * @return {@code boolean} value indicating whether update was successful or not
     */
    public boolean updateFoodCategory(FoodCategory foodCategory) {
        try (PreparedStatement ps = connection.prepareStatement(SQL_UPDATE_FOOD_CATEGORY, PreparedStatement.RETURN_GENERATED_KEYS)) {
            //set value parameters
            ps.setString(1, foodCategory.getName());
            ps.setString(2, foodCategory.getDescription());

            //finally set primary key parameter
            ps.setInt(3, foodCategory.getId());
            //execute
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            System.err.println("An error occurred while attempting to update the FoodCategory record identified by '" + foodCategory.getId() + "' in the database.");
            System.err.println(e.getMessage());
            return false;
        }
    }

    /**
     * Update the corresponding {@code FoodType} record in the database
     *
     * @param foodType - the {@code FoodType} object to be updated
     * @return {@code boolean} value indicating whether update was successful or not
     */
    public boolean updateFoodType(FoodType foodType) {
        try (PreparedStatement ps = connection.prepareStatement(SQL_UPDATE_FOOD_TYPE, PreparedStatement.RETURN_GENERATED_KEYS)) {
            //set value parameters
            ps.setInt(1, foodType.getFoodCategoryId());
            ps.setString(2, foodType.getName());
            ps.setString(3, foodType.getDescription());

            //finally set primary key parameter
            ps.setInt(4, foodType.getId());
            //execute
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            System.err.println("An error occurred while attempting to update the FoodType record identified by '" + foodType.getId() + "' in the database.");
            System.err.println(e.getMessage());
            return false;
        }
    }

    /**
     * Update the corresponding {@code PreparationTechnique} record in the database
     *
     * @param preparationTechnique - the {@code PreparationTechnique} object to be updated
     * @return {@code boolean} value indicating whether update was successful or not
     */
    public boolean updatePreparationTechnique(PreparationTechnique preparationTechnique) {
        try (PreparedStatement ps = connection.prepareStatement(SQL_UPDATE_PREPARATION_TECHNIQUE, PreparedStatement.RETURN_GENERATED_KEYS)) {
            //set value parameters
            ps.setString(1, preparationTechnique.getDescription());

            //finally set primary key parameter
            ps.setString(2, preparationTechnique.getCode());
            //execute
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            System.err.println("An error occurred while attempting to update the PreparationTechnique record identified by '" + preparationTechnique.getCode() + "' in the database.");
            System.err.println(e.getMessage());
            return false;
        }
    }

    /**
     * Update the corresponding {@code Dish} record in the database
     *
     * @param dish - the {@code Dish} object to be updated
     * @return {@code boolean} value indicating whether update was successful or not
     */
    public boolean updateDish(Dish dish) {
        try (PreparedStatement ps = connection.prepareStatement(SQL_UPDATE_DISH, PreparedStatement.RETURN_GENERATED_KEYS)) {
            //set value parameters
            ps.setString(1, dish.getName());
            ps.setString(2, dish.getDescription());
            ps.setString(3, dish.getPreparationTechniqueCode());

            //finally set primary key parameter
            ps.setInt(4, dish.getId());
            //execute
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            System.err.println("An error occurred while attempting to update the Dish record identified by '" + dish.getId() + "' in the database.");
            System.err.println(e.getMessage());
            return false;
        }
    }

    /**
     * Update the corresponding {@code DishComponent} record in the database
     *
     * @param dishComponent - the {@code DishComponent} object to be updated
     * @return {@code boolean} value indicating whether update was successful or not
     */
    public boolean updateDishComponent(DishComponent dishComponent) {
        try (PreparedStatement ps = connection.prepareStatement(SQL_UPDATE_DISH_COMPONENT, PreparedStatement.RETURN_GENERATED_KEYS)) {
            //set value parameters
            ps.setInt(1, dishComponent.getDishId());
            ps.setInt(2, dishComponent.getFoodTypeId());
            ps.setInt(3, dishComponent.getProportion());

            //finally set primary key parameter
            ps.setInt(4, dishComponent.getId());
            //execute
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            System.err.println("An error occurred while attempting to update the DishComponent record identified by '" + dishComponent.getId() + "' in the database.");
            System.err.println(e.getMessage());
            return false;
        }
    }

    /**
     * Update the corresponding {@code Meal} record in the database
     *
     * @param meal - the {@code Meal} object to be updated
     * @return {@code boolean} value indicating whether update was successful or not
     */
    public boolean updateMeal(Meal meal) {
        try (PreparedStatement ps = connection.prepareStatement(SQL_UPDATE_MEAL, PreparedStatement.RETURN_GENERATED_KEYS)) {
            //set value parameters
            ps.setDate(1, Date.valueOf(meal.getDate()));
            ps.setTime(2, Time.valueOf(meal.getTime()));

            //finally set primary key parameter
            ps.setInt(3, meal.getId());
            //execute
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            System.err.println("An error occurred while attempting to update the Meal record identified by '" + meal.getId() + "' in the database.");
            System.err.println(e.getMessage());
            return false;
        }
    }

    /**
     * Update the corresponding {@code MealComponent} record in the database
     *
     * @param mealComponent - the {@code MealComponent} object to be updated
     * @return {@code boolean} value indicating whether update was successful or not
     */
    public boolean updateMealComponent(MealComponent mealComponent) {
        try (PreparedStatement ps = connection.prepareStatement(SQL_UPDATE_MEAL_COMPONENT, PreparedStatement.RETURN_GENERATED_KEYS)) {
            //set value parameters
            ps.setInt(1, mealComponent.getMealId());
            ps.setInt(2, mealComponent.getFoodTypeId());
            ps.setString(3, mealComponent.getPreparationTechniqueCode());
            ps.setInt(4, mealComponent.getVolume());

            //finally set primary key parameter
            ps.setInt(5, mealComponent.getId());
            //execute
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            System.err.println("An error occurred while attempting to update the MealComponent record identified by '" + mealComponent.getId() + "' in the database.");
            System.err.println(e.getMessage());
            return false;
        }
    }

    /**
     * Delete the corresponding {@code FoodCategory} record from the database
     *
     * @param foodCategory the {@code FoodCategory} to be deleted
     * @return {@code boolean} value indicating whether delete was successful or not
     */
    public boolean deleteFoodCategory(FoodCategory foodCategory) {
        try (PreparedStatement ps = connection.prepareStatement(SQL_DELETE_FOOD_CATEGORY)) {
            //set id parameter
            ps.setInt(1, foodCategory.getId());
            //execute
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            System.err.println("An error occurred while attempting to delete the FoodCategory record identified by '" + foodCategory.getId() + "' from the database.");
            System.err.println(e.getMessage());
            return false;
        }
    }

    /**
     * Delete the corresponding {@code FoodType} record from the database
     *
     * @param foodType the {@code FoodType} to be deleted
     * @return {@code boolean} value indicating whether delete was successful or not
     */
    public boolean deleteFoodType(FoodType foodType) {
        try (PreparedStatement ps = connection.prepareStatement(SQL_DELETE_FOOD_TYPE)) {
            //set id parameter
            ps.setInt(1, foodType.getId());
            //execute
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            System.err.println("An error occurred while attempting to delete the FoodType record identified by '" + foodType.getId() + "' from the database.");
            System.err.println(e.getMessage());
            return false;
        }
    }

    /**
     * Delete the corresponding {@code PreparationTechnique} record from the database
     *
     * @param preparationTechnique the {@code PreparationTechnique} to be deleted
     * @return {@code boolean} value indicating whether delete was successful or not
     */
    public boolean deletePreparationTechnique(PreparationTechnique preparationTechnique) {
        try (PreparedStatement ps = connection.prepareStatement(SQL_DELETE_PREPARATION_TECHNIQUE)) {
            //set primary key parameter
            ps.setString(1, preparationTechnique.getCode());
            //execute
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            System.err.println("An error occurred while attempting to delete the PreparationTechnique record identified by '" + preparationTechnique.getCode() + "' from the database.");
            System.err.println(e.getMessage());
            return false;
        }
    }

    /**
     * Delete the corresponding {@code Meal} record from the database
     *
     * @param meal the {@code Meal} to be deleted
     * @return {@code boolean} value indicating whether delete was successful or not
     */
    public boolean deleteMeal(Meal meal) {
        try (PreparedStatement ps = connection.prepareStatement(SQL_DELETE_MEAL)) {
            //set primary key parameter
            ps.setInt(1, meal.getId());
            //execute
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            System.err.println("An error occurred while attempting to delete the Meal record identified by '" + meal.getId() + "' from the database.");
            System.err.println(e.getMessage());
            return false;
        }
    }

    /**
     * Delete the corresponding {@code MealComponent} record from the database
     *
     * @param mealComponent the {@code MealComponent} to be deleted
     * @return {@code boolean} value indicating whether delete was successful or not
     */
    public boolean deleteMealComponent(MealComponent mealComponent) {
        try (PreparedStatement ps = connection.prepareStatement(SQL_DELETE_MEAL_COMPONENT)) {
            //set primary key parameter
            ps.setInt(1, mealComponent.getId());
            //execute
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            System.err.println("An error occurred while attempting to delete the MealComponent record identified by '" + mealComponent.getId() + "' from the database.");
            System.err.println(e.getMessage());
            return false;
        }
    }

    /**
     * Delete the corresponding {@code Dish} record from the database
     *
     * @param dish the {@code Dish} to be deleted
     * @return {@code boolean} value indicating whether delete was successful or not
     */
    public boolean deleteDish(Dish dish) {
        try (PreparedStatement ps = connection.prepareStatement(SQL_DELETE_DISH)) {
            //set primary key parameter
            ps.setInt(1, dish.getId());
            //execute
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            System.err.println("An error occurred while attempting to delete the Dish record identified by '" + dish.getId() + "' from the database.");
            System.err.println(e.getMessage());
            return false;
        }
    }

    /**
     * Delete the corresponding {@code DishComponent} record from the database
     *
     * @param dishComponent the {@code DishComponent} to be deleted
     * @return {@code boolean} value indicating whether delete was successful or not
     */
    public boolean deleteDishComponent(DishComponent dishComponent) {
        try (PreparedStatement ps = connection.prepareStatement(SQL_DELETE_DISH_COMPONENT)) {
            //set primary key parameter
            ps.setInt(1, dishComponent.getId());
            //execute
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            System.err.println("An error occurred while attempting to delete the DishComponent record identified by '" + dishComponent.getId() + "' from the database.");
            System.err.println(e.getMessage());
            return false;
        }
    }
}
