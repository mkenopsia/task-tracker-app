package ru.mkenopsia.tasktrackerbackend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.mkenopsia.tasktrackerbackend.dto.*;
import ru.mkenopsia.tasktrackerbackend.entity.Task;
import ru.mkenopsia.tasktrackerbackend.mapper.TaskMapper;
import ru.mkenopsia.tasktrackerbackend.service.TaskService;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;
    private final TaskMapper taskMapper;

    @GetMapping
    public ResponseEntity<List<TaskDto>> getTasksWithinPeriod(Authentication authentication, @RequestParam("from") ZonedDateTime from, @RequestParam("to") ZonedDateTime to) {
        if (from.isAfter(to)) {
            throw new IllegalArgumentException("validation.error.invalid_period");
        }

        List<TaskDto> tasks = new ArrayList<>();
        if (authentication.getPrincipal() instanceof CustomUserDetails userDetails) {
            tasks = this.taskService.getUserTasksWithinPeriod(userDetails.getId(), from, to);
        }

        return ResponseEntity.ok(tasks);
    }

    @PostMapping
    public ResponseEntity<CreateTaskResponse> createTask(Authentication authentication, @Valid @RequestBody CreateTaskRequest request, BindingResult bindingResult) throws BindException {
        if (bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        }

        Integer userId = null;
        if (authentication.getPrincipal() instanceof CustomUserDetails userDetails) {
            userId = userDetails.getId();
        }

        CreateTaskResponse createdTask = this.taskMapper.toCreateTaskResponse(
                this.taskService.createTask(this.taskMapper.toEntity(userId, request))
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(createdTask);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteTask(@RequestParam("taskId") Integer taskId) {
        this.taskService.deleteTaskById(taskId);

        return ResponseEntity.ok().build();
    }

    @PatchMapping
    public ResponseEntity<UpdateTaskResponse> updateTask(Authentication authentication, @Valid @RequestBody UpdateTaskRequest request, BindingResult bindingResult) throws BindException {
        if (bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        }

        Integer userId = null;
        if (authentication.getPrincipal() instanceof CustomUserDetails userDetails) {
            userId = userDetails.getId();
        }

        UpdateTaskResponse updatedTask = this.taskMapper.toUpdateTaskResponse(
                this.taskService.updateTask(this.taskMapper.toEntity(userId, request))
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(updatedTask);
    }
}
