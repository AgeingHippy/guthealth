package com.ageinghippy.service;

import com.ageinghippy.model.entity.UserPrinciple;
import com.ageinghippy.model.nomads.Task;
import com.ageinghippy.model.nomads.TaskResponse;
import com.ageinghippy.model.nomads.TaskUser;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class TaskListApiGateway {
    private final RestTemplate restTemplate;

    //todo - sort out fetch of properties during unit tests
//    @Value("codingnomads-tasklist-api.user.uri")
    private String usersUri = "http://demo.codingnomads.co:8080/tasks_api/users";
    private String tasksUri = "http://demo.codingnomads.co:8080/tasks_api/tasks";

    public int getUserId(String email) {
        ResponseEntity<TaskResponse<List<TaskUser>>> response;
        ParameterizedTypeReference<TaskResponse<List<TaskUser>>> typeRef =
                new ParameterizedTypeReference<>() {
                };

        try {
            response = restTemplate.exchange(usersUri + "?email=" + email,
                    HttpMethod.GET,
                    null,
                    typeRef
            );
            return Objects.requireNonNull(response.getBody()).data.getFirst().id;
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.BAD_REQUEST) {
                //we assume all 400 responses are because the user does not exist
                return 0;
            } else {
                throw new RuntimeException(e);
            }
        }
    }

    public int createUser(String email) {
        ResponseEntity<TaskResponse<TaskUser>> response;
        ParameterizedTypeReference<TaskResponse<TaskUser>> typeRef =
                new ParameterizedTypeReference<>() {
                };

        try {
            response = restTemplate.exchange(
                    usersUri,
                    HttpMethod.POST,
                    new HttpEntity<>(
                            TaskUser.builder()
                                    .email(email)
                                    .build()),
                    typeRef
            );
            return Objects.requireNonNull(response.getBody()).data.id;
        } catch (HttpClientErrorException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Task> getTasks(int taskUserId) {
        ResponseEntity<TaskResponse<List<Task>>> response;
        ParameterizedTypeReference<TaskResponse<List<Task>>> typeRef =
                new ParameterizedTypeReference<>() {
                };

        try {
            response = restTemplate.exchange(
                    tasksUri + "?complete=-1&userId=" + taskUserId,
                    HttpMethod.GET,
                    null,
                    typeRef
            );
            return Objects.requireNonNull(response.getBody()).data;
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.BAD_REQUEST) {
                //we assume all 400 responses are because the data does not exist
                return List.of();
            } else {
                throw new RuntimeException(e);
            }
        }
    }

    public List<Task> getTasks(UserPrinciple principle) {
        String email = principle.getUserMeta().getEmail();
        if (email != null || !email.isEmpty()) {
            return getTasks(getUserId(email));
        } else {
            return List.of();
        }

    }

    public Task createTask(Task newTask) {
        ResponseEntity<TaskResponse<Task>> response;
        ParameterizedTypeReference<TaskResponse<Task>> typeRef =
                new ParameterizedTypeReference<>() {
                };

        try {
            response = restTemplate.exchange(
                    tasksUri,
                    HttpMethod.POST,
                    new HttpEntity<>(newTask),
                    typeRef
            );
            return Objects.requireNonNull(response.getBody()).data;
        } catch (HttpClientErrorException e) {
            throw new RuntimeException(e);
        }
    }

    public Task createTask(UserPrinciple principle, Task newTask) {
        String email = principle.getUserMeta().getEmail();
        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException("User email address required to create new tasks");
        }
        newTask.userId = getUserId(email);
        if (newTask.userId == 0) {
            newTask.userId = createUser(email);
        }

        return createTask(newTask);
    }

    public Task updateTask(UserPrinciple principle, Task updatedTask) {
        updatedTask.userId = getUserId(principle.getUserMeta().getEmail());

        ResponseEntity<TaskResponse<Task>> response;
        ParameterizedTypeReference<TaskResponse<Task>> typeRef =
                new ParameterizedTypeReference<>() {
                };

        try {
            response = restTemplate.exchange(
                    tasksUri + "/" + updatedTask.id,
                    HttpMethod.PUT,
                    new HttpEntity<>(updatedTask),
                    typeRef
            );
            return Objects.requireNonNull(response.getBody()).data;
        } catch (HttpClientErrorException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteTask(UserPrinciple principle, int taskId) {
        List<Task> tasks = getTasks(principle);
        if (tasks.stream().noneMatch(task -> task.id == taskId)) {
            throw new IllegalArgumentException("Specified task does not belong to the specified user");
        }

        ResponseEntity<Void> response = restTemplate.exchange(
                tasksUri +"/" + taskId,
                HttpMethod.DELETE,
                null,
                Void.class
        );
    }
}
