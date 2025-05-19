package com.ageinghippy.controller.rest;

import com.ageinghippy.model.dto.PreparationTechniqueDTO;
import com.ageinghippy.service.PreparationTechniqueService;
import com.ageinghippy.service.UserPrincipleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/preparation-techniques")
public class PreparationTechniqueController {

    private final PreparationTechniqueService preparationTechniqueService;
    private final UserPrincipleService userPrincipleService;

    @GetMapping("")
    public List<PreparationTechniqueDTO> getPreparationTechniques(Authentication authentication) {
        return preparationTechniqueService.getPreparationTechniques(
                userPrincipleService.castToUserPrinciple(authentication.getPrincipal()));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasPermission(#id,'PreparationTechnique','read')")
    public PreparationTechniqueDTO getPreparationTechnique(@PathVariable Long id) {
        return preparationTechniqueService.getPreparationTechnique(id);
    }

    @PostMapping("")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<PreparationTechniqueDTO> postPreparationTechnique(
            @Valid @RequestBody PreparationTechniqueDTO preparationTechnique,
            Authentication authentication) {
        preparationTechnique = preparationTechniqueService.createPreparationTechnique(preparationTechnique,
                userPrincipleService.castToUserPrinciple(authentication.getPrincipal()));
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(preparationTechnique.id())
                .toUri();
        return ResponseEntity.created(location).body(preparationTechnique);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasPermission(#id,'PreparationTechnique','edit')")
    public ResponseEntity<PreparationTechniqueDTO> putPreparationTechnique(@RequestBody PreparationTechniqueDTO preparationTechnique, @PathVariable Long id) {
        if (!id.equals(preparationTechnique.id())) {
            throw new IllegalArgumentException("The id specified in the request body must match the id specified in the url");
        }
        return ResponseEntity.ok(preparationTechniqueService.updatePreparationTechnique(id, preparationTechnique))                ;
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasPermission(#id,'PreparationTechnique','edit')")
    public ResponseEntity<String> deletePreparationTechnique(@PathVariable Long id) {
        preparationTechniqueService.deletePreparationTechnique(id);
        return ResponseEntity.noContent().build();
    }

}
