package ru.mkenopsia.tasktrackerbackend.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import ru.mkenopsia.tasktrackerbackend.dto.*;
import ru.mkenopsia.tasktrackerbackend.entity.Task;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface TaskMapper {

    @Mapping(target = "date", source = "date", expression = "java(task.getDate().toLocalDate())")
    TaskDto toTaskDto(Task task);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "authorId", source = "userId")
    @Mapping(target = "isDone", defaultValue = "false")
    @Mapping(target = "name", source = "request.name")
    @Mapping(target = "description", source = "request.description")
    @Mapping(target = "date", source = "request.date")
    Task toEntity(Integer userId, CreateTaskRequest request);

    @Mapping(target = "id", source = "request.id")
    @Mapping(target = "authorId", source = "userId")
    @Mapping(target = "isDone", source = "request.isDone")
    @Mapping(target = "name", source = "request.name")
    @Mapping(target = "description", source = "request.description")
    @Mapping(target = "date", source = "request.date")
    Task toEntity(Integer userId, UpdateTaskRequest request);

    CreateTaskResponse toCreateTaskResponse(Task task);

    UpdateTaskResponse toUpdateTaskResponse(Task task);
}
