package ru.mkenopsia.tasktrackerbackend.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import ru.mkenopsia.tasktrackerbackend.dto.UserInfoDto;
import ru.mkenopsia.tasktrackerbackend.dto.UserLoginResponse;
import ru.mkenopsia.tasktrackerbackend.dto.UserSignUpRequest;
import ru.mkenopsia.tasktrackerbackend.dto.UserSignUpResponse;
import ru.mkenopsia.tasktrackerbackend.entity.User;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface UserMapper {

    @Mapping(target = "tasks", ignore = true)
    User toEntity(UserSignUpRequest request);

    @Mapping(target = "tasks", ignore = true)
    User toEntity(UserInfoDto userInfoDto);

    UserSignUpResponse toSignUpResponse(User user);

    UserInfoDto toUserInfoDto(User user);

    UserLoginResponse toUserLoginResponse(User user);
}
