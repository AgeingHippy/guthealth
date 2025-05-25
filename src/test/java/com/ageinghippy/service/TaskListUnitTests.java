package com.ageinghippy.service;


import com.ageinghippy.model.entity.UserMeta;
import com.ageinghippy.model.entity.UserPrinciple;
import com.ageinghippy.model.nomads.Task;
import com.ageinghippy.model.nomads.TaskError;
import com.ageinghippy.model.nomads.TaskResponse;
import com.ageinghippy.model.nomads.TaskUser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TaskListUnitTests {
    private TaskListApiGateway taskListApiGateway;

    private String tasksUri = "http://demo.codingnomads.co:8080/tasks_api/tasks";
    private String usersUri = "http://demo.codingnomads.co:8080/tasks_api/users";

    @Mock
    RestTemplate restTemplate;

    @BeforeEach
    void setUp() {
        TaskListApiGateway gateway = new TaskListApiGateway(restTemplate);
        taskListApiGateway = spy(gateway);
    }

    @Test
    void verifyUserDoesExist() {
        String userEmail = "bob@home.com";

        TaskResponse<List<TaskUser>> taskResponse = new TaskResponse<>();

        taskResponse.data = List.of(
                TaskUser.builder()
                        .id(1234)
                        .firstName("bob")
                        .lastName("builder")
                        .email("bob@home.com")
                        .build());
        taskResponse.error = TaskError.builder().message("none").build();
        taskResponse.status = "200 ok";

        ResponseEntity<TaskResponse<List<TaskUser>>> responseEntity =
                new ResponseEntity<>(taskResponse, HttpStatus.OK);

        ParameterizedTypeReference<TaskResponse<List<TaskUser>>> typeRef =
                new ParameterizedTypeReference<>() {
                };

        when(restTemplate.exchange(
                usersUri + "?email=" + userEmail,
                HttpMethod.GET,
                null,
                typeRef)
        ).thenReturn(responseEntity);

        assertEquals(1234, taskListApiGateway.getUserId(userEmail));
    }

    @Test
    void testUserDoesNotExist() throws JsonProcessingException {
        String userEmail = "bob@home.com";

        TaskResponse<List<TaskUser>> taskResponse = new TaskResponse<>();

        taskResponse.data = null;

        taskResponse.error = TaskError.builder().message("User does not exist with email: bob@home.com").build();
        taskResponse.status = "400 BAD_REQUEST";

        ResponseEntity<TaskResponse<List<TaskUser>>> responseEntity =
                new ResponseEntity<>(taskResponse, HttpStatus.BAD_REQUEST);

        ParameterizedTypeReference<TaskResponse<List<TaskUser>>> typeRef =
                new ParameterizedTypeReference<>() {
                };

        HttpClientErrorException exception = HttpClientErrorException.create(
                HttpStatus.BAD_REQUEST,
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                null,
                (new ObjectMapper()).writeValueAsBytes(taskResponse),
                null // encoding, e.g., "UTF-8"
        );

        when(restTemplate.exchange("http://demo.codingnomads.co:8080/tasks_api/users?email=" + userEmail,
                HttpMethod.GET,
                null,
                typeRef)
        ).thenThrow(exception);

        assertEquals(0, taskListApiGateway.getUserId(userEmail));
    }

    @Test
    void createNewTaskUser() {
        String userEmail = "bob@home.com";
        TaskUser newTaskUser = TaskUser.builder()
                .email(userEmail)
                .build();

        TaskUser expectedUser = TaskUser.builder()
                .id(1234)
                .email(userEmail)
                .build();

        TaskResponse<TaskUser> taskResponse = new TaskResponse<>();
        taskResponse.data = expectedUser;
        taskResponse.error = TaskError.builder().message("none").build();
        taskResponse.status = "201 CREATED";

        ResponseEntity<TaskResponse<TaskUser>> responseEntity =
                new ResponseEntity<>(taskResponse, HttpStatus.CREATED);

        ParameterizedTypeReference<TaskResponse<TaskUser>> typeRef =
                new ParameterizedTypeReference<>() {
                };


        when(restTemplate.exchange("http://demo.codingnomads.co:8080/tasks_api/users",
                HttpMethod.POST,
                new HttpEntity<>(newTaskUser),
                typeRef)
        ).thenReturn(responseEntity);

        assertEquals(1234, taskListApiGateway.createUser(userEmail));
    }

    @Test
    void getTasksForUser() {
        int userId = 1234;
        List<Task> tasks = List.of(
                Task.builder().id(101).userId(1234).name("task 1").description("desc 1").build(),
                Task.builder().id(102).userId(1234).name("task 2").description("desc 2").build()
        );

        TaskResponse<List<Task>> taskResponse = new TaskResponse<>();
        taskResponse.data = tasks;
        taskResponse.error = TaskError.builder().build();
        taskResponse.status = "200 OK";

        ResponseEntity<TaskResponse<List<Task>>> responseEntity =
                new ResponseEntity<>(taskResponse, HttpStatus.OK);

        ParameterizedTypeReference<TaskResponse<List<Task>>> typeRef =
                new ParameterizedTypeReference<>() {
                };

        when(restTemplate.exchange(
                "http://demo.codingnomads.co:8080/tasks_api/tasks?complete=-1&userId=" + userId,
                HttpMethod.GET,
                null,
                typeRef))
                .thenReturn(responseEntity);

        assertEquals(tasks, taskListApiGateway.getTasks(1234));
    }

    @Test
    void createTaskForUser() {
        int userId = 1234;
        Task newTask = Task.builder()
                .userId(userId)
                .name("task 1")
                .description("desc 1")
                .build();

        Task expectedTask = Task.builder()
                .id(101)
                .userId(userId)
                .name("task 1")
                .description("desc 1")
                .build();

        TaskResponse<Task> taskResponse = new TaskResponse<>();
        taskResponse.data = expectedTask;
        taskResponse.error = TaskError.builder().message("none").build();
        taskResponse.status = "201 CREATED";

        ResponseEntity<TaskResponse<Task>> responseEntity =
                new ResponseEntity<>(taskResponse, HttpStatus.CREATED);

        ParameterizedTypeReference<TaskResponse<Task>> typeRef =
                new ParameterizedTypeReference<>() {
                };

        when(restTemplate.exchange(
                tasksUri,
                HttpMethod.POST,
                new HttpEntity<>(newTask),
                typeRef)
        ).thenReturn(responseEntity);

        assertEquals(expectedTask, taskListApiGateway.createTask(newTask));
    }

    @Test
    void createTask_user_exists() {
        UserPrinciple principle = UserPrinciple.builder()
                .userMeta(UserMeta.builder().email("bob@home.com").build())
                .build();

        Task task = Task.builder().build();

        doReturn(123).when(taskListApiGateway).getUserId("bob@home.com");
        doAnswer(invocation -> {
            return invocation.getArgument(0);
        }).when(taskListApiGateway).createTask(any(Task.class));

        assertEquals(task, taskListApiGateway.createTask(principle, task));

        verify(taskListApiGateway, times(1)).getUserId("bob@home.com");
        verify(taskListApiGateway, times(1)).createTask(any(Task.class));
    }

    @Test
    void createTask_user_not_exists() {
        UserPrinciple principle = UserPrinciple.builder()
                .userMeta(UserMeta.builder().email("bob@home.com").build())
                .build();

        Task task = Task.builder().build();

        doReturn(0).when(taskListApiGateway).getUserId("bob@home.com");
        doReturn(123).when(taskListApiGateway).createUser("bob@home.com");
        doAnswer(invocation -> {
            return invocation.getArgument(0);
        }).when(taskListApiGateway).createTask(any(Task.class));

        assertEquals(task, taskListApiGateway.createTask(principle, task));

        verify(taskListApiGateway, times(1)).getUserId("bob@home.com");
        verify(taskListApiGateway, times(1)).createUser("bob@home.com");
        verify(taskListApiGateway, times(1)).createTask(any(Task.class));
    }

    @Test
    void createTask_no_email() {
        UserPrinciple principle = UserPrinciple.builder()
                .userMeta(UserMeta.builder().build())
                .build();
        Task task = Task.builder().build();

        assertThrows(IllegalArgumentException.class,
                () -> taskListApiGateway.createTask(principle, task));
    }

    @Test
    void updateTask_task_exists() {
        UserPrinciple principle = UserPrinciple.builder()
                .userMeta(UserMeta.builder().email("bob@home.com").build())
                .build();

        List<Task> tasks = List.of(
                Task.builder().id(123).build(),
                Task.builder().id(456).build(),
                Task.builder().id(789).build()
        );

        Task updatedTask = Task.builder().id(123).build();

        TaskResponse<Task> taskResponse = new TaskResponse<>();
        taskResponse.data = updatedTask;
        taskResponse.error = TaskError.builder().message("none").build();
        taskResponse.status = "200 OK";

        ResponseEntity<TaskResponse<Task>> responseEntity =
                new ResponseEntity<>(taskResponse, HttpStatus.CREATED);

        ParameterizedTypeReference<TaskResponse<Task>> typeRef =
                new ParameterizedTypeReference<>() {
                };

        doReturn(567).when(taskListApiGateway).getUserId("bob@home.com");
        doReturn(tasks).when(taskListApiGateway).getTasks(567);

        when(restTemplate.exchange(
                tasksUri + "/123",
                HttpMethod.PUT,
                new HttpEntity<>(updatedTask),
                typeRef)
        ).thenReturn(responseEntity);

        Task responseTask = taskListApiGateway.updateTask(principle, updatedTask);
        assertEquals(567, responseTask.userId);
    }

    @Test
    void updateTask_task_not_exists() throws JsonProcessingException {
        UserPrinciple principle = UserPrinciple.builder()
                .userMeta(UserMeta.builder().email("bob@home.com").build())
                .build();

        List<Task> tasks = List.of(
                Task.builder().id(123).build(),
                Task.builder().id(456).build(),
                Task.builder().id(789).build()
        );

        Task updatedTask = Task.builder().id(111).build();

        doReturn(567).when(taskListApiGateway).getUserId("bob@home.com");
        doReturn(tasks).when(taskListApiGateway).getTasks(567);

        assertThrows(IllegalArgumentException.class,
                () -> taskListApiGateway.updateTask(principle, updatedTask));
    }

    @Test
    void testDeleteExists() {
        UserPrinciple principle = UserPrinciple.builder().build();

        List<Task> tasks = List.of(
                Task.builder().id(123).build(),
                Task.builder().id(456).build(),
                Task.builder().id(789).build()
        );

        doReturn(tasks).when(taskListApiGateway).getTasks(principle);

        TaskResponse<String> taskResponse = new TaskResponse<>();
        taskResponse.data = "Task Successfully Deleted";
        taskResponse.error = TaskError.builder().message("none").build();
        taskResponse.status = "200 OK";

        ResponseEntity<TaskResponse<String>> responseEntity =
                new ResponseEntity<>(taskResponse, HttpStatus.CREATED);

        ParameterizedTypeReference<TaskResponse<String>> typeRef =
                new ParameterizedTypeReference<>() {
                };

        when(restTemplate.exchange(
                "http://demo.codingnomads.co:8080/tasks_api/tasks" + "/456",
                HttpMethod.DELETE,
                null,
                typeRef))
                .thenReturn(responseEntity);

        taskListApiGateway.deleteTask(principle, 456);

        verify(restTemplate, timeout(1)).exchange(
                "http://demo.codingnomads.co:8080/tasks_api/tasks" + "/456",
                HttpMethod.DELETE,
                null,
                typeRef);
    }

    @Test
    void testDeleteNotExists() {
        UserPrinciple principle = UserPrinciple.builder().build();

        List<Task> tasks = List.of(
                Task.builder().id(123).build(),
                Task.builder().id(456).build(),
                Task.builder().id(789).build()
        );

        doReturn(tasks).when(taskListApiGateway).getTasks(principle);

        assertThrows(IllegalArgumentException.class,
                () -> taskListApiGateway.deleteTask(principle, 111));

    }

    @Test
    void getTasks_no_email() {
        UserPrinciple principle = UserPrinciple.builder()
                .userMeta(UserMeta.builder().build())
                .build();

        assertEquals(List.of(), taskListApiGateway.getTasks(principle));
    }

    @Test
    void getTasks_has_email() {
        UserPrinciple principle = UserPrinciple.builder()
                .userMeta(UserMeta.builder().email("bob@home.com").build())
                .build();

        List<Task> tasks = List.of(
                Task.builder().id(123).build(),
                Task.builder().id(456).build(),
                Task.builder().id(789).build()
        );

        doReturn(123).when(taskListApiGateway).getUserId("bob@home.com");
        doReturn(tasks).when(taskListApiGateway).getTasks(123);

        assertEquals(tasks, taskListApiGateway.getTasks(principle));

        verify(taskListApiGateway, times(1)).getUserId("bob@home.com");
        verify(taskListApiGateway, times(1)).getTasks(123);
    }

}
