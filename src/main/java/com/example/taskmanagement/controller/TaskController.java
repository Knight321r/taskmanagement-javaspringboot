package com.example.taskmanagement.controller;

import com.example.taskmanagement.model.Task;
import com.example.taskmanagement.model.User;
import com.example.taskmanagement.repository.TaskRepository;
import com.example.taskmanagement.repository.UserRepository;
import com.example.taskmanagement.security.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/tasks")
public class TaskController {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public TaskController(TaskRepository taskRepository, UserRepository userRepository, JwtUtil jwtUtil) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping
    public ResponseEntity<String> createTask(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestBody Task taskDetails) {
        System.out.println("im herererer");
        // Extract the JWT token from the Authorization header
        String token = authorizationHeader.replace("Bearer ", "");

        // Validate the JWT token and retrieve the user ID
        String userId;
        try {
            userId = jwtUtil.extractSubject(token);
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Invalid or expired token");
        }

        // Retrieve the user from the database
        Optional<User> userOptional = userRepository.findById(Long.parseLong(userId));
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(401).body("User not found");
        }

        User user = userOptional.get();

        // Create and save the task
        Task task = new Task();
        task.setTitle(taskDetails.getTitle());
        task.setDescription(taskDetails.getDescription());
        task.setDueDate(taskDetails.getDueDate());
        task.setUser(user);

        // Set default status if not provided
        if (task.getStatus() == null) {
            task.setStatus("PENDING");
        }

        taskRepository.save(task);

        return ResponseEntity.ok("Task created successfully");
    }

    @GetMapping
    public ResponseEntity<?> getAllTasks(
            @RequestHeader("Authorization") String authorizationHeader) {
        // Extract the JWT token from the Authorization header
        String token = authorizationHeader.replace("Bearer ", "");

        // Validate the JWT token and retrieve the user ID
        String userId;
        try {
            userId = jwtUtil.extractSubject(token);
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Invalid or expired token");
        }

        // Retrieve the user from the database
        Optional<User> userOptional = userRepository.findById(Long.parseLong(userId));
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(401).body("User not found");
        }

        User user = userOptional.get();

        // Retrieve all tasks for the user
        List<Task> userTasks = taskRepository.findByUser(user);

        return ResponseEntity.ok(userTasks);
    }

    @GetMapping("/{taskId}")
    public ResponseEntity<?> getTaskById(
            @RequestHeader("Authorization") String authorizationHeader,
            @PathVariable Long taskId) {
        // Extract the JWT token from the Authorization header
        String token = authorizationHeader.replace("Bearer ", "");

        // Validate the JWT token and retrieve the user ID
        String userId;
        try {
            userId = jwtUtil.extractSubject(token);
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Invalid or expired token");
        }

        // Retrieve the user from the database
        Optional<User> userOptional = userRepository.findById(Long.parseLong(userId));
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(401).body("User not found");
        }

        User user = userOptional.get();

        // Find the task by ID
        Optional<Task> taskOptional = taskRepository.findById(taskId);
        if (taskOptional.isEmpty()) {
            return ResponseEntity.status(404).body("Task not found");
        }

        Task task = taskOptional.get();

        // Ensure the task belongs to the authenticated user
        if (!task.getUser().getId().equals(user.getId())) {
            return ResponseEntity.status(403).body("You do not have permission to access this task");
        }

        return ResponseEntity.ok(task);
    }

    @PutMapping("/{taskId}")
    public ResponseEntity<?> updateTaskById(
            @RequestHeader("Authorization") String authorizationHeader,
            @PathVariable Long taskId,
            @RequestBody Task taskDetails) {
        // Extract the JWT token from the Authorization header
        String token = authorizationHeader.replace("Bearer ", "");

        // Validate the JWT token and retrieve the user ID
        String userId;
        try {
            userId = jwtUtil.extractSubject(token);
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Invalid or expired token");
        }

        // Retrieve the user from the database
        Optional<User> userOptional = userRepository.findById(Long.parseLong(userId));
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(401).body("User not found");
        }

        User user = userOptional.get();

        // Find the existing task by ID
        Optional<Task> existingTaskOptional = taskRepository.findById(taskId);
        if (existingTaskOptional.isEmpty()) {
            return ResponseEntity.status(404).body("Task not found");
        }

        Task existingTask = existingTaskOptional.get();

        // Ensure the task belongs to the authenticated user
        if (!existingTask.getUser().getId().equals(user.getId())) {
            return ResponseEntity.status(403).body("You do not have permission to update this task");
        }

        // Update task fields
        // Only update fields that are provided in the request
        if (taskDetails.getTitle() != null) {
            existingTask.setTitle(taskDetails.getTitle());
        }
        if (taskDetails.getDescription() != null) {
            existingTask.setDescription(taskDetails.getDescription());
        }
        if (taskDetails.getDueDate() != null) {
            existingTask.setDueDate(taskDetails.getDueDate());
        }
        if (taskDetails.getStatus() != null) {
            // Optionally, you can add validation for status values
            existingTask.setStatus(taskDetails.getStatus());
        }

        // Save the updated task
        Task updatedTask = taskRepository.save(existingTask);

        return ResponseEntity.ok(updatedTask);
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<?> deleteTaskById(
            @RequestHeader("Authorization") String authorizationHeader,
            @PathVariable Long taskId) {
        // Extract the JWT token from the Authorization header
        String token = authorizationHeader.replace("Bearer ", "");

        // Validate the JWT token and retrieve the user ID
        String userId;
        try {
            userId = jwtUtil.extractSubject(token);
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Invalid or expired token");
        }

        // Retrieve the user from the database
        Optional<User> userOptional = userRepository.findById(Long.parseLong(userId));
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(401).body("User not found");
        }

        User user = userOptional.get();

        // Find the existing task by ID
        Optional<Task> existingTaskOptional = taskRepository.findById(taskId);
        if (existingTaskOptional.isEmpty()) {
            return ResponseEntity.status(404).body("Task not found");
        }

        Task existingTask = existingTaskOptional.get();

        // Ensure the task belongs to the authenticated user
        if (!existingTask.getUser().getId().equals(user.getId())) {
            return ResponseEntity.status(403).body("You do not have permission to delete this task");
        }

        // Delete the task
        taskRepository.delete(existingTask);

        return ResponseEntity.ok("Task deleted successfully");
    }

    @PatchMapping("/{taskId}/complete")
    public ResponseEntity<?> completeTask(
            @RequestHeader("Authorization") String authorizationHeader,
            @PathVariable Long taskId) {
        // Extract the JWT token from the Authorization header
        String token = authorizationHeader.replace("Bearer ", "");

        // Validate the JWT token and retrieve the user ID
        String userId;
        try {
            userId = jwtUtil.extractSubject(token);
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Invalid or expired token");
        }

        // Retrieve the user from the database
        Optional<User> userOptional = userRepository.findById(Long.parseLong(userId));
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(401).body("User not found");
        }

        User user = userOptional.get();

        // Find the existing task by ID
        Optional<Task> existingTaskOptional = taskRepository.findById(taskId);
        if (existingTaskOptional.isEmpty()) {
            return ResponseEntity.status(404).body("Task not found");
        }

        Task existingTask = existingTaskOptional.get();

        // Ensure the task belongs to the authenticated user
        if (!existingTask.getUser().getId().equals(user.getId())) {
            return ResponseEntity.status(403).body("You do not have permission to update this task");
        }

        // Update the task status to COMPLETED
        existingTask.setStatus("COMPLETED");

        // Save the updated task
        Task updatedTask = taskRepository.save(existingTask);

        return ResponseEntity.ok(updatedTask);
    }
}
