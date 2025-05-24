package com.ageinghippy.model.nomads;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaskResponse<T> implements Serializable {

    @Serial
    private static final long serialVersionUID = 3904503725783474580L;

    public T data;
    public TaskError error;
    public String status;
}
