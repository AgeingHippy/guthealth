package com.ageinghippy.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PreparationTechnique {
    @Id
    private String code;
    @NotNull
    private String description;

    @Override
    public String toString() {
        return "PreparationTechnique{" +
                "code='" + code + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
