package com.example.task1.controller;

import com.example.task1.domain.Task;
import com.example.task1.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TaskControllerTest {

    @Mock
    private TaskService taskService;

    @InjectMocks
    private TaskController taskController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllTasks() {
        List<Task> tasks = Arrays.asList(new Task(), new Task());
        when(taskService.getAllTasksWithoutOwner()).thenReturn(tasks);

        List<Task> result = taskController.getAllTasks();
        assertEquals(2, result.size());
        verify(taskService, times(1)).getAllTasksWithoutOwner();
    }

    @Test
    void testGetTaskById() {
        Task task = new Task();
        task.setId(1L);
        when(taskService.getTaskById(1L)).thenReturn(task);

        Task result = taskController.getTaskById(1L);
        assertEquals(1L, result.getId());
        verify(taskService, times(1)).getTaskById(1L);
    }

    @Test
    void testGetTasksByOwner() {
        List<Task> tasks = Arrays.asList(new Task(), new Task());
        when(taskService.getTasksByOwner(1L)).thenReturn(tasks);

        List<Task> result = taskController.getTasksByOwner(1L);
        assertEquals(2, result.size());
        verify(taskService, times(1)).getTasksByOwner(1L);
    }

    @Test
    void testCreateTask() {
        Task task = new Task();
        when(taskService.createTask(task)).thenReturn(task);

        Task result = taskController.createTask(task);
        assertNotNull(result);
        verify(taskService, times(1)).createTask(task);
    }

    @Test
    void testUpdateRemainingEffort() {
        Task task = new Task();
        when(taskService.updateRemainingEffort(1L, 5)).thenReturn(task);

        ResponseEntity<Task> response = taskController.updateRemainingEffort(1L, 5);
        assertNotNull(response.getBody());
        verify(taskService, times(1)).updateRemainingEffort(1L, 5);
    }

    @Test
    void testDeleteTask() {
        doNothing().when(taskService).deleteTask(1L);

        taskController.deleteTask(1L);
        verify(taskService, times(1)).deleteTask(1L);
    }
}
