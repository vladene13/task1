package com.example.task1.controller;

import com.example.task1.domain.Owner;
import com.example.task1.service.OwnerService;
import com.example.task1.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AuthControllerTest {
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private OwnerService ownerService;

    private AuthController authController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        authController = new AuthController(authenticationManager, new JwtUtil(), ownerService);
    }

    @Test
    void login_ShouldReturnToken_WhenCredentialsAreValid() {
        String email = "test@test.com";
        String password = "password";
        Map<String, String> loginData = Map.of("email", email, "password", password);

        // Creează un mock al obiectului Authentication
        Authentication authentication = mock(Authentication.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);

        // Folosește un mock pentru JwtUtil
        JwtUtil jwtUtilMock = mock(JwtUtil.class);
        when(jwtUtilMock.generateToken(email)).thenReturn("test-token");

        // Iniezi controller-ul cu mock-ul JwtUtil
        authController = new AuthController(authenticationManager, jwtUtilMock, ownerService);

        // Apelezi metoda login
        Map<String, String> response = authController.login(loginData);

        // Verifici rezultatul
        assertEquals("test-token", response.get("token"));
    }



    @Test
    void login_ShouldReturnErrorMessage_WhenAuthenticationFails() {
        String email = "test@test.com";
        String password = "wrongpassword";
        Map<String, String> loginData = Map.of("email", email, "password", password);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Invalid credentials"));

        Map<String, String> response = authController.login(loginData);

        assertEquals("Invalid credentials", response.get("message"));
    }

    @Test
    void register_ShouldReturnSuccessMessage() {
        Map<String, String> registerData = Map.of(
                "email", "test@test.com",
                "name", "Test User",
                "password", "password"
        );

        Owner savedOwner = new Owner();
        savedOwner.setId(1L);
        when(ownerService.registerOwner(any(Owner.class))).thenReturn(savedOwner);

        Map<String, String> response = authController.register(registerData);

        assertEquals("User registered successfully", response.get("message"));
        assertEquals("1", response.get("id"));
        verify(ownerService).registerOwner(any(Owner.class));
    }
}
