package com.ageinghippy.model.nomads;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Builder
@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class TaskUser implements Serializable {

    @Serial
    private static final long serialVersionUID = -3229674963247020388L;

    public int id;
    public String email;
    @Builder.Default
    public String firstName = "placeholder";
    @Builder.Default
    public String lastName = "placeholder";
    public Long createdAt;
    public Long updatedAt;
}
