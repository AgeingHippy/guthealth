package com.ageinghippy.controller.mvc;

import com.ageinghippy.model.dto.DishComponentDTO;
import com.ageinghippy.model.dto.FoodTypeDTOComplex;
import com.ageinghippy.model.dto.FoodTypeDTOSimple;
import com.ageinghippy.service.DishComponentService;
import com.ageinghippy.service.DishService;
import com.ageinghippy.service.FoodTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/dish-component")
@RequiredArgsConstructor
public class DishComponentViewController {
    private final DishComponentService dishComponentService;
    private final DishService dishService;
    private final FoodTypeService foodTypeService;

    @GetMapping("")
    public String getAllDishComponents(Model model, @PathVariable Long dishId) {
        List<DishComponentDTO> dishComponents = dishComponentService.getDishComponents(dishId);

        model.addAttribute("dishComponents", dishComponents);

        return "redirect:/dish/edit/" + dishId;
    }

    @GetMapping("/new")
    public String showNewDishComponentView(Model model, @RequestParam Long dishId) {
        model.addAttribute("dishComponent",
                new DishComponentDTO(null, new FoodTypeDTOSimple(null, null, null), null));
        model.addAttribute("dish", dishService.getDish(dishId));

        return "/dish-component-new";
    }

    @PostMapping("/create")
    public String createDishComponent(@RequestParam Long dishId, @ModelAttribute DishComponentDTO dishComponent) {
        dishComponentService.createDishComponent(dishId, dishComponent);

        return "redirect:/dish/edit/" + dishId;
    }

    @GetMapping("/edit/{id}")
    public String showEditDishComponentView(Model model, @PathVariable Long id, @RequestParam Long dishId) {
        DishComponentDTO dishComponentDTO = dishComponentService.getDishComponent(id);
        FoodTypeDTOComplex foodTypeDTOComplex = foodTypeService.getFoodType(dishComponentDTO.foodType().id());

        model.addAttribute("dishComponent", dishComponentDTO);
        model.addAttribute("dish", dishService.getDish(dishId));
        model.addAttribute("foodCategoryId",foodTypeDTOComplex.foodCategory().id());

        return "/dish-component-edit";
    }

    @PostMapping("/update/{id}")
    public String updateDishComponent(Model model, @ModelAttribute DishComponentDTO dishComponent,
                                      @PathVariable Long id, @RequestParam Long dishId) {
        dishComponentService.updateDishComponent(id, dishComponent);

        return "redirect:/dish/edit/" + dishId;
    }

    @RequestMapping("/delete/{id}")
    public String deleteDishComponent(@PathVariable Long id, @RequestParam Long dishId) {
        dishComponentService.deleteDishComponent(id);

        return "redirect:/dish/edit/" + dishId;
    }

}
