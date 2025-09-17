package ru.mkenopsia.tasktrackerbackend.dto;

import java.time.ZonedDateTime;

public record UpdateTaskResponse(
        Integer id,
        String name,
        String description,
        Boolean isDone,
        ZonedDateTime date
) {
}
