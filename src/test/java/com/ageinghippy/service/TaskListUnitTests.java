package com.ageinghippy.service;


import com.ageinghippy.model.external.TaskError;
import com.ageinghippy.model.external.TaskResponse;
import com.ageinghippy.model.external.TaskUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TaskListUnitTests {
    private TaskListApiGateway taskListApiGateway;

    @Mock
    RestTemplate restTemplate;

    @BeforeEach
    void setUp() {
        taskListApiGateway = new TaskListApiGateway(restTemplate);
    }

    @Test
    void verifyUserDoesExist() {
        String userEmail = "bob@home.com";

        ResponseEntity<TaskResponse<Object>> responseEntity = new ResponseEntity<>(
                TaskResponse.builder()
                        .data(List.of(TaskUser.builder()
                                .id(1234)
                                .firstName("bob")
                                .lastName("builder")
                                .email("bob@home.com")
                                .build()))
                        .error(TaskError.builder().message("none").build())
                        .status("200 ok")
                        .build(),
                HttpStatus.OK);

        when(restTemplate.exchange("http://demo.codingnomads.co:8080/tasks_api/users?email=" + userEmail,
                HttpMethod.GET,
                null,
                TaskResponse.class)
        ).thenReturn(responseEntity);

        assertTrue(taskListApiGateway.userExists(userEmail));
    }

    @Test
    void testUserDoesNotExist() {
        assertFalse(taskListApiGateway.userExists("bob@home.com"));
    }

}
