package ru.mkenopsia.tasktrackerbackend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.mkenopsia.tasktrackerbackend.dto.CreateTaskRequest;
import ru.mkenopsia.tasktrackerbackend.dto.TaskDto;
import ru.mkenopsia.tasktrackerbackend.entity.Task;
import ru.mkenopsia.tasktrackerbackend.mapper.TaskMapper;
import ru.mkenopsia.tasktrackerbackend.repository.TaskRepository;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    @Transactional
    public Task createTask(Task task) {
        return this.taskRepository.save(task);
    }

    @Transactional(readOnly = true)
    public List<TaskDto> getUserTasksWithinPeriod(Integer userId, ZonedDateTime from, ZonedDateTime to) {
        var tasks = this.taskRepository.getUserTasksWithinPeriod(userId, from, to)
                .orElseThrow(() -> new NoSuchElementException("datasource.error.tasks.not_found"));

        return tasks.stream().map(taskMapper::toTaskDto).toList();
    }

    @Transactional
    public void deleteTaskById(Integer taskId) {
        this.taskRepository.deleteById(taskId);
    }

    @Transactional
    public Task updateTask(Task task) {
        return this.taskRepository.save(task);
    }
}
