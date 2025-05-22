package com.ageinghippy.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class TaskListApiGateway {
    private final RestTemplate restTemplate;

    @Value("codingnomads-tasklist-api.user.uri")
    private String UserUri;

    public boolean userExists(String email) {

        return false;
    }
}
