package com.ageinghippy.service;

import com.ageinghippy.DataSetupHelper;
import com.ageinghippy.model.DTOMapper;
import com.ageinghippy.model.dto.PreparationTechniqueDTO;
import com.ageinghippy.model.entity.PreparationTechnique;
import com.ageinghippy.repository.PreparationTechniqueRepository;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PreparationTechniqueServiceTest {
    @Mock
    private PreparationTechniqueRepository preparationTechniqueRepository;
    @Mock
    private DTOMapper dtoMapper;

    private PreparationTechniqueService service;

    private DataSetupHelper dsh = new DataSetupHelper();

    @BeforeEach
    void setUp() {
        PreparationTechniqueService preparationTechniqueService =
                new PreparationTechniqueService(preparationTechniqueRepository, dtoMapper);
        service = spy(preparationTechniqueService);
    }

    @Test
    public void getPreparationTechnique_success() {
        PreparationTechnique preparationTechnique = dsh.getPreparationTechnique("PrepType1");
        PreparationTechniqueDTO preparationTechniqueDTO = dsh.getPreparationTechniqueDTO("PrepType1");

        when(preparationTechniqueRepository.findById("PrepType1")).thenReturn(Optional.of(preparationTechnique));
        when(dtoMapper.map(preparationTechnique, PreparationTechniqueDTO.class)).thenReturn(preparationTechniqueDTO);

        assertEquals(preparationTechniqueDTO, service.getPreparationTechnique("PrepType1"));
    }

    @Test
    public void getPreparationTechnique_notFound() {
        when(preparationTechniqueRepository.findById("PrepType99")).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> service.getPreparationTechnique("PrepType99"));
    }

    @Test
    public void getPreparationTechniques_success() {
        List<PreparationTechnique> preparationTechniqueList = List.of(
                dsh.getPreparationTechnique("PrepType1"),
                dsh.getPreparationTechnique("PrepType2"),
                dsh.getPreparationTechnique("PrepType3"),
                dsh.getPreparationTechnique("PrepType4")
        );

        List<PreparationTechniqueDTO> preparationTechniqueDTOList = List.of(
                dsh.getPreparationTechniqueDTO("PrepType1"),
                dsh.getPreparationTechniqueDTO("PrepType2"),
                dsh.getPreparationTechniqueDTO("PrepType3"),
                dsh.getPreparationTechniqueDTO("PrepType4")
        );

        when(preparationTechniqueRepository.findAll()).thenReturn(preparationTechniqueList);

        when(dtoMapper.mapList(preparationTechniqueList,PreparationTechniqueDTO.class)).thenReturn(preparationTechniqueDTOList);

        assertEquals(preparationTechniqueDTOList, service.getPreparationTechniques());
    }

    @Test
    public void createPreparationTechnique_success() {
        PreparationTechniqueDTO preparationTechniqueDTO = dsh.getPreparationTechniqueDTO("PrepType1");
        PreparationTechnique preparationTechnique = dsh.getPreparationTechnique("PrepType1");

        when(dtoMapper.map(preparationTechniqueDTO, PreparationTechnique.class)).thenReturn(preparationTechnique);
        when(preparationTechniqueRepository.findById("PrepType1")).thenReturn(Optional.empty());

        when(service.savePreparationTechnique(any(PreparationTechnique.class))).then(returnsFirstArg());

        when(dtoMapper.map(preparationTechnique, PreparationTechniqueDTO.class)).thenReturn(preparationTechniqueDTO);

        assertEquals(service.createPreparationTechnique(preparationTechniqueDTO),preparationTechniqueDTO);
    }

    @Test
    public void createPreparationTechnique_exists() {
        PreparationTechniqueDTO preparationTechniqueDTO = dsh.getPreparationTechniqueDTO("PrepType1");
        PreparationTechnique preparationTechnique = dsh.getPreparationTechnique("PrepType1");

        when(dtoMapper.map(preparationTechniqueDTO, PreparationTechnique.class)).thenReturn(preparationTechnique);
        when(preparationTechniqueRepository.findById("PrepType1")).thenReturn(Optional.of(preparationTechnique));

        assertThrows(ConstraintViolationException.class, () -> service.createPreparationTechnique(preparationTechniqueDTO));
    }

    @Test
    public void updatePreparationTechnique_success() {
        PreparationTechniqueDTO newPreparationTechniqueDTO =
                new PreparationTechniqueDTO("PrepType1","new description");
        PreparationTechnique preparationTechnique = dsh.getPreparationTechnique("PrepType1");

        when(preparationTechniqueRepository.findById("PrepType1")).thenReturn(Optional.of(preparationTechnique));

        when(service.savePreparationTechnique(any())).then(returnsFirstArg());

        when(dtoMapper.map(any(),any())).thenReturn(newPreparationTechniqueDTO);

        assertEquals(newPreparationTechniqueDTO, service.updatePreparationTechnique("PrepType1", newPreparationTechniqueDTO));
    }

    @Test
    public void updatePreparationTechnique_success_noChange() {
        PreparationTechniqueDTO newPreparationTechniqueDTO =
                new PreparationTechniqueDTO("PrepType1",null);
        PreparationTechnique preparationTechnique = dsh.getPreparationTechnique("PrepType1");
        PreparationTechniqueDTO expectedPreparationTechniqueDTO = dsh.getPreparationTechniqueDTO("PrepType1");

        when(preparationTechniqueRepository.findById("PrepType1")).thenReturn(Optional.of(preparationTechnique));
        when(service.savePreparationTechnique(preparationTechnique)).thenReturn(preparationTechnique);
        when(dtoMapper.map(preparationTechnique,PreparationTechniqueDTO.class)).thenReturn(expectedPreparationTechniqueDTO);

        assertEquals(expectedPreparationTechniqueDTO, service.updatePreparationTechnique("PrepType1", newPreparationTechniqueDTO));
    }

    @Test
    public void updatePreparationTechnique_failure() {
        PreparationTechniqueDTO newPreparationTechniqueDTO =
                new PreparationTechniqueDTO("PrepType2","new description");

        assertThrows(IllegalArgumentException.class,
                () -> service.updatePreparationTechnique("PrepType1", newPreparationTechniqueDTO));
    }

    @Test
    public void deletePreparationTechnique_success() {
        PreparationTechnique preparationTechnique = dsh.getPreparationTechnique("PrepType1");
        when(preparationTechniqueRepository.findById("PrepType1")).thenReturn(Optional.of(preparationTechnique));
        doNothing().when(service).deletePreparationTechnique(preparationTechnique);

        service.deletePreparationTechnique("PrepType1");

        verify(service,times(1)).deletePreparationTechnique(preparationTechnique);
    }

    @Test
    public void deletePreparationTechnique_notFound() {
        when(preparationTechniqueRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class,() -> service.deletePreparationTechnique("PrepType99"));
    }

    @Test
    public void deletePreparationTechnique_protected() {
        PreparationTechnique preparationTechnique = new PreparationTechnique();

        doNothing().when(preparationTechniqueRepository).delete(preparationTechnique);

        service.deletePreparationTechnique(preparationTechnique);

        verify(preparationTechniqueRepository, times(1)).delete(preparationTechnique);
    }

    @Test
    public void savePreparationTechnique_protected() {
        PreparationTechnique preparationTechnique = dsh.getPreparationTechnique("PrepType1");

        when(preparationTechniqueRepository.save(preparationTechnique)).thenReturn(preparationTechnique);

        assertEquals(preparationTechnique, service.savePreparationTechnique(preparationTechnique));
    }
}