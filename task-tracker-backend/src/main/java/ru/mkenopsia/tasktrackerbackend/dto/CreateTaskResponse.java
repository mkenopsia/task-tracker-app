package ru.mkenopsia.tasktrackerbackend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.ZonedDateTime;

public record CreateTaskResponse(
        Integer id,
        String name,
        String description,
        ZonedDateTime date
) {
}
