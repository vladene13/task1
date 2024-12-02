package com.example.task1.controller;

import com.example.task1.domain.Task;
import com.example.task1.service.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    // here you list all tasks (without owner info)
    @GetMapping
    public List<Task> getAllTasks() {
        return taskService.getAllTasksWithoutOwner();
    }

    // here you get the task by ID
    @GetMapping("/{id}")
    public Task getTaskById(@PathVariable Long id) {
        return taskService.getTaskById(id);
    }

    // here you list the tasks by owner
    @GetMapping("/owner/{ownerId}")
    public List<Task> getTasksByOwner(@PathVariable Long ownerId) {
        return taskService.getTasksByOwner(ownerId);
    }

    // here you create a new task
    @PostMapping
    public Task createTask(@RequestBody Task task) {
        return taskService.createTask(task);
    }

    // here you update the remaining effort
    @PutMapping("/{id}/remaining-effort")
    public ResponseEntity<Task> updateRemainingEffort(@PathVariable Long id, @RequestBody int remainingEffort) {
        Task updatedTask = taskService.updateRemainingEffort(id, remainingEffort);
        return ResponseEntity.ok(updatedTask);
    }

    // here you delete a task
    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
    }
}
