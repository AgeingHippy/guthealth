package com.ageinghippy.controller;

import com.ageinghippy.model.dto.PreparationTechniqueDTO;
import com.ageinghippy.model.entity.PreparationTechnique;
import com.ageinghippy.service.PreparationTechniqueService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping
@RequiredArgsConstructor
public class ApplicationController {

    private final PreparationTechniqueService preparationTechniqueService;

    @GetMapping(value = {"/","/home"})
    public String gotoIndex(Model model) {
        return "redirect:/index";
    }

    @GetMapping(value = {"/index"})
    public String showIndex(Model model) {
        return "/index";
    }

    @GetMapping(value = "/preparation-technique")
    public String showPreparationTechnique(Model model) {
        List<PreparationTechniqueDTO> preparationTechniques = preparationTechniqueService.getPreparationTechniques();
        model.addAttribute("preparationTechniques",preparationTechniques);

        return "/preparation-technique";
    }

//    @GetMapping(value = "/preparation-technique-edit")
//    public String editPreparationTechnique(Model model, @RequestParam String code) {
//        PreparationTechniqueDTO preparationTechnique = preparationTechniqueService.getPreparationTechnique(code);
//
//        model.addAttribute("preparationTechnique",preparationTechnique);
//
//        return "/edit-preparation-technique";
//    }

    @GetMapping(value = "/preparation-technique/edit/{code}")
    public String editPreparationTechnique(Model model, @PathVariable String code) {
        PreparationTechniqueDTO preparationTechnique = preparationTechniqueService.getPreparationTechnique(code);

        model.addAttribute("preparationTechnique",preparationTechnique);

        return "/preparation-technique-edit";
    }


    @PostMapping("/preparation-technique/update/{code}")
    public String updatePreparationTechnique(@PathVariable String code,
                                             @ModelAttribute PreparationTechniqueDTO preparationTechnique) {
        preparationTechnique = preparationTechniqueService.updatePreparationTechnique(code, preparationTechnique);
        return "redirect:/preparation-technique";
    }

    @GetMapping("/preparation-technique/new")
    public String showNewPreparationTechniqueForm(Model model) {
        model.addAttribute("preparationTechnique",new PreparationTechniqueDTO(null,null));
        return "preparation-technique-create";
    }

    @PostMapping("/preparation-technique/create")
    public String createPreparationTechnique(@ModelAttribute PreparationTechniqueDTO preparationTechnique) {
        preparationTechnique = preparationTechniqueService.createPreparationTechnique( preparationTechnique);
        return "redirect:/preparation-technique";
    }

    @RequestMapping("/preparation-technique/delete/{code}")
    public String deletePreparationTechnique(@PathVariable String code) {
        preparationTechniqueService.deletePreparationTechnique(code);
        return "redirect:/preparation-technique";
    }

}
