package com.ageinghippy.controller.rest;

import com.ageinghippy.model.dto.PreparationTechniqueDTO;
import com.ageinghippy.service.PreparationTechniqueService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/preparation-techniques")
public class PreparationTechniqueController {

    private final PreparationTechniqueService preparationTechniqueService;

    @GetMapping("")
    public List<PreparationTechniqueDTO> getPreparationTechniques() {
        return preparationTechniqueService.getPreparationTechniques();
    }

    @GetMapping("/{code}")
    public PreparationTechniqueDTO getPreparationTechnique(@PathVariable String code) {
        return preparationTechniqueService.getPreparationTechnique(code);
    }

    @PostMapping("")
    public ResponseEntity<PreparationTechniqueDTO> postPreparationTechnique(@Valid @RequestBody PreparationTechniqueDTO preparationTechnique) {
        preparationTechnique = preparationTechniqueService.createPreparationTechnique(preparationTechnique);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{code}")
                .buildAndExpand(preparationTechnique.code())
                .toUri();
        return ResponseEntity.created(location).body(preparationTechnique);
    }

    @PutMapping("/{code}")
    public ResponseEntity<PreparationTechniqueDTO> putPreparationTechnique(@RequestBody PreparationTechniqueDTO preparationTechnique, @PathVariable String code) {
        if (!code.equals(preparationTechnique.code())) {
            throw new IllegalArgumentException("The code specified in the request body must match the code specified in the url");
        }
        return ResponseEntity.ok(preparationTechniqueService.updatePreparationTechnique(code, preparationTechnique))                ;
    }

    @DeleteMapping("/{code}")
    public ResponseEntity<String> deletePreparationTechnique(@PathVariable String code) {
        preparationTechniqueService.deletePreparationTechnique(code);
        return ResponseEntity.noContent().build();
    }

}
