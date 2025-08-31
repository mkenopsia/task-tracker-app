package ru.mkenopsia.tasktrackerbackend.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import ru.mkenopsia.tasktrackerbackend.dto.TokenDto;
import ru.mkenopsia.tasktrackerbackend.entity.User;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@Component
public class JwtTokenUtils {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.lifetime}")
    private Duration jwtLifetime;

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    public TokenDto generateToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("email", user.getEmail());

        Date issuedTime = new Date();
        Date expirationTime = new Date(issuedTime.getTime() + jwtLifetime.toMillis());

        String token = Jwts.builder()
                .claims(claims)
                .subject(user.getUsername())
                .issuedAt(issuedTime)
                .expiration(expirationTime)
                .signWith(getSigningKey())
                .compact();

        return new TokenDto(token, expirationTime);
    }

    public String getUsername(String token) {
        return this.getAllClaims(token).getSubject();
    }

    public String getEmail(String token) {
        return this.getAllClaims(token).get("email", String.class);
    }

    public boolean isTokenValid(String token) {
        try {
            Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private Claims getAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String getToken(Cookie[] cookies) {
        if(cookies == null) return null;

        return CookieUtils.getJwtBearerCookie(cookies).getValue();
    }
}
