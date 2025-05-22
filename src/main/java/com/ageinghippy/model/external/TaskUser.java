package com.ageinghippy.model.external;

import lombok.Builder;

@Builder
public class TaskUser {
    public int id;
    public String email;
    public String firstName;
    public String lastName;
    public Long createdAt;
    public Long updatedAt;
}
