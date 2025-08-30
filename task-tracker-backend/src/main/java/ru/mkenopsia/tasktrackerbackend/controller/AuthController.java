package ru.mkenopsia.tasktrackerbackend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.mkenopsia.tasktrackerbackend.dto.UserRegisterRequest;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {



    public ResponseEntity<?> register(@Valid @RequestBody UserRegisterRequest userRegisterRequest) {


        return ResponseEntity.ok().build();
    }

}
