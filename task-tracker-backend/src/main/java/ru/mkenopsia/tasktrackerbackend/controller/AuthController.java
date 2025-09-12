package ru.mkenopsia.tasktrackerbackend.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.mkenopsia.tasktrackerbackend.dto.*;
import ru.mkenopsia.tasktrackerbackend.mapper.UserMapper;
import ru.mkenopsia.tasktrackerbackend.service.AuthService;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/sign-up")
    public ResponseEntity<UserSignUpResponse> signUp(@Valid @RequestBody UserSignUpRequest userSignUpRequest,
                                                     HttpServletResponse response,
                                                     BindingResult bindingResult) throws BindException {
        if (bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        }

        UserSignUpResponse createdUser = this.authService.signUpUser(userSignUpRequest);

        Authentication authentication = this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                userSignUpRequest.username(),
                userSignUpRequest.password()
        ));

        if (authentication instanceof UsernamePasswordAuthenticationToken &&
                authentication.getPrincipal() instanceof UserInfoDto userInfoDto) {

            response.addCookie(authService.getTokenCookie(userInfoDto));
        }

        return ResponseEntity.ok(createdUser);
    }

    @PostMapping("/sign-in")
    public ResponseEntity<UserLoginResponse> signIn(@Valid @RequestBody UserLoginRequest userLoginRequest,
                                                    HttpServletResponse response,
                                                    BindingResult bindingResult) throws BindException {
        if (bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        }

        UserLoginResponse loginResponse = this.authService.signInUser(userLoginRequest);

        Authentication authentication = this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                userLoginRequest.identifier(),
                userLoginRequest.password()
        ));

        if (authentication instanceof UsernamePasswordAuthenticationToken &&
                authentication.getPrincipal() instanceof UserInfoDto userInfoDto) {

            response.addCookie(authService.getTokenCookie(userInfoDto));
        }

        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping("/sign-out")
    public ResponseEntity<?> signOut(HttpServletRequest request, HttpServletResponse response) {

        this.authService.signOutUser(request, response);

        return ResponseEntity.noContent().build();
    }
}
