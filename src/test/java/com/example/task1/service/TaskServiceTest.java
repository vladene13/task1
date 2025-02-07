package com.example.task1.service;

import com.example.task1.domain.Task;
import com.example.task1.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class TaskServiceTest {
    @Mock
    private TaskRepository taskRepository;

    private TaskService taskService;

    @BeforeEach
    void setUp() {
        taskRepository = mock(TaskRepository.class);
        taskService = new TaskService(taskRepository);
    }

    @Test
    void getAllTasksWithoutOwner_ShouldReturnTasksWithNullOwner() {
        // Arrange
        Task task1 = new Task();
        Task task2 = new Task();
        when(taskRepository.findAll()).thenReturn(Arrays.asList(task1, task2));

        // Act
        List<Task> tasks = taskService.getAllTasksWithoutOwner();

        // Assert
        assertNull(tasks.get(0).getOwner());
        assertNull(tasks.get(1).getOwner());
        verify(taskRepository).findAll();
    }

    @Test
    void updateRemainingEffort_ShouldUpdateEffort() {
        // Arrange
        Task task = new Task();
        task.setId(1L);
        task.setRemainingEffort(10);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        // Act
        Task updatedTask = taskService.updateRemainingEffort(1L, 5);

        // Assert
        assertEquals(5, updatedTask.getRemainingEffort());
        verify(taskRepository).save(task);
    }

    @Test
    void deleteTask_ShouldThrowException_WhenTaskNotFound() {
        // Arrange
        when(taskRepository.existsById(1L)).thenReturn(false);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> taskService.deleteTask(1L));
    }
}