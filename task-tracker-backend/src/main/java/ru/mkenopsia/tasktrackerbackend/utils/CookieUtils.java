package ru.mkenopsia.tasktrackerbackend.utils;

import jakarta.servlet.http.Cookie;

import java.util.NoSuchElementException;

public class CookieUtils {

    private CookieUtils() {
        throw new AssertionError();
    }

    public static Cookie getJwtBearerCookie(Cookie[] cookies) {
        if(cookies == null) {
            throw new NoSuchElementException("auth.error.jwt_token.token_not_found");
        }

        for(var cookie : cookies) {
            if(cookie.getName().equals("JWT")) {
                return cookie;
            }
        }

        throw new NoSuchElementException("auth.error.jwt_token.token_not_found");
    }
}
