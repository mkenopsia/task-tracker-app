package ru.mkenopsia.tasktrackerbackend.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Service;
import ru.mkenopsia.tasktrackerbackend.dto.*;
import ru.mkenopsia.tasktrackerbackend.entity.User;
import ru.mkenopsia.tasktrackerbackend.mapper.UserMapper;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;
    private final UserMapper userMapper;
    private final JwtTokenService jwtTokenService;
    private final AuthenticationManager authenticationManager;
    private final TokenCookieService tokenCookieService;

    public UserSignUpResponse signUpUser(UserSignUpRequest userSignUpRequest) {
        User user = userMapper.toEntity(userSignUpRequest);

        this.userService.save(user);

        return userMapper.toSignUpResponse(user);
    }

    public UserLoginResponse signInUser(UserLoginRequest userLoginRequest) {
        User user = this.userService.findByUsernameOrEmail(userLoginRequest.identifier());

        return userMapper.toUserLoginResponse(user);
    }

    public void signOutUser(HttpServletRequest request, HttpServletResponse response) {
        try {
            response.addCookie(this.tokenCookieService.getDeletionCookie());
        } catch (NoSuchElementException ignored) {

        }
    }

    public Cookie getTokenCookie(String username, String email) {
        return tokenCookieService.provideCookieWithRefreshedToken(jwtTokenService.generateToken(username, email));
    }
}
