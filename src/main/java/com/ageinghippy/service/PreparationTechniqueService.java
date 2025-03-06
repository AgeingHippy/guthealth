package com.ageinghippy.service;

import com.ageinghippy.model.MyMapper;
import com.ageinghippy.model.entity.PreparationTechnique;
import com.ageinghippy.model.dto.PreparationTechniqueDTO;
import com.ageinghippy.repository.PreparationTechniqueRepository;
import com.ageinghippy.util.Util;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PreparationTechniqueService {

    private final PreparationTechniqueRepository preparationTechniqueRepository;
    private final MyMapper myMapper;

    public List<PreparationTechniqueDTO> getPreparationTechniquesDto() {
        List<PreparationTechnique> preparationTechniques = preparationTechniqueRepository.findAll();
        return myMapper.mapList(preparationTechniques, PreparationTechniqueDTO.class);
    }

    public PreparationTechniqueDTO getPreparationTechniqueDto(String code) {
        PreparationTechnique preparationTechnique = preparationTechniqueRepository.findById(code).orElseThrow();
        return myMapper.map(preparationTechnique, PreparationTechniqueDTO.class);
    }

    public PreparationTechniqueDTO createPreparationTechniqueDto(PreparationTechniqueDTO preparationTechniqueDTO) {
        PreparationTechnique preparationTechnique = myMapper.map(preparationTechniqueDTO, PreparationTechnique.class);
        preparationTechnique = savePreparationTechnique(preparationTechnique);
        return myMapper.map(preparationTechnique, PreparationTechniqueDTO.class);
    }

    public PreparationTechnique getPreparationTechnique(String code) {
        return preparationTechniqueRepository.findById(code).orElseThrow();
    }

    public ArrayList<PreparationTechnique> getPreparationTechniques() {
        return (ArrayList<PreparationTechnique>) preparationTechniqueRepository.findAll();
    }

    public PreparationTechnique createPreparationTechnique(PreparationTechnique preparationTechnique) {
        if (preparationTechniqueRepository.findById(preparationTechnique.getCode()).orElse(null) != null) {
            throw new IllegalArgumentException("Preparation Technique with code '"+ preparationTechnique.getCode() + "' already exists");
        }
        return savePreparationTechnique(preparationTechnique);
    }

    private PreparationTechnique savePreparationTechnique(PreparationTechnique preparationTechnique) {
        return preparationTechniqueRepository.save(preparationTechnique);
    }

    public PreparationTechnique updatePreparationTechnique(String code, PreparationTechnique updatePreparationTechnique) {
        PreparationTechnique preparationTechnique = preparationTechniqueRepository.findById(code).orElseThrow();
        preparationTechnique.setDescription(Util.valueIfNull(updatePreparationTechnique.getDescription(), preparationTechnique.getDescription()));
        return savePreparationTechnique(preparationTechnique);
    }

    public void deletePreparationTechnique(String code) {
        PreparationTechnique preparationTechnique = preparationTechniqueRepository.findById(code).orElseThrow();
        deletePreparationTechnique(preparationTechnique);
    }

    public void deletePreparationTechnique(PreparationTechnique preparationTechnique) {
        preparationTechniqueRepository.delete(preparationTechnique);
    }
}
