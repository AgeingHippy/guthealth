package com.ageinghippy.service;

import com.ageinghippy.model.PreparationTechnique;
import com.ageinghippy.repository.PreparationTechniqueRepository;
import com.ageinghippy.util.Util;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class PreparationTechniqueService {

    private final PreparationTechniqueRepository preparationTechniqueRepository;

    public PreparationTechnique getPreparationTechnique(String code) {
        return preparationTechniqueRepository.findById(code).orElse(null);
    }

    public ArrayList<PreparationTechnique> getPreparationTechniques() {
        return (ArrayList<PreparationTechnique>) preparationTechniqueRepository.findAll();
    }

    public PreparationTechnique savePreparationTechnique(PreparationTechnique preparationTechnique) {
        return preparationTechniqueRepository.save(preparationTechnique);
    }

    public PreparationTechnique updatePreparationTechnique(String code, PreparationTechnique updatePreparationTechnique) {
        PreparationTechnique preparationTechnique = preparationTechniqueRepository.findById(code).orElseThrow();
        preparationTechnique.setDescription(Util.valueIfNull(updatePreparationTechnique.getDescription(), preparationTechnique.getDescription()));
        return savePreparationTechnique(preparationTechnique);
    }

    public void deletePreparationTechnique(PreparationTechnique preparationTechnique) {
        preparationTechniqueRepository.delete(preparationTechnique);
    }
}
