package com.ageinghippy.controller.mvc;

import com.ageinghippy.model.dto.FoodCategoryDTOSimple;
import com.ageinghippy.model.dto.PreparationTechniqueDTO;
import com.ageinghippy.service.PreparationTechniqueService;
import com.ageinghippy.service.UserPrincipleService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
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
    private final UserPrincipleService userPrincipleService;

    @GetMapping("")
    public String showPreparationTechniques(Model model, Authentication authentication) {
        List<PreparationTechniqueDTO> preparationTechniques =
                preparationTechniqueService.getPreparationTechniques(
                        userPrincipleService.castToUserPrinciple(authentication.getPrincipal())
                );
        model.addAttribute("preparationTechniques", preparationTechniques);

        return "/preparation-technique";
    }

    @GetMapping("/system")
    @PreAuthorize("hasRole('GUEST')")
    public String showSystemPreparationTechniques(Model model) {
        List<PreparationTechniqueDTO> preparationTechniques =
                preparationTechniqueService.getPreparationTechniques(
                        userPrincipleService.loadUserByUsername("system"));

        model.addAttribute("preparationTechniques", preparationTechniques);

        return "/preparation-technique-system";
    }

    @GetMapping(value = "/edit/{id}")
    @PreAuthorize("hasPermission(#id,'PreparationTechnique','edit')")
    public String editPreparationTechnique(Model model, @PathVariable Long id) {
        if (!model.containsAttribute("preparationTechnique")) {
            PreparationTechniqueDTO preparationTechnique = preparationTechniqueService.getPreparationTechnique(id);

            model.addAttribute("preparationTechnique", preparationTechnique);
        }

        return "/preparation-technique-edit";
    }


    @PostMapping("/update/{id}")
    @PreAuthorize("hasPermission(#id,'PreparationTechnique','edit')")
    public String updatePreparationTechnique(@PathVariable Long id,
                                             @ModelAttribute PreparationTechniqueDTO preparationTechnique,
                                             RedirectAttributes redirectAttributes) {
        try {
            preparationTechniqueService.updatePreparationTechnique(id, preparationTechnique);
            redirectAttributes.addFlashAttribute("successMessage", "PreparationTechnique updated successfully.");
            return "redirect:/preparation-technique";
        } catch (Exception e) {
            String errorMessage = e.getClass().getSimpleName() + " - " + "PreparationTechnique update failed.";
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
            redirectAttributes.addFlashAttribute("preparationTechnique", preparationTechnique);
            return "redirect:/preparation-technique/edit/" + id;
        }

    }

    @GetMapping("/new")
    @PreAuthorize("hasRole('USER)")
    public String showNewPreparationTechniqueForm(Model model) {
        if (!model.containsAttribute("preparationTechnique")) {
            model.addAttribute(
                    "preparationTechnique",
                    new PreparationTechniqueDTO(null, null, null));
        }

        return "preparation-technique-new";
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('USER)")
    public String createPreparationTechnique(@ModelAttribute PreparationTechniqueDTO preparationTechnique,
                                             Authentication authentication,
                                             RedirectAttributes redirectAttributes) {
        try {
            preparationTechniqueService.createPreparationTechnique(
                    preparationTechnique,
                    userPrincipleService.castToUserPrinciple(authentication.getPrincipal()));
            redirectAttributes.addFlashAttribute("successMessage", "PreparationTechnique created successfully.");
            return "redirect:/preparation-technique";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    e.getClass().getSimpleName() + "- " + "PreparationTechnique create failed.");
            redirectAttributes.addFlashAttribute("preparationTechnique", preparationTechnique);
            return "redirect:/preparation-technique/new";
        }

    }

    @RequestMapping("/delete/{id}")
    @PreAuthorize("hasPermission(#id,'PreparationTechnique','delete')")
    public String deletePreparationTechnique(@PathVariable Long id,
                                             RedirectAttributes redirectAttributes) {
        try {
            preparationTechniqueService.deletePreparationTechnique(id);
            redirectAttributes.addFlashAttribute("successMessage", "PreparationTechnique deleted successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    e.getClass().getSimpleName() + "- " + "PreparationTechnique deletion failed.");
        }

        return "redirect:/preparation-technique";
    }
}
