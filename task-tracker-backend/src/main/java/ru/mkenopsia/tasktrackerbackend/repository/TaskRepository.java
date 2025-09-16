package ru.mkenopsia.tasktrackerbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.mkenopsia.tasktrackerbackend.entity.Task;

import java.time.ZonedDateTime;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Integer> {
    List<Task> getTasksByDate(ZonedDateTime date);
}
