package com.example.task1.filter;

import com.example.task1.util.JwtUtil;
import com.example.task1.service.OwnerService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.io.IOException;
import java.lang.reflect.Field;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class JwtFilterTest {

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private OwnerService ownerService;

    @Mock
    private ApplicationContext applicationContext;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @Mock
    private UserDetails userDetails;

    @InjectMocks
    private JwtFilter jwtFilter;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        jwtFilter = new JwtFilter(jwtUtil);

        // Setează applicationContext folosind reflecția
        Field appContextField = JwtFilter.class.getDeclaredField("applicationContext");
        appContextField.setAccessible(true);
        appContextField.set(jwtFilter, applicationContext);
    }

    @Test
    void testDoFilterInternal_ValidToken() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn("Bearer validtoken");
        when(jwtUtil.extractEmail("validtoken")).thenReturn("user@example.com");
        when(applicationContext.getBean(OwnerService.class)).thenReturn(ownerService);
        when(ownerService.loadUserByUsername("user@example.com")).thenReturn(userDetails);
        when(jwtUtil.isTokenExpired("validtoken")).thenReturn(false);
        when(userDetails.getAuthorities()).thenReturn(null);

        jwtFilter.doFilterInternal(request, response, filterChain);

        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    void testDoFilterInternal_InvalidToken() throws ServletException, IOException {
        String invalidToken = "header.payload.invalidsignature";
        String authorizationHeader = "Bearer " + invalidToken;

        when(request.getHeader("Authorization")).thenReturn(authorizationHeader);

        // Simulăm că validarea tokenului aruncă o excepție
        when(jwtUtil.validateToken(invalidToken))
                .thenThrow(new io.jsonwebtoken.SignatureException("JWT signature does not match"));

        jwtFilter.doFilterInternal(request, response, filterChain);

        // Ne asigurăm că SecurityContextHolder nu are autentificare setată
        assertNull(SecurityContextHolder.getContext().getAuthentication());

        // Verificăm că filtrul a permis continuarea procesării cererii
        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    void testDoFilterInternal_NoAuthorizationHeader() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn(null);

        jwtFilter.doFilterInternal(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain, times(1)).doFilter(request, response);
    }
}
