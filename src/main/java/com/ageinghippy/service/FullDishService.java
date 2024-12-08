package com.ageinghippy.service;

import com.ageinghippy.data.GutHealthDAO;
import com.ageinghippy.data.model.*;

import java.util.ArrayList;

/**
 * Manage all that is related to dishes including dish components
 */
public class FullDishService {
    private final GutHealthDAO gutHealthDAO;

    public FullDishService(GutHealthDAO gutHealthDAO) {
        this.gutHealthDAO = gutHealthDAO;
    }

    public FullDish getFullDish(int id) {
        FullDish fullDish = new FullDish();
        fullDish.setDish(gutHealthDAO.getDish(id));
        fullDish.setDishComponents(gutHealthDAO.getDishComponents("WHERE dish_id = " + fullDish.getDish().getId()));
        return fullDish;
    }

    public ArrayList<Dish> getDishes(String whereClause) {
        return gutHealthDAO.getDishes(whereClause);
    }

    public ArrayList<FullDish> getFullDishes() {
        ArrayList<FullDish> fullDishes = new ArrayList<>();
        gutHealthDAO.getDishes().forEach(dish -> {
            fullDishes.add(new FullDish(dish, gutHealthDAO.getDishComponents("WHERE dish_id = " + dish.getId())));
        });
        return fullDishes;
    }

    public FullDish saveFullDish(FullDish fullDish) {

        //save dish
        fullDish.setDish(saveDish(fullDish.getDish()));

        //save current dish components
        fullDish.getDishComponents().forEach(dishComponent -> {
            dishComponent.setDishId(
                    dishComponent.getDishId() == 0 ? fullDish.getDish().getId() : dishComponent.getDishId());
            dishComponent = saveDishComponent(dishComponent);
        });

        //delete removed dish components
        fullDish.getRemovedDishComponents().forEach(this::deleteDishComponent);
        //reset deletedDishComponents
        fullDish.getRemovedDishComponents().clear();

        return fullDish;
    }

    private Dish saveDish(Dish dish) {
        int id;
        if (dish.getId() == 0) {
            id = gutHealthDAO.insertDish(dish);
        } else {
            gutHealthDAO.updateDish(dish);
            id = dish.getId();
        }
        return gutHealthDAO.getDish(id);
    }

    private DishComponent saveDishComponent(DishComponent dishComponent) {
        int id;
        if (dishComponent.getId() == 0) {
            id = gutHealthDAO.insertDishComponent(dishComponent);
        } else {
            gutHealthDAO.updateDishComponent(dishComponent);
            id = dishComponent.getId();
        }
        return gutHealthDAO.getDishComponent(id);
    }

    public void deleteFullDish(FullDish fullDish) {
        fullDish.getDishComponents().forEach(gutHealthDAO::deleteDishComponent);
        gutHealthDAO.deleteDish(fullDish.getDish());
    }

    private void deleteDishComponent(DishComponent dishComponent) {
        if (dishComponent.getId() != 0) {
            gutHealthDAO.deleteDishComponent(dishComponent);
        }
    }

    public String dishComponentPrintString(DishComponent dishComponent) {
        FoodType foodType = gutHealthDAO.getFoodType(dishComponent.getFoodTypeId());
        return "DishComponent{" +
                "id=" + dishComponent.getId() +
                ", foodType=" + foodType.getName() + '\'' +
                ", proportion=" + dishComponent.getProportion() +
                '}';
    }

}
