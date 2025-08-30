package ru.mkenopsia.tasktrackerbackend.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.mkenopsia.tasktrackerbackend.dto.TokenDto;
import ru.mkenopsia.tasktrackerbackend.entity.User;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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
        return this.getAllClaimsFromToken(token).getSubject();
    }

    public String getEmail(String token) {
        return this.getAllClaimsFromToken(token).get("email", String.class);
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
