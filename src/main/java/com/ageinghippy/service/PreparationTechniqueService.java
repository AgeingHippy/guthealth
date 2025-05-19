package com.ageinghippy.service;

import com.ageinghippy.model.DTOMapper;
import com.ageinghippy.model.entity.PreparationTechnique;
import com.ageinghippy.model.dto.PreparationTechniqueDTO;
import com.ageinghippy.model.entity.UserPrinciple;
import com.ageinghippy.repository.PreparationTechniqueRepository;
import com.ageinghippy.util.Util;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PreparationTechniqueService {

    private final PreparationTechniqueRepository preparationTechniqueRepository;
    private final DTOMapper dtoMapper;
    private final UserPrincipleService userPrincipleService;

    public PreparationTechniqueDTO getPreparationTechnique(Long id) {
        return dtoMapper.map(preparationTechniqueRepository.findById(id).orElseThrow(), PreparationTechniqueDTO.class);
    }

    //todo - limit by principle?
    public List<PreparationTechniqueDTO> getPreparationTechniques(UserPrinciple principle) {
        return dtoMapper.mapList(preparationTechniqueRepository.findAllByPrinciple(principle), PreparationTechniqueDTO.class);
    }

    public PreparationTechniqueDTO createPreparationTechnique(PreparationTechniqueDTO preparationTechnique, UserPrinciple principle) {
        if (preparationTechnique.id() != null) {
            throw new IllegalArgumentException("Preparation Technique ID cannot be specified on new record");
        }
        PreparationTechnique newPreparationTechnique = dtoMapper.map(preparationTechnique, PreparationTechnique.class);

        newPreparationTechnique.setPrinciple(principle);

        newPreparationTechnique = savePreparationTechnique(newPreparationTechnique);

        return dtoMapper.map(newPreparationTechnique, PreparationTechniqueDTO.class);
    }

    protected PreparationTechnique savePreparationTechnique(PreparationTechnique preparationTechnique) {
        return preparationTechniqueRepository.save(preparationTechnique);
    }

    public PreparationTechniqueDTO updatePreparationTechnique(Long id, PreparationTechniqueDTO updatePreparationTechnique) {
        if (!id.equals(updatePreparationTechnique.id())) {
            throw new IllegalArgumentException("Specified and update ids do not match");
        }

        PreparationTechnique preparationTechnique = preparationTechniqueRepository.findById(id).orElseThrow();

        preparationTechnique.setCode(
                Util.valueIfNull(updatePreparationTechnique.code(), preparationTechnique.getCode()));
        preparationTechnique.setDescription(
                Util.valueIfNull(updatePreparationTechnique.description(), preparationTechnique.getDescription()));

        preparationTechnique = savePreparationTechnique(preparationTechnique);

        return dtoMapper.map(preparationTechnique, PreparationTechniqueDTO.class);
    }

    public void deletePreparationTechnique(Long id) {
        PreparationTechnique preparationTechnique = preparationTechniqueRepository.findById(id).orElseThrow();
        deletePreparationTechnique(preparationTechnique);
    }

    protected void deletePreparationTechnique(PreparationTechnique preparationTechnique) {
        preparationTechniqueRepository.delete(preparationTechnique);
    }

    public void copySystemPreparationTechniques(UserPrinciple principle) {
        UserPrinciple systemUserPrinciple = userPrincipleService.loadUserByUsername("system");
        List<PreparationTechnique> systemPreparationTechniques =
                preparationTechniqueRepository.findAllByPrinciple(systemUserPrinciple);
        systemPreparationTechniques.forEach(pt -> copySystemPreparationTechnique(pt.getId(), principle));
    }


    @Transactional
    public PreparationTechniqueDTO copySystemPreparationTechnique(Long id, UserPrinciple principle) {
        PreparationTechnique systemPreparationTechnique =
                preparationTechniqueRepository.findById(id).orElseThrow();

        PreparationTechnique targetPreparationTechnique =
                preparationTechniqueRepository.findByCodeAndPrinciple(systemPreparationTechnique.getCode(), principle).orElse(null);
        if (targetPreparationTechnique == null) {
            targetPreparationTechnique =
                    PreparationTechnique.builder()
                            .code(systemPreparationTechnique.getCode())
                            .description(systemPreparationTechnique.getDescription())
                            .principle(principle)
                            .build();
            targetPreparationTechnique = savePreparationTechnique(targetPreparationTechnique);
        }

        return dtoMapper.map(targetPreparationTechnique, PreparationTechniqueDTO.class);
    }
}
