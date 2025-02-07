package com.example.task1.domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

    @Test
    void testTaskGettersAndSetters() {
        Task task = new Task();
        task.setId(1L);
        task.setTitle("Test Title");
        task.setDescription("Test Description");
        task.setRemainingEffort(10);
        Owner owner = new Owner();
        owner.setId(2L);
        task.setOwner(owner);

        assertEquals(1L, task.getId());
        assertEquals("Test Title", task.getTitle());
        assertEquals("Test Description", task.getDescription());
        assertEquals(10, task.getRemainingEffort());
        assertEquals(2L, task.getOwner().getId());
    }
}