package com.ageinghippy.data;

import com.ageinghippy.model.*;

import java.sql.*;
import java.text.MessageFormat;
import java.util.ArrayList;

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
    private static final String SQL_UPDATE_FOOD_MEAL_COMPONENT = """
            UPDATE  meal_component
               SET  meal_id = ?
                  , food_type_id = ?
                  , preparation_technique_code = ?
                  , volume = ?
            WHERE   id = ?""";

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
        return getFoodCategories(String.format("WHERE id = %s",id)).getFirst();
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
        return getFoodTypes(String.format("WHERE id = %d",id)).getFirst();
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
        return getPreparationTechniques(String.format("WHERE code = %s",code)).getFirst();
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
        return getMeals(String.format("WHERE id = %d",id)).getFirst();
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
        return getMealComponents(String.format("WHERE id = %d",id)).getFirst();
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
        return getDishes(String.format("WHERE id = %d",id)).getFirst();
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
        return getDishComponents(String.format("WHERE id = %d",id)).getFirst();
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
            ps.setString(2, foodType.getDescription());

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
            ps.setInt(3,foodCategory.getId());
            //execute
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            System.err.println("An error occurred while attempting to update the FoodCategory record identified by '" +foodCategory.getId() + "' in the database." );
            System.err.println(e.getMessage());
            return false;
        }
    }

}
