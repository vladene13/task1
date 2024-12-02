package com.example.task1.service;

import com.example.task1.domain.Task;
import com.example.task1.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    // here you can list all tasks without owner info
    public List<Task> getAllTasksWithoutOwner() {
        List<Task> tasks = taskRepository.findAll();
        tasks.forEach(task -> task.setOwner(null)); //this line removes the owner details
        return tasks;
    }

    // here you can get the task by ID
    public Task getTaskById(Long id) {
        Optional<Task> task = taskRepository.findById(id);
        return task.orElse(null);
    }

    // here you can list the tasks by owner ID
    public List<Task> getTasksByOwner(Long ownerId) {
        return taskRepository.findByOwnerId(ownerId);
    }

    // here you create a new task
    public Task createTask(Task task) {
        return taskRepository.save(task);
    }

    // here you update the remaining effort
    public Task updateRemainingEffort(Long id, int remainingEffort) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Task not found with ID: " + id));
        task.setRemainingEffort(remainingEffort);
        return taskRepository.save(task);
    }

    // here you delete a task
    public void deleteTask(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new IllegalArgumentException("Task not found with ID: " + id);
        }
        taskRepository.deleteById(id);
    }
}
