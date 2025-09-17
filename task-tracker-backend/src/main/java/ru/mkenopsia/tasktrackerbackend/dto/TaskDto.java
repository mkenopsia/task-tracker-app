package ru.mkenopsia.tasktrackerbackend.dto;

import java.time.LocalDate;
import java.time.ZonedDateTime;

public record TaskDto(
        Integer id,
        String name,
        String description,
        Boolean isDone,
        ZonedDateTime date) {
}
