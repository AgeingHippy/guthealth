package com.ageinghippy.controller.view;

import com.ageinghippy.model.dto.PreparationTechniqueDTO;
import com.ageinghippy.service.PreparationTechniqueService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/preparation-technique")
@RequiredArgsConstructor
public class PreparationTechniqueViewController {

    private final PreparationTechniqueService preparationTechniqueService;

    @GetMapping("")
    public String showPreparationTechniques(Model model) {
        List<PreparationTechniqueDTO> preparationTechniques = preparationTechniqueService.getPreparationTechniques();
        model.addAttribute("preparationTechniques",preparationTechniques);

        return "/preparation-technique";
    }

    @GetMapping(value = "/edit/{code}")
    public String editPreparationTechnique(Model model, @PathVariable String code) {
        PreparationTechniqueDTO preparationTechnique = preparationTechniqueService.getPreparationTechnique(code);

        model.addAttribute("preparationTechnique",preparationTechnique);

        return "/preparation-technique-edit";
    }


    @PostMapping("/update/{code}")
    public String updatePreparationTechnique(@PathVariable String code,
                                             @ModelAttribute PreparationTechniqueDTO preparationTechnique) {
        preparationTechnique = preparationTechniqueService.updatePreparationTechnique(code, preparationTechnique);
        return "redirect:/preparation-technique";
    }

    @GetMapping("/new")
    public String showNewPreparationTechniqueForm(Model model) {
        model.addAttribute("preparationTechnique",new PreparationTechniqueDTO(null,null));
        return "preparation-technique-new";
    }

    @PostMapping("/create")
    public String createPreparationTechnique(@ModelAttribute PreparationTechniqueDTO preparationTechnique) {
        preparationTechnique = preparationTechniqueService.createPreparationTechnique( preparationTechnique);
        return "redirect:/preparation-technique";
    }

    @RequestMapping("/delete/{code}")
    public String deletePreparationTechnique(@PathVariable String code) {
        preparationTechniqueService.deletePreparationTechnique(code);
        return "redirect:/preparation-technique";
    }
}
