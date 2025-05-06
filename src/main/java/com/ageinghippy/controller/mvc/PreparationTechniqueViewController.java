package com.ageinghippy.controller.mvc;

import com.ageinghippy.model.dto.PreparationTechniqueDTO;
import com.ageinghippy.service.PreparationTechniqueService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/preparation-technique")
@RequiredArgsConstructor
public class PreparationTechniqueViewController {

    private final PreparationTechniqueService preparationTechniqueService;

    @GetMapping("")
    public String showPreparationTechniques(Model model) {
        List<PreparationTechniqueDTO> preparationTechniques = preparationTechniqueService.getPreparationTechniques();
        model.addAttribute("preparationTechniques", preparationTechniques);

        return "/preparation-technique";
    }

    @GetMapping(value = "/edit/{code}")
    public String editPreparationTechnique(Model model, @PathVariable String code) {
        if (!model.containsAttribute("preparationTechnique")) {
            PreparationTechniqueDTO preparationTechnique = preparationTechniqueService.getPreparationTechnique(code);

            model.addAttribute("preparationTechnique", preparationTechnique);
        }

        return "/preparation-technique-edit";
    }


    @PostMapping("/update/{code}")
    public String updatePreparationTechnique(@PathVariable String code,
                                             @ModelAttribute PreparationTechniqueDTO preparationTechnique,
                                             RedirectAttributes redirectAttributes) {
        try {
            preparationTechniqueService.updatePreparationTechnique(code, preparationTechnique);
            redirectAttributes.addFlashAttribute("successMessage", "PreparationTechnique updated successfully.");
            return "redirect:/preparation-technique";
        } catch (Exception e) {
            String errorMessage = e.getClass().getSimpleName() + " - " + "PreparationTechnique update failed.";
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
            redirectAttributes.addFlashAttribute("preparationTechnique", preparationTechnique);
            return "redirect:/preparation-technique/edit/" + code;
        }

    }

    @GetMapping("/new")
    public String showNewPreparationTechniqueForm(Model model) {
        if (!model.containsAttribute("preparationTechnique")) {
            model.addAttribute("preparationTechnique", new PreparationTechniqueDTO(null, null));
        }

        return "preparation-technique-new";
    }

    @PostMapping("/create")
    public String createPreparationTechnique(@ModelAttribute PreparationTechniqueDTO preparationTechnique,
                                             RedirectAttributes redirectAttributes) {
        try {
            preparationTechniqueService.createPreparationTechnique(preparationTechnique);
            redirectAttributes.addFlashAttribute("successMessage", "PreparationTechnique created successfully.");
            return "redirect:/preparation-technique";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    e.getClass().getSimpleName() + "- " + "PreparationTechnique create failed.");
            redirectAttributes.addFlashAttribute("preparationTechnique",preparationTechnique);
            return "redirect:/preparation-technique/new";
        }

    }

    @RequestMapping("/delete/{code}")
    public String deletePreparationTechnique(@PathVariable String code,
                                             RedirectAttributes redirectAttributes) {
        try {
            preparationTechniqueService.deletePreparationTechnique(code);
            redirectAttributes.addFlashAttribute("successMessage", "PreparationTechnique deleted successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    e.getClass().getSimpleName() + "- " + "PreparationTechnique deletion failed.");
        }

        return "redirect:/preparation-technique";
    }
}
