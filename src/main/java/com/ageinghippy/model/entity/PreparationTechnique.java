package com.ageinghippy.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn
    @JsonIgnore
    private UserPrinciple principle;

    @NotNull
    private String code;
    //    @NotNull
    private String description;

    @Override
    public String toString() {
        return "PreparationTechnique{" +
               "id=" + id +
               ", code='" + code + '\'' +
               ", description='" + description + '\'' +
               '}';
    }
}
