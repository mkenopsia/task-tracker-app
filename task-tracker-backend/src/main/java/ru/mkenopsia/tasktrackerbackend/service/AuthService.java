package ru.mkenopsia.tasktrackerbackend.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.mkenopsia.tasktrackerbackend.dto.TokenDto;
import ru.mkenopsia.tasktrackerbackend.dto.UserSignUpRequest;
import ru.mkenopsia.tasktrackerbackend.entity.User;
import ru.mkenopsia.tasktrackerbackend.mapper.UserMapper;
import ru.mkenopsia.tasktrackerbackend.utils.JwtTokenUtils;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;
    private final JwtTokenUtils jwtTokenUtils;
    private final UserMapper userMapper;

    public User signUpUser(User user, HttpServletResponse response) {
        User createdUser = userService.save(user);

        TokenDto token = jwtTokenUtils.generateToken(user);
        Cookie cookie = new Cookie("__Host-auth-token", token.token());
        cookie.setPath("/");
        cookie.setDomain(null);
        cookie.setSecure(true);
        cookie.setHttpOnly(true);
        cookie.setMaxAge((int) ChronoUnit.SECONDS.between(Instant.now(), token.expirationTime().toInstant()));

        response.addCookie(cookie);

        System.out.println("\n" + token.token() + "\n");

        return createdUser;
    }

}
