package com.example.auth_service.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    public String generateToken(String email, String role, String userId, String name, String departmentId) {
        return Jwts.builder()
                .subject(email)
                .claim("role", role)
                .claim("userId", userId)
                .claim("name", name)                  // Add name
                .claim("email", email)                // Explicitly add email
                .claim("departmentId", departmentId)  // Add departmentId
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)))
                .compact();
    }

    // For backward compatibility - delegates to the new method
    public String generateToken(String email, String role, String userId) {
        return generateToken(email, role, userId, null, null);
    }

    public Claims validateToken(String token) {
        return Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)))
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean isTokenExpired(String token) {
        Claims claims = validateToken(token);
        return claims.getExpiration().before(new Date());
    }

    public String getEmailFromToken(String token) {
        Claims claims = validateToken(token);
        return claims.getSubject();
    }

    public String getRoleFromToken(String token) {
        Claims claims = validateToken(token);
        return claims.get("role", String.class);
    }
}