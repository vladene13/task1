package com.example.task1.controller;

import com.example.task1.util.JwtUtil;
import com.example.task1.service.OwnerService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

        import java.util.Map;

@RestController
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final OwnerService ownerService;

    public AuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil, OwnerService ownerService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.ownerService = ownerService;
    }


    @PostMapping("/login")
    public Map<String, String> login(@RequestBody Map<String, String> loginData) {
        String email = loginData.get("email");
        String password = loginData.get("password");

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));

            String token = jwtUtil.generateToken(email);
            return Map.of("token", token);
        } catch (AuthenticationException e) {
            return Map.of("message", "Invalid credentials");
        }
    }
}

