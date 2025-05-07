package com.ageinghippy.controller.mvc;

import com.ageinghippy.model.dto.FoodCategoryDTOComplex;
import com.ageinghippy.model.dto.FoodCategoryDTOSimple;
import com.ageinghippy.model.entity.UserPrinciple;
import com.ageinghippy.service.FoodCategoryService;
import com.ageinghippy.service.FoodTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/food-category")
@RequiredArgsConstructor
public class FoodCategoryViewController {

    private final FoodCategoryService foodCategoryService;
    private final FoodTypeService foodTypeService;

    @GetMapping("")
    public String showFoodCategories(Model model, Authentication authentication) {
        List<FoodCategoryDTOSimple> foodCategories = foodCategoryService.getFoodCategories((UserPrinciple) authentication.getPrincipal());

        model.addAttribute("foodCategories", foodCategories);

        return "/food-category";
    }

    @GetMapping("/view/{id}")
    @PreAuthorize("hasPermission(#id,'FoodCategory','read')")
    public String showSpecificFoodCategoryView(Model model, @PathVariable Long id) {
        FoodCategoryDTOComplex foodCategory = foodCategoryService.getFoodCategory(id);

        model.addAttribute("foodCategory", foodCategory);

        return "food-category-view";
    }


    @RequestMapping("/delete/{id}")
    @PreAuthorize("hasPermission(#id,'FoodCategory','delete')")
    public String deleteFoodCategory(@PathVariable Long id,
                                     RedirectAttributes redirectAttributes) {
        try {
            foodCategoryService.deleteFoodCategory(id);
            redirectAttributes.addFlashAttribute("successMessage", "FoodCategory deleted successfully");
        } catch (Exception e) {
            String errorMessage = e.getClass().getSimpleName() + " - " + "FoodCategory delete failed.";
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
        }

        return "redirect:/food-category";
    }

    @GetMapping("/new")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
    public String showNewFoodCategoryView(Model model) {
        if (!model.containsAttribute("foodCategory")) {
            model.addAttribute("foodCategory", new FoodCategoryDTOSimple(null, null, null));
        }

        return "/food-category-new";
    }

    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
    public String createFoodCategory(@ModelAttribute FoodCategoryDTOSimple foodCategory,
                                     Authentication authentication,
                                     RedirectAttributes redirectAttributes) {
        try {
            Long id = foodCategoryService.createFoodCategory(foodCategory, (UserPrinciple) authentication.getPrincipal()).id();
            redirectAttributes.addFlashAttribute("successMessage", "FoodCategory successfully created");
            return "redirect:/food-category/edit/" + id;
        } catch (Exception e) {
            String errorMessage = e.getClass().getSimpleName() + " - " + "FoodCategory create failed.";
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
            redirectAttributes.addFlashAttribute("foodCategory", foodCategory);
            return "redirect:/food-category/new";
        }

    }

    @GetMapping("/edit/{id}")
    @PreAuthorize("hasPermission(#id,'FoodCategory','edit')")
    public String showEditFoodCategoryView(Model model, @PathVariable Long id) {
        if (!model.containsAttribute("foodCategory")) {
            FoodCategoryDTOComplex foodCategoryDTOComplex = foodCategoryService.getFoodCategory(id);
            model.addAttribute("foodCategory", foodCategoryDTOComplex);
        }

        return "/food-category-edit";
    }

    @PostMapping("/update/{id}")
    @PreAuthorize("hasPermission(#id,'FoodCategory','edit')")
    public String updateFoodCategory(@PathVariable Long id,
                                     @ModelAttribute FoodCategoryDTOSimple foodCategory,
                                     RedirectAttributes redirectAttributes) {
        try {
            foodCategoryService.updateFoodCategory(id, foodCategory);
            redirectAttributes.addFlashAttribute("successMessage", "FoodCategory updated successfully");
        } catch (Exception e) {
            String errorMessage = e.getClass().getSimpleName() + " - " + "FoodCategory update failed.";
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
            redirectAttributes.addFlashAttribute("foodCategory",
                    new FoodCategoryDTOComplex(foodCategory.id(), foodCategory.name(), foodCategory.description(),
                            foodCategoryService.getFoodCategory(id).foodTypes()));
        }

        return "redirect:/food-category/edit/" + id;
    }


}
