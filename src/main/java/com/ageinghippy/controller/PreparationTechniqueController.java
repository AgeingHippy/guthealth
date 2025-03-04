package com.ageinghippy.controller;

import com.ageinghippy.model.PreparationTechnique;
import com.ageinghippy.service.PreparationTechniqueService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/preparation-techniques")
public class PreparationTechniqueController {

    private final PreparationTechniqueService preparationTechniqueService;

    @GetMapping("")
    public ArrayList<PreparationTechnique> getPreparationTechniques() {
        return preparationTechniqueService.getPreparationTechniques();
    }

    @GetMapping("/{code}")
    public PreparationTechnique getPreparationTechnique(@PathVariable String code) {
        return preparationTechniqueService.getPreparationTechnique(code);
    }

    @PostMapping("")
    public PreparationTechnique postPreparationTechnique(@Valid @RequestBody PreparationTechnique preparationTechnique) {
        return preparationTechniqueService.savePreparationTechnique(preparationTechnique);
    }

    @PutMapping("/{code}")
    public PreparationTechnique putPreparationTechnique(@RequestBody PreparationTechnique preparationTechnique, @PathVariable String code) {
        return preparationTechniqueService.updatePreparationTechnique(code, preparationTechnique);
    }

    @DeleteMapping("/{code}")
    public ResponseEntity<String> deletePreparationTechnique(@PathVariable String code) {
        PreparationTechnique preparationTechnique = preparationTechniqueService.getPreparationTechnique(code);
        if (preparationTechnique != null) {
            preparationTechniqueService.deletePreparationTechnique(preparationTechnique);
            return ResponseEntity.noContent().build();
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }

}
