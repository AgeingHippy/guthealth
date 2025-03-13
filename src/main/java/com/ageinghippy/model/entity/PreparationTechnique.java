package com.ageinghippy.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Entity
@Table(name = "preparation_technique")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PreparationTechnique implements Serializable {

    @Serial
    private static final long serialVersionUID = 8426557152559923887L;

    @Id
    private String code;
//    @NotNull
    private String description;

    @Override
    public String toString() {
        return "PreparationTechnique{" +
                "code='" + code + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
