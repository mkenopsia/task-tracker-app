package ru.mkenopsia.tasktrackerbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.mkenopsia.tasktrackerbackend.entity.Task;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Integer> {
    Optional<List<Task>> getTasksByDate(ZonedDateTime date);

    @Query(nativeQuery = true, value = "SELECT * FROM task_management.t_task" +
            " WHERE c_date BETWEEN :from AND :to AND c_author_id = :userId")
    Optional<List<Task>> getUserTasksWithinPeriod(@Param("userId") Integer userId, @Param("from") ZonedDateTime from, @Param("to") ZonedDateTime to);
}
