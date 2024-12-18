package com.example.task1.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;

import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

    private static final Key SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private static final long EXPIRATION_TIME = 3600000; // 1 hour

    public static String generateToken(String username) {
        Date expirationDate = new Date(System.currentTimeMillis() + EXPIRATION_TIME);
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(expirationDate)
                .signWith(SECRET_KEY)
                .compact();
    }

    public static Claims validateToken(String token, String username) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public static String getUsernameFromToken(String token) {
        Claims claims = validateToken(token, "");
        return claims.getSubject();
    }

    public static boolean isTokenExpired(String token) {
        Claims claims = validateToken(token, "");
        return claims.getExpiration().before(new Date());
    }

    public static String extractEmail(String token) {
        Claims claims = validateToken(token, "");
        // we suppose that the email key is 'email'
        return claims.get("email", String.class);
    }
}

