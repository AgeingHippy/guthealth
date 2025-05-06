package com.ageinghippy.service;

import com.ageinghippy.model.DTOMapper;
import com.ageinghippy.model.dto.FoodCategoryDTOComplex;
import com.ageinghippy.model.dto.FoodCategoryDTOSimple;
import com.ageinghippy.model.entity.FoodCategory;
import com.ageinghippy.model.entity.FoodType;
import com.ageinghippy.model.entity.UserPrinciple;
import com.ageinghippy.repository.FoodCategoryRepository;
import com.ageinghippy.util.Util;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class FoodCategoryService {
    private final FoodCategoryRepository foodCategoryRepository;
    private final DTOMapper dTOMapper;
    private final EntityManager entityManager;
    private final CacheManager cacheManager;

    @Cacheable(value = "foodCategory",key="#id")
    //todo - evict this foodCategory when adding/updating related foodTypes
    public FoodCategoryDTOComplex getFoodCategory(Long id) {
        return dTOMapper.map(foodCategoryRepository.findById(id).orElseThrow(), FoodCategoryDTOComplex.class);
    }

    @Cacheable(value="foodCategoryList",key="'principleId='+#principle.getId()")
    public List<FoodCategoryDTOSimple> getFoodCategories(UserPrinciple principle) {
        return dTOMapper.mapList(foodCategoryRepository.findAllByPrinciple(principle), FoodCategoryDTOSimple.class);
    }

    @Transactional
    @CacheEvict(value="foodCategoryList",key="'principleId='+#principle.getId()")
    public FoodCategoryDTOComplex createFoodCategory(FoodCategoryDTOSimple foodCategory, UserPrinciple principle) {
        FoodCategory newFoodCategory = dTOMapper.map(foodCategory, FoodCategory.class);
        newFoodCategory.setPrinciple(principle);

        newFoodCategory = saveFoodCategory(newFoodCategory);

        return dTOMapper.map(newFoodCategory, FoodCategoryDTOComplex.class);
    }

    //todo - prototype method
    @Transactional
    @CacheEvict(value="foodCategoryList",key="'principleId='+#principle.getId()")
    public FoodCategoryDTOComplex createFoodCategory(FoodCategoryDTOComplex foodCategory, UserPrinciple principle) {
        FoodCategory newFoodCategory = dTOMapper.map(foodCategory, FoodCategory.class);
        newFoodCategory.setPrinciple(principle);

        //need to assign foodCategory explicitly
        //todo - assign in mapper?
        for (int i=0; i< newFoodCategory.getFoodTypes().size(); i++) {
            newFoodCategory.getFoodTypes().get(i).setFoodCategory(newFoodCategory);
        }

        List<FoodType> foodTypes = newFoodCategory.getFoodTypes();

        newFoodCategory = saveFoodCategory(newFoodCategory);

        return dTOMapper.map(newFoodCategory, FoodCategoryDTOComplex.class);
    }

    private FoodCategory saveFoodCategory(FoodCategory foodCategory) {
        foodCategory = foodCategoryRepository.save(foodCategory);
        entityManager.flush();
        entityManager.refresh(foodCategory);
        return foodCategory;
    }

    /**
     * Updates the food category identified by the provided id with non-null values as provided in updateFoodCategory
     *
     * @param id                 The id of the {@code FoodCategory} to update
     * @param updateFoodCategory the {@code FoodCategory} containing updated values
     * @return the updated {@code FoodCategory}
     * @throws java.util.NoSuchElementException if the food category with the provided id does not exist
     */
    @Transactional
    public FoodCategoryDTOComplex updateFoodCategory(Long id, FoodCategoryDTOSimple updateFoodCategory) {

        FoodCategory foodCategory = foodCategoryRepository.findById(id).orElseThrow();
        foodCategory.setName(Util.valueIfNull(updateFoodCategory.name(), foodCategory.getName()));
        foodCategory.setDescription(Util.valueIfNull(updateFoodCategory.description(), foodCategory.getDescription()));

        foodCategory = saveFoodCategory(foodCategory);

        evictFoodCategoryListCacheForCurrentPrinciple();

        return dTOMapper.map(foodCategory, FoodCategoryDTOComplex.class);
    }

    private void deleteFoodCategory(FoodCategory foodCategory) {
        foodCategoryRepository.deleteById(foodCategory.getId());
    }

    @Transactional
    public void deleteFoodCategory(Long id) {
        deleteFoodCategory(foodCategoryRepository.findById(id).orElseThrow());
        evictFoodCategoryListCacheForCurrentPrinciple();
    }

    private void evictFoodCategoryListCacheForCurrentPrinciple() {
        UserPrinciple userPrinciple = (UserPrinciple) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Objects.requireNonNull(cacheManager.getCache("foodCategoryList")).evictIfPresent("principleId=" + userPrinciple.getId());
    }
}
