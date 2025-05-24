package com.ageinghippy.model.nomads;

import lombok.Builder;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Builder
@Data
public class Task implements Serializable {

    @Serial
    private static final long serialVersionUID = -2218693873376528145L;

    public int id;
    public int userId;
    public String name;
    public String description;
    public Long createdAt;
    public Long updatedAt;
    public boolean completed;
}
