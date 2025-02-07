package com.example.task1.util;

import com.example.task1.domain.Owner;
import com.example.task1.repository.OwnerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Arrays;

import static org.mockito.Mockito.*;

class DatabaseInitializerTest {
    @Mock
    private OwnerRepository ownerRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private DatabaseInitializer databaseInitializer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        databaseInitializer = new DatabaseInitializer(ownerRepository, passwordEncoder);
    }

    @Test
    void updatePasswordsInDatabase_ShouldEncryptPlainPasswords() {
        // Arrange
        Owner owner = new Owner();
        owner.setEmail("test@test.com");
        owner.setPassword("plain-password");

        when(ownerRepository.findAll()).thenReturn(Arrays.asList(owner));
        when(passwordEncoder.encode("plain-password")).thenReturn("encoded-password");

        // Act
        databaseInitializer.onApplicationEvent(mock(ApplicationReadyEvent.class));

        // Assert
        verify(passwordEncoder).encode("plain-password");
        verify(ownerRepository).save(owner);
    }

    @Test
    void updatePasswordsInDatabase_ShouldSkipNullPasswords() {
        // Arrange
        Owner owner = new Owner();
        owner.setEmail("test@test.com");
        owner.setPassword(null);

        when(ownerRepository.findAll()).thenReturn(Arrays.asList(owner));

        // Act
        databaseInitializer.onApplicationEvent(mock(ApplicationReadyEvent.class));

        // Assert
        verify(passwordEncoder, never()).encode(any());
        verify(ownerRepository, never()).save(any());
    }
}