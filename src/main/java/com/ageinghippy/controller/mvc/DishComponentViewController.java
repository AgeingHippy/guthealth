package com.ageinghippy.controller.mvc;

import com.ageinghippy.model.dto.DishComponentDTO;
import com.ageinghippy.service.DishComponentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/dish-component")
@RequiredArgsConstructor
public class DishComponentViewController {
    private final DishComponentService dishComponentService;

    @GetMapping("")
    public String getAllDishComponents(Model model, @PathVariable Long dishId) {
        List<DishComponentDTO> dishComponents = dishComponentService.getDishComponents(dishId);

        model.addAttribute("dishComponents", dishComponents);

        return "redirect:/dish/edit/"+dishId;
    }

}
