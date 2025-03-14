package com.ageinghippy.service;

import com.ageinghippy.model.DTOMapper;
import com.ageinghippy.model.entity.PreparationTechnique;
import com.ageinghippy.model.dto.PreparationTechniqueDTO;
import com.ageinghippy.repository.PreparationTechniqueRepository;
import com.ageinghippy.util.Util;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PreparationTechniqueService {

    private final PreparationTechniqueRepository preparationTechniqueRepository;
    private final DTOMapper DTOMapper;

    public PreparationTechniqueDTO getPreparationTechnique(String code) {
        return DTOMapper.map(preparationTechniqueRepository.findById(code).orElseThrow(), PreparationTechniqueDTO.class);
    }

    public List<PreparationTechniqueDTO> getPreparationTechniques() {
        return DTOMapper.mapList(preparationTechniqueRepository.findAll(), PreparationTechniqueDTO.class);
    }

    public PreparationTechniqueDTO createPreparationTechnique(PreparationTechniqueDTO preparationTechnique) {
        PreparationTechnique newPreparationTechnique = DTOMapper.map(preparationTechnique, PreparationTechnique.class);

        if (preparationTechniqueRepository.findById(newPreparationTechnique.getCode()).orElse(null) != null) {
            throw new IllegalArgumentException("Preparation Technique with code '" + newPreparationTechnique.getCode() + "' already exists");
        }
        newPreparationTechnique = savePreparationTechnique(newPreparationTechnique);

        return DTOMapper.map(newPreparationTechnique, PreparationTechniqueDTO.class);
    }

    private PreparationTechnique savePreparationTechnique(PreparationTechnique preparationTechnique) {
        return preparationTechniqueRepository.save(preparationTechnique);
    }

    public PreparationTechniqueDTO updatePreparationTechnique(String code, PreparationTechniqueDTO updatePreparationTechnique) {

        PreparationTechnique preparationTechnique = preparationTechniqueRepository.findById(code).orElseThrow();
        preparationTechnique.setDescription(
                Util.valueIfNull(updatePreparationTechnique.description(), preparationTechnique.getDescription()));
        preparationTechnique = savePreparationTechnique(preparationTechnique);

        return DTOMapper.map(preparationTechnique, PreparationTechniqueDTO.class);
    }

    public void deletePreparationTechnique(String code) {
        PreparationTechnique preparationTechnique = preparationTechniqueRepository.findById(code).orElseThrow();
        deletePreparationTechnique(preparationTechnique);
    }

    public void deletePreparationTechnique(PreparationTechnique preparationTechnique) {
        preparationTechniqueRepository.delete(preparationTechnique);
    }
}
