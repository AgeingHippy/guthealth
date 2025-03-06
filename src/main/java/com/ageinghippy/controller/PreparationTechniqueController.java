package com.ageinghippy.controller;

import com.ageinghippy.model.entity.PreparationTechnique;
import com.ageinghippy.service.PreparationTechniqueService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
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
    public ResponseEntity<PreparationTechnique> postPreparationTechnique(@Valid @RequestBody PreparationTechnique preparationTechnique) {
        preparationTechnique = preparationTechniqueService.createPreparationTechnique(preparationTechnique);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{code}")
                .buildAndExpand(preparationTechnique.getCode())
                .toUri();
        return ResponseEntity.created(location).body(preparationTechnique);
    }

    @PutMapping("/{code}")
    public PreparationTechnique putPreparationTechnique(@RequestBody PreparationTechnique preparationTechnique, @PathVariable String code) {
        if (preparationTechnique.getCode() != null) {
            throw new IllegalArgumentException("Preparation Technique code cannot be specified in body");
        }
        return preparationTechniqueService.updatePreparationTechnique(code, preparationTechnique);
    }

    @DeleteMapping("/{code}")
    public ResponseEntity<String> deletePreparationTechnique(@PathVariable String code) {
        preparationTechniqueService.deletePreparationTechnique(code);
        return ResponseEntity.noContent().build();
    }

}
