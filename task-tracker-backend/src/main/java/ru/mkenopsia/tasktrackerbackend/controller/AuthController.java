package ru.mkenopsia.tasktrackerbackend.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.mkenopsia.tasktrackerbackend.dto.UserSignUpRequest;
import ru.mkenopsia.tasktrackerbackend.entity.User;
import ru.mkenopsia.tasktrackerbackend.mapper.UserMapper;
import ru.mkenopsia.tasktrackerbackend.service.AuthService;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UserMapper userMapper;

    @PostMapping("/sign-up")
    public ResponseEntity<?> signUp(@Valid @RequestBody UserSignUpRequest userSignUpRequest,
                                    HttpServletResponse response) {
        User createdUser = this.authService.signUpUser(userMapper.toEntity(userSignUpRequest), response);

        return ResponseEntity.ok(userMapper.toSignUpResponse(createdUser));
    }

}
