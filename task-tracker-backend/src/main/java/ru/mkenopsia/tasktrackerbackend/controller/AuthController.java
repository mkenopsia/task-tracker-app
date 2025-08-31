package ru.mkenopsia.tasktrackerbackend.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.mkenopsia.tasktrackerbackend.dto.UserLoginRequest;
import ru.mkenopsia.tasktrackerbackend.dto.UserLoginResponse;
import ru.mkenopsia.tasktrackerbackend.dto.UserSignUpRequest;
import ru.mkenopsia.tasktrackerbackend.dto.UserSignUpResponse;
import ru.mkenopsia.tasktrackerbackend.mapper.UserMapper;
import ru.mkenopsia.tasktrackerbackend.service.AuthService;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/sign-up")
    public ResponseEntity<?> signUp(@Valid @RequestBody UserSignUpRequest userSignUpRequest,
                                    HttpServletResponse response,
                                    BindingResult bindingResult) throws BindException {
        if(bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        }

        UserSignUpResponse createdUser = this.authService.signUpUser(userSignUpRequest, response);

        return ResponseEntity.ok(createdUser);
    }

    @PostMapping("/sign-in")
    public ResponseEntity<?> signIn(@Valid @RequestBody UserLoginRequest userLoginRequest,
                                    HttpServletResponse response,
                                    BindingResult bindingResult) throws BindException {
        if(bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        }

        this.authService.signInUser(userLoginRequest, response);

        return ResponseEntity.ok(new UserLoginResponse(userLoginRequest.identifier()));
    }

    @PostMapping("/sign-out")
    public ResponseEntity<?> signOut(HttpServletRequest request, HttpServletResponse response) {

        this.authService.signOutUser(request, response);

        return ResponseEntity.noContent().build();
    }
}
