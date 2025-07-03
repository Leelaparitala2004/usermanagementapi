package com.example.usermanagementapi.controller;

import com.example.usermanagementapi.model.User;
import com.example.usermanagementapi.repository.UserRepository;
import jakarta.persistence.Column;
import jakarta.validation.Valid;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserRepository userRepository;

    // CREATE user
    @PostMapping
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
        logger.info("Creating new user: {}", user.getName());
        User savedUser = userRepository.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }

    // READ all users
    @GetMapping
    public List<User> getAllUsers() {
        logger.info("Fetching all users");
        return userRepository.findAll();
    }

    // READ user by ID
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        logger.info("Fetching user with ID: {}", id);
        Optional<User> user = userRepository.findById(id);
        return user.map(ResponseEntity::ok)
                .orElseGet(() -> {
                    logger.warn("User with ID {} not found", id);
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
                });
    }

    // UPDATE user
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @Valid @RequestBody User userDetails) {
        logger.info("Updating user with ID: {}", id);
        return userRepository.findById(id).map(existingUser -> {
            existingUser.setName(userDetails.getName());
            existingUser.setEmail(userDetails.getEmail());
            User updatedUser = userRepository.save(existingUser);
            logger.info("User updated successfully: {}", updatedUser.getName());
            return ResponseEntity.ok(updatedUser);
        }).orElseGet(() -> {
            logger.warn("User with ID {} not found for update", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        });
    }

    // DELETE user
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable Long id) {
        logger.info("Deleting user with ID: {}", id);
        return userRepository.findById(id).map(user -> {
            userRepository.deleteById(id);
            logger.info("User deleted successfully");
            return ResponseEntity.noContent().build();
        }).orElseGet(() -> {
            logger.warn("User with ID {} not found for deletion", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        });
    }


}
