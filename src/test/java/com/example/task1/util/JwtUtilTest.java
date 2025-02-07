package com.example.task1.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {

    private JwtUtil jwtUtil;
    private String validToken;
    private String expiredToken;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
        validToken = jwtUtil.generateToken("user@example.com");

        // Creare token expirat manual (scăzând durata de expirare)
        expiredToken = jwtUtil.generateToken("expired@example.com");
        try {
            Thread.sleep(2000); // Simulăm trecerea timpului pentru expirare (doar pentru demonstrație)
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testGenerateToken() {
        assertNotNull(validToken);
        assertTrue(validToken.startsWith("eyJ")); // JWT începe de obicei cu 'eyJ'
    }

    @Test
    void testExtractEmail() {
        String email = jwtUtil.extractEmail(validToken);
        assertEquals("user@example.com", email);
    }

    @Test
    void testIsTokenExpired_ValidToken() {
        assertFalse(jwtUtil.isTokenExpired(validToken));
    }

//    @Test
//    void testIsTokenExpired_ExpiredToken() {
//        try {
//            jwtUtil.isTokenExpired(expiredToken);
//            fail("Expected RuntimeException for expired token");
//        } catch (RuntimeException e) {
//            assertTrue(e.getCause() instanceof ExpiredJwtException);
//        }
//    }

    @Test
    void testValidateToken_ValidToken() {
        assertTrue(jwtUtil.validateToken(validToken));
    }

    @Test
    void testValidateToken_InvalidToken() {
        assertFalse(jwtUtil.validateToken("invalid.token.value"));
    }

    @Test
    void testExtractClaims_InvalidToken() {
        assertThrows(RuntimeException.class, () -> jwtUtil.extractClaims("invalid.token.value"));
    }
}
