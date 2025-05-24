package com.ageinghippy.controller.rest;

import com.ageinghippy.model.entity.UserPrinciple;
import com.ageinghippy.model.nomads.Task;
import com.ageinghippy.service.TaskListApiGateway;
import com.ageinghippy.service.UserPrincipleService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskListApiGateway taskListApiGateway;
    private final UserPrincipleService userPrincipleService;

    @GetMapping("")
    public List<Task> getUserTasks(Authentication authentication) {
        UserPrinciple principle = userPrincipleService.castToUserPrinciple(authentication.getPrincipal());

        return taskListApiGateway.getTasks(principle);
    }

    @PostMapping("")
    public Task createUserTask(@RequestBody Task newTask, Authentication authentication) {
        UserPrinciple principle = userPrincipleService.castToUserPrinciple(authentication.getPrincipal());

        return  taskListApiGateway.createTask(principle, newTask);
    }

    @PutMapping("")
    public Task updateUserTask(@RequestBody Task updatedTask, Authentication authentication) {
        UserPrinciple principle = userPrincipleService.castToUserPrinciple(authentication.getPrincipal());

        return  taskListApiGateway.updateTask(principle, updatedTask);
    }

    @DeleteMapping("/{id}")
    public void deleteUserTask(@PathVariable int id, Authentication authentication) {
        UserPrinciple principle = userPrincipleService.castToUserPrinciple(authentication.getPrincipal());

        taskListApiGateway.deleteTask(principle,id);
    }

}
