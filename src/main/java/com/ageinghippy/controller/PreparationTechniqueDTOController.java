package com.ageinghippy.controller;

import com.ageinghippy.model.dto.PreparationTechniqueDTO;
import com.ageinghippy.service.PreparationTechniqueService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/dto/preparation-techniques")
@RequiredArgsConstructor
public class PreparationTechniqueDTOController {

    private final PreparationTechniqueService preparationTechniqueService;

    @GetMapping
    public List<PreparationTechniqueDTO> getPreparationTechniques() {
        return preparationTechniqueService.getPreparationTechniquesDto();
    }

    @GetMapping("/{code}")
    public PreparationTechniqueDTO getPreparationTechnique(@PathVariable(name = "code") String code) {
        return preparationTechniqueService.getPreparationTechniqueDto(code);
    }

    @PostMapping
    public PreparationTechniqueDTO createPreparationTechnique(@RequestBody PreparationTechniqueDTO preparationTechniqueDTO) {
        return preparationTechniqueService.createPreparationTechniqueDto(preparationTechniqueDTO);
    }

}
