package com.example.task1.service;

import com.example.task1.domain.Owner;
import com.example.task1.repository.OwnerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.userdetails.UserDetails;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class OwnerServiceTest {
    @Mock
    private OwnerRepository ownerRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

    private OwnerService ownerService;

    @BeforeEach
    void setUp() {
        ownerRepository = mock(OwnerRepository.class);
        passwordEncoder = mock(PasswordEncoder.class);
        ownerService = new OwnerService(ownerRepository, passwordEncoder);
    }

    @Test
    void registerOwner_ShouldEncodePasswordAndSaveOwner() {
        // Arrange
        Owner owner = new Owner();
        owner.setEmail("test@test.com");
        owner.setPassword("password");

        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        when(ownerRepository.save(any(Owner.class))).thenReturn(owner);

        // Act
        Owner savedOwner = ownerService.registerOwner(owner);

        // Assert
        verify(passwordEncoder).encode("password");
        verify(ownerRepository).save(owner);
        assertEquals("encodedPassword", savedOwner.getPassword());
    }

    @Test
    void loadUserByUsername_ShouldReturnUserDetails() {
        // Arrange
        Owner owner = new Owner();
        owner.setEmail("test@test.com");
        owner.setPassword("password");

        when(ownerRepository.findByEmail("test@test.com")).thenReturn(owner);

        // Act
        UserDetails userDetails = ownerService.loadUserByUsername("test@test.com");

        // Assert
        assertNotNull(userDetails);
        assertEquals("test@test.com", userDetails.getUsername());
    }
}