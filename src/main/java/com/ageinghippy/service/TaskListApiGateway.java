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

    /**
     * Fetch the task user id if the user for the given email exists in the tasks database
     *
     * @param email a String containing the email representing the user
     * @return int containing the userId in the tasks database. 0 if user does not exist
     */
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
                throw e;
            }
        }
    }

    /**
     * Create a user with the given email address in the tasks database
     *
     * @param email a String value containing the user email address
     * @return an int containing the user id within the task database
     */
    public int createUser(String email) {
        ResponseEntity<TaskResponse<TaskUser>> response;
        ParameterizedTypeReference<TaskResponse<TaskUser>> typeRef =
                new ParameterizedTypeReference<>() {
                };

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
    }

    /**
     * Get a list of {@link Task} records for the given user
     *
     * @param taskUserId and int value representing the user id
     * @return list of {@link Task} records for the given user.
     * Note: An empty list is returned if the user does not exist.
     */
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

    /**
     * Get the tasks for the given principle
     *
     * @param principle an {@link UserPrinciple} for whom we wish to get the tasks
     * @return a list of associated {@link Task} records
     * <p>
     * Note that if the user email is not present, we return an empty list
     */
    public List<Task> getTasks(UserPrinciple principle) {
        String email = principle.getUserMeta().getEmail();
        if (email != null && !email.isEmpty()) {
            return getTasks(getUserId(email));
        } else {
            return List.of();
        }
    }

    /**
     * Insert a new task into the tasks database
     *
     * @param newTask the {@link Task} we wish to create
     * @return the Task as created in the database
     */
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

    /**
     * Save a task in the nomads database for the given user
     * If the user does not already exist in the database, we will also insert the user.
     *
     * @param principle the {@link UserPrinciple} representing the task owner
     * @param newTask   the {@link Task} we wish to insert
     * @return the Task as created in the database
     */
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

    /**
     * Update the given task in the codingnomads task database
     *
     * @param principle   the {@link UserPrinciple} who owns the task
     * @param updatedTask the {@link Task} we wish to update
     * @return the Task as updated in the database
     * @throws IllegalArgumentException if the task does not belong to the specified user
     */
    public Task updateTask(UserPrinciple principle, Task updatedTask) {
        int userId = getUserId(principle.getUserMeta().getEmail());
        List<Task> tasks = getTasks(userId);
        if (tasks.stream().noneMatch(task -> task.id == updatedTask.id)) {
            throw new IllegalArgumentException("Specified task does not belong to the specified user");
        }
        updatedTask.userId = userId;

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
            throw e;
        }
    }

    /**
     * Delete the given task if it belongs to the given user
     *
     * @param principle a {@link UserPrinciple} owning the task to be deleted
     * @param taskId    the int id representing the {@link Task} to be deleted
     * @throws IllegalArgumentException if the identified task does not exist in the list of tasks owned by this user
     */
    public void deleteTask(UserPrinciple principle, int taskId) {
        List<Task> tasks = getTasks(principle);
        if (tasks.stream().noneMatch(task -> task.id == taskId)) {
            throw new IllegalArgumentException("Specified task does not belong to the specified user");
        }

        ResponseEntity<TaskResponse<String>> response;
        ParameterizedTypeReference<TaskResponse<String>> typeRef =
                new ParameterizedTypeReference<>() {
                };

        response = restTemplate.exchange(
                tasksUri + "/" + taskId,
                HttpMethod.DELETE,
                null,
                typeRef
        );
    }
}
