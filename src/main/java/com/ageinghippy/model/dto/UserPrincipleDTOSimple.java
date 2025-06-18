package com.ageinghippy.model.dto;

import java.io.Serializable;

public record UserPrincipleDTOSimple(Long id, String userName, String name) implements Serializable {
}
