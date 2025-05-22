package com.ageinghippy.model.external;

import lombok.Builder;

import java.util.List;

@Builder
public class TaskResponse<T> {
    public List<T> data;
    public TaskError error;
    public String status;
}
