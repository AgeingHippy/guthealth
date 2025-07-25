package com.ageinghippy.configuration.security;

import com.ageinghippy.model.entity.*;
import com.ageinghippy.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CustomPermissionEvaluator implements PermissionEvaluator {

    private final MealRepository mealRepository;
    private final MealComponentRepository mealComponentRepository;
    private final DishRepository dishRepository;
    private final DishComponentRepository dishComponentRepository;
    private final FoodCategoryRepository foodCategoryRepository;
    private final FoodTypeRepository foodTypeRepository;
    private final PreparationTechniqueRepository preparationTechniqueRepository;

    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        return false;
    }

    /**
     * Evaluate whether the logged-in user has permissions for the entity they are attempting to manipulate
     *
     * @param authentication
     * @param targetId
     * @param targetType
     * @param permission
     * @return
     */
    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {

        if (!permission.getClass().equals("".getClass())) {
            throw new SecurityException(
                    "Cannot execute hasPermission() calls where " +
                    "permission is not in String form");
        }


        //we need to determine whether the user is the owner of the entity
        UserPrinciple userPrinciple = (UserPrinciple) authentication.getPrincipal();

        switch (targetType) {
            case "Meal":
                Optional<Meal> meal = mealRepository.findById(Long.parseLong(targetId.toString()));
                if (meal.isEmpty()) {
                    return true;
                }
                return meal.get().getPrinciple().getUsername().equals(userPrinciple.getUsername());

            case "MealComponent":
                Optional<MealComponent> mealComponent = mealComponentRepository.findById(Long.parseLong(targetId.toString()));
                if (mealComponent.isEmpty()) {
                    return true;
                }
                return mealComponent.get().getMeal().getPrinciple().getUsername().equals(userPrinciple.getUsername());

            case "Dish":
                Optional<Dish> dish = dishRepository.findById(Long.parseLong(targetId.toString()));
                if (dish.isEmpty()) {
                    return true;
                }
                return dish.get().getPrinciple().getUsername().equals(userPrinciple.getUsername());

            case "DishComponent":
                Optional<DishComponent> dishComponent = dishComponentRepository.findById(Long.parseLong(targetId.toString()));
                if (dishComponent.isEmpty()) {
                    return true;
                }
                return dishComponent.get().getDish().getPrinciple().getUsername().equals(userPrinciple.getUsername());

            case "FoodCategory":
                Optional<FoodCategory> foodCategory = foodCategoryRepository.findById(Long.parseLong(targetId.toString()));
                if (foodCategory.isEmpty()) {
                    return true;
                }
                return hasSystemDataReadPermission(foodCategory.get().getPrinciple(), userPrinciple, permission)
                       ||
                       foodCategory.get().getPrinciple().getUsername().equals(userPrinciple.getUsername());

            case "FoodType":
                Optional<FoodType> foodType = foodTypeRepository.findById(Long.parseLong(targetId.toString()));
                if (foodType.isEmpty()) {
                    return true;
                }
                return foodType.get().getFoodCategory().getPrinciple().getUsername().equals(userPrinciple.getUsername());

            case "PreparationTechnique":
                Optional<PreparationTechnique> preparationTechnique =
                        preparationTechniqueRepository.findById(Long.parseLong(targetId.toString()));
                if (preparationTechnique.isEmpty()) {
                    return true;
                }
                return hasSystemDataReadPermission(preparationTechnique.get().getPrinciple(), userPrinciple, permission)
                       ||
                       preparationTechnique.get().getPrinciple().getUsername().equals(userPrinciple.getUsername());

            default:
                break;
        }
        return false;
    }


    private boolean hasSystemDataReadPermission(UserPrinciple dataOwner, UserPrinciple supplicant, Object permission) {
        return "read".equals(permission.toString())
               &&
               supplicant.getAuthorities()
                       .stream()
                       .anyMatch(auth -> "ROLE_GUEST".equals(auth.getAuthority()) || "ROLE_USER".equals(auth.getAuthority()))
               &&
               "system".equals(dataOwner.getUsername());
    }
}
