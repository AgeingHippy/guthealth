package com.ageinghippy.model;

import com.ageinghippy.model.dto.PreparationTechniqueDTO;
import com.ageinghippy.model.entity.PreparationTechnique;
import org.modelmapper.ModelMapper;
import org.modelmapper.record.RecordModule;

import java.util.List;

public class MyMapper extends ModelMapper {

    public MyMapper() {
        super();
        this.registerModule(new RecordModule());
    }

    @Override
    public <D> D map(Object source, Class<D> destinationType) {

        if (PreparationTechniqueDTO.class == destinationType) {
            return (D) toDto((PreparationTechnique) source);
        } else {
            return super.map(source, destinationType);
        }
    }

    public <D> List<D> mapList(List<?> sources, Class<D> destinationType) {
        return sources.stream()
                .map(item -> this.map(item, destinationType))
                .toList();
    }

    private PreparationTechniqueDTO toDto(PreparationTechnique preparationTechnique) {
        return new PreparationTechniqueDTO(preparationTechnique.getCode(), preparationTechnique.getDescription());
    }

}
