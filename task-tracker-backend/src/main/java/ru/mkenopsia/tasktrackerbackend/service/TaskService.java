package ru.mkenopsia.tasktrackerbackend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.mkenopsia.tasktrackerbackend.entity.Task;
import ru.mkenopsia.tasktrackerbackend.repository.TaskRepository;

import java.time.ZonedDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;

    public void createTask(Task task) {
        this.taskRepository.save(task);
    }

    public List<Task> getTasksByDate(ZonedDateTime dateTime) {
        return this.taskRepository.getTasksByDate(dateTime);
    }
}
