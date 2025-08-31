package ru.mkenopsia.tasktrackerbackend.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.mkenopsia.tasktrackerbackend.dto.TokenDto;
import ru.mkenopsia.tasktrackerbackend.dto.UserLoginRequest;
import ru.mkenopsia.tasktrackerbackend.dto.UserSignUpRequest;
import ru.mkenopsia.tasktrackerbackend.dto.UserSignUpResponse;
import ru.mkenopsia.tasktrackerbackend.entity.User;
import ru.mkenopsia.tasktrackerbackend.mapper.UserMapper;
import ru.mkenopsia.tasktrackerbackend.utils.CookieUtils;
import ru.mkenopsia.tasktrackerbackend.utils.JwtTokenUtils;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;
    private final UserMapper userMapper;
    private final JwtTokenUtils jwtTokenUtils;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    public UserSignUpResponse signUpUser(UserSignUpRequest userSignUpRequest, HttpServletResponse response) {
        User user = userMapper.toEntity(userSignUpRequest);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        User createdUser = userService.save(user);

        this.provideToken(createdUser, response);

        return userMapper.toSignUpResponse(user);
    }

    public void signInUser(UserLoginRequest userLoginRequest, HttpServletResponse response) {
        this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                userLoginRequest.identifier(),
                userLoginRequest.password()
        ));

        User user = this.userService.findByUsernameOrEmail(userLoginRequest.identifier());

        this.provideToken(user, response);
    }


    private void provideToken(User user, HttpServletResponse response) {
        TokenDto token = jwtTokenUtils.generateToken(user);
        Cookie cookie = new Cookie("__Host-auth-token", token.token());
        cookie.setPath("/");
        cookie.setDomain(null);
        cookie.setSecure(true);
        cookie.setHttpOnly(true);
        cookie.setMaxAge((int) ChronoUnit.SECONDS.between(Instant.now(), token.expirationTime().toInstant()));

        response.addCookie(cookie);
    }

    public void signOutUser(HttpServletRequest request, HttpServletResponse response) {
        Cookie jSessionIdCookie = new Cookie("JSESSIONID", "");
        jSessionIdCookie.setMaxAge(0);
        jSessionIdCookie.setPath("/");
        jSessionIdCookie.setHttpOnly(true);

        response.addCookie(jSessionIdCookie);

        try {
            Cookie jwtBearerCookie = CookieUtils.getJwtBearerCookie(request.getCookies());

            jwtBearerCookie.setPath("/");
            jwtBearerCookie.setDomain(null);
            jwtBearerCookie.setSecure(true);
            jwtBearerCookie.setHttpOnly(true);
            jwtBearerCookie.setMaxAge(0);

            response.addCookie(jwtBearerCookie);
        } catch (NoSuchElementException ignored) {

        }
    }
}
