package com.ageinghippy.model.nomads;

import lombok.Builder;

import java.io.Serial;
import java.io.Serializable;

@Builder
public class TaskError implements Serializable {

    @Serial
    private static final long serialVersionUID = -7807875577405827987L;

    public String message;
}
