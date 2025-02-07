package com.example.task1.domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class OwnerTest {

    @Test
    void testOwnerGettersAndSetters() {
        Owner owner = new Owner();
        owner.setId(1L);
        owner.setName("John Doe");
        owner.setEmail("john.doe@example.com");
        owner.setPassword("securepassword");

        assertEquals(1L, owner.getId());
        assertEquals("John Doe", owner.getName());
        assertEquals("john.doe@example.com", owner.getEmail());
        assertEquals("securepassword", owner.getPassword());
    }
}
