package com.example.task1.service;

import com.example.task1.domain.Comment;
import com.example.task1.domain.Task;
import com.example.task1.domain.Owner;
import com.example.task1.repository.CommentRepository;
import com.example.task1.repository.TaskRepository;
import com.example.task1.repository.OwnerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final TaskRepository taskRepository;
    private final OwnerRepository ownerRepository;

    public CommentService(CommentRepository commentRepository,
                          TaskRepository taskRepository,
                          OwnerRepository ownerRepository) {
        this.commentRepository = commentRepository;
        this.taskRepository = taskRepository;
        this.ownerRepository = ownerRepository;
    }

    public List<Comment> getCommentsByTaskId(Long taskId) {
        return commentRepository.findByTaskId(taskId);
    }

    public Comment createComment(Comment comment, Long taskId, Long ownerId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task not found"));
        Owner owner = ownerRepository.findById(ownerId)
                .orElseThrow(() -> new IllegalArgumentException("Owner not found"));

        comment.setTask(task);
        comment.setOwner(owner);
        return commentRepository.save(comment);
    }

    public Comment updateComment(Long id, String newContent) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Comment not found"));
        comment.setContent(newContent);
        return commentRepository.save(comment);
    }

    public void deleteComment(Long id) {
        if (!commentRepository.existsById(id)) {
            throw new IllegalArgumentException("Comment not found");
        }
        commentRepository.deleteById(id);
    }
}