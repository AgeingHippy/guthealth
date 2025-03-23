package com.ageinghippy.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserMeta {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private String email;

    private String bio;
}
