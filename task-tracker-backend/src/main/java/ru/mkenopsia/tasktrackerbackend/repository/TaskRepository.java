package ru.mkenopsia.tasktrackerbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.mkenopsia.tasktrackerbackend.entity.Task;

public interface TaskRepository extends JpaRepository<Task, Integer> {
}
