package com.ageinghippy.service;

import com.ageinghippy.DataSetupHelper;
import com.ageinghippy.model.DTOMapper;
import com.ageinghippy.model.dto.PreparationTechniqueDTO;
import com.ageinghippy.model.entity.PreparationTechnique;
import com.ageinghippy.model.entity.UserPrinciple;
import com.ageinghippy.repository.PreparationTechniqueRepository;
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
    @Mock
    private UserPrincipleService userPrincipleService;

    private PreparationTechniqueService service;

    private DataSetupHelper dsh = new DataSetupHelper();

    @BeforeEach
    void setUp() {
        PreparationTechniqueService preparationTechniqueService =
                new PreparationTechniqueService(preparationTechniqueRepository, dtoMapper, userPrincipleService);
        service = spy(preparationTechniqueService);
    }

    @Test
    public void getPreparationTechnique_success() {
        PreparationTechnique preparationTechnique = dsh.getPreparationTechnique(1L);
        PreparationTechniqueDTO preparationTechniqueDTO = dsh.getPreparationTechniqueDTO(1L);

        when(preparationTechniqueRepository.findById(1L)).thenReturn(Optional.of(preparationTechnique));
        when(dtoMapper.map(preparationTechnique, PreparationTechniqueDTO.class)).thenReturn(preparationTechniqueDTO);

        assertEquals(preparationTechniqueDTO, service.getPreparationTechnique(1L));
    }

    @Test
    public void getPreparationTechnique_notFound() {
        when(preparationTechniqueRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> service.getPreparationTechnique(99L));
    }

    @Test
    public void getPreparationTechniques_success() {
        List<PreparationTechnique> preparationTechniqueList = List.of(
                dsh.getPreparationTechnique(1L),
                dsh.getPreparationTechnique(2L),
                dsh.getPreparationTechnique(3L),
                dsh.getPreparationTechnique(4L)
        );

        List<PreparationTechniqueDTO> preparationTechniqueDTOList = List.of(
                dsh.getPreparationTechniqueDTO(1L),
                dsh.getPreparationTechniqueDTO(2L),
                dsh.getPreparationTechniqueDTO(3L),
                dsh.getPreparationTechniqueDTO(4L)
        );

        when(preparationTechniqueRepository.findAllByPrinciple(any(UserPrinciple.class))).thenReturn(preparationTechniqueList);

        when(dtoMapper.mapList(preparationTechniqueList, PreparationTechniqueDTO.class)).thenReturn(preparationTechniqueDTOList);

        assertEquals(preparationTechniqueDTOList, service.getPreparationTechniques(UserPrinciple.builder().build()));
    }

    @Test
    public void createPreparationTechnique_success() {
        UserPrinciple principle = new UserPrinciple();
        PreparationTechniqueDTO preparationTechniqueDTO = dsh.getPreparationTechniqueDTO(1L);
        PreparationTechnique preparationTechnique = dsh.getPreparationTechnique(1L);
        PreparationTechniqueDTO newPreparationTechniqueDTO =
                new PreparationTechniqueDTO(null, preparationTechniqueDTO.code(), preparationTechniqueDTO.description());
        PreparationTechnique newPreparationTechnique = PreparationTechnique.builder()
                .code(newPreparationTechniqueDTO.code())
                .description(newPreparationTechniqueDTO.description())
                .build();

        lenient().when(dtoMapper.map(newPreparationTechniqueDTO, PreparationTechnique.class)).thenReturn(newPreparationTechnique);
        when(service.savePreparationTechnique(any(PreparationTechnique.class)))
                .thenAnswer(invocation -> {
                    assertEquals(principle, ((PreparationTechnique) invocation.getArgument(0)).getPrinciple());
                    return preparationTechnique;
                });
        lenient().when(dtoMapper.map(preparationTechnique, PreparationTechniqueDTO.class))
                .thenReturn(preparationTechniqueDTO);

        assertEquals(
                preparationTechniqueDTO,
                service.createPreparationTechnique(newPreparationTechniqueDTO, principle));

        verify(service, times(1)).savePreparationTechnique(any(PreparationTechnique.class));
        verify(dtoMapper, times(1)).map(newPreparationTechniqueDTO, PreparationTechnique.class);
        verify(dtoMapper, times(1)).map(preparationTechnique, PreparationTechniqueDTO.class);
    }

    @Test
    public void updatePreparationTechnique_success() {
        PreparationTechniqueDTO newPreparationTechniqueDTO =
                new PreparationTechniqueDTO(1L, "PrepType1", "new description");
        PreparationTechnique preparationTechnique = dsh.getPreparationTechnique(1L);

        when(preparationTechniqueRepository.findById(1L)).thenReturn(Optional.of(preparationTechnique));

        when(service.savePreparationTechnique(any())).then(returnsFirstArg());

        when(dtoMapper.map(any(), any())).thenReturn(newPreparationTechniqueDTO);

        assertEquals(newPreparationTechniqueDTO, service.updatePreparationTechnique(1L, newPreparationTechniqueDTO));
    }

    @Test
    public void updatePreparationTechnique_success_noChange() {
        PreparationTechniqueDTO newPreparationTechniqueDTO =
                new PreparationTechniqueDTO(1L, null, null);
        PreparationTechnique preparationTechnique = dsh.getPreparationTechnique(1L);
        PreparationTechniqueDTO expectedPreparationTechniqueDTO = dsh.getPreparationTechniqueDTO(1L);

        when(preparationTechniqueRepository.findById(1L)).thenReturn(Optional.of(preparationTechnique));
        when(service.savePreparationTechnique(preparationTechnique)).thenReturn(preparationTechnique);
        when(dtoMapper.map(preparationTechnique, PreparationTechniqueDTO.class)).thenReturn(expectedPreparationTechniqueDTO);

        assertEquals(expectedPreparationTechniqueDTO, service.updatePreparationTechnique(1L, newPreparationTechniqueDTO));
    }

    @Test
    public void updatePreparationTechnique_failure() {
        PreparationTechniqueDTO newPreparationTechniqueDTO =
                new PreparationTechniqueDTO(2L, "PrepType2", "new description");

        assertThrows(IllegalArgumentException.class,
                () -> service.updatePreparationTechnique(1L, newPreparationTechniqueDTO));
    }

    @Test
    public void deletePreparationTechnique_success() {
        PreparationTechnique preparationTechnique = dsh.getPreparationTechnique(1L);
        when(preparationTechniqueRepository.findById(1L)).thenReturn(Optional.of(preparationTechnique));
        doNothing().when(service).deletePreparationTechnique(preparationTechnique);

        service.deletePreparationTechnique(1L);

        verify(service, times(1)).deletePreparationTechnique(preparationTechnique);
    }

    @Test
    public void deletePreparationTechnique_notFound() {
        when(preparationTechniqueRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> service.deletePreparationTechnique(99L));
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
        PreparationTechnique preparationTechnique = dsh.getPreparationTechnique(1L);

        when(preparationTechniqueRepository.save(preparationTechnique)).thenReturn(preparationTechnique);

        assertEquals(preparationTechnique, service.savePreparationTechnique(preparationTechnique));
    }

    @Test
    void copyNewSystemPreparationTechnique() {
        UserPrinciple principle = dsh.getPrinciple("basic");
        PreparationTechnique systemPreparationTechnique = dsh.getPreparationTechnique(5L);

        lenient().when(preparationTechniqueRepository.findById(5L)).thenReturn(Optional.of(systemPreparationTechnique));
        lenient().when(preparationTechniqueRepository.save(any(PreparationTechnique.class)))
                .thenAnswer(invocation -> {
                    PreparationTechnique request = invocation.getArgument(0, PreparationTechnique.class);
                    assertEquals(principle, request.getPrinciple());
                    assertEquals(systemPreparationTechnique.getCode(), request.getCode());
                    assertEquals(systemPreparationTechnique.getDescription(), request.getDescription());
                    return request;
                });
        when(dtoMapper.map(any(), any()))
                .thenReturn(new PreparationTechniqueDTO(null, null, null));

        PreparationTechniqueDTO preparationTechniqueDTO = service.copySystemPreparationTechnique(5L, principle);

        verify(preparationTechniqueRepository, times(1)).findById(5L);
        verify(preparationTechniqueRepository, times(1)).save(any(PreparationTechnique.class));
        verify(dtoMapper, times(1)).map(any(), any());
    }

    @Test
    void copyExistingSystemPreparationTechnique() {
        UserPrinciple principle = dsh.getPrinciple("basic");
        PreparationTechnique systemPreparationTechnique = dsh.getPreparationTechnique(5L);
        PreparationTechnique existingPreparationTechnique = PreparationTechnique.builder().build();

        lenient().when(preparationTechniqueRepository.findById(5L)).thenReturn(Optional.of(systemPreparationTechnique));
        lenient().when(preparationTechniqueRepository.findByCodeAndPrinciple(systemPreparationTechnique.getCode(), principle))
                .thenReturn(Optional.of(existingPreparationTechnique));

        when(dtoMapper.map(existingPreparationTechnique, PreparationTechniqueDTO.class))
                .thenReturn(new PreparationTechniqueDTO(null, null, null));

        PreparationTechniqueDTO preparationTechniqueDTO = service.copySystemPreparationTechnique(5L, principle);

        verify(preparationTechniqueRepository, times(1)).findById(5L);
        verify(preparationTechniqueRepository, times(1))
                .findByCodeAndPrinciple(systemPreparationTechnique.getCode(), principle);
        verify(preparationTechniqueRepository, times(0)).save(any(PreparationTechnique.class));
        verify(dtoMapper, times(1)).map(existingPreparationTechnique, PreparationTechniqueDTO.class);
    }

    @Test
    void copyAllSystemPreparationTechniques() {
        UserPrinciple principle = dsh.getPrinciple("basic");
        UserPrinciple systemPrinciple = dsh.getPrinciple("system");
        List<PreparationTechnique> systemPreparationTechniques = List.of(
                dsh.getPreparationTechnique(5L),
                dsh.getPreparationTechnique(6L));

        when(userPrincipleService.loadUserByUsername("system")).thenReturn(systemPrinciple);
        when(preparationTechniqueRepository.findAllByPrinciple(systemPrinciple)).thenReturn(systemPreparationTechniques);
        doReturn(new PreparationTechniqueDTO(null, null, null))
                .when(service).copySystemPreparationTechnique(5L, principle);
        doReturn(new PreparationTechniqueDTO(null, null, null))
                .when(service).copySystemPreparationTechnique(6L, principle);

        service.copySystemPreparationTechniques(principle);

        verify(userPrincipleService, times(1)).loadUserByUsername("system");
        verify(preparationTechniqueRepository, times(1)).findAllByPrinciple(systemPrinciple);
        verify(service, times(1)).copySystemPreparationTechnique(5L, principle);
        verify(service, times(1)).copySystemPreparationTechnique(6L, principle);
    }
}