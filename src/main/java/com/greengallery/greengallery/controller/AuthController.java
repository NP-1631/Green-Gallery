package com.greengallery.greengallery.controller;

import com.greengallery.greengallery.dto.LoginRequest;
import com.greengallery.greengallery.dto.RegisterRequest;
import com.greengallery.greengallery.model.User;
import com.greengallery.greengallery.repository.UserRepository;
import com.greengallery.greengallery.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/auth")

public class AuthController {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;


    // ──────────────── Register Endpoint ────────────────
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        try {
            User user = new User();
            user.setFirstName(request.getFirstName());
            user.setLastName(request.getLastName());
            user.setEmail(request.getEmail());
            user.setUsername(request.getUsername()); // use frontend username
            user.setPassword(request.getPassword()); // later encode
            user.setRole(request.getRole() != null ? request.getRole().toUpperCase() : "USER");

            User savedUser = userService.saveUser(user);

            Map<String, Object> response = new HashMap<>();
            response.put("id", savedUser.getId());
            response.put("username", savedUser.getUsername());
            response.put("role", savedUser.getRole());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            // log the exception
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Registration failed: " + e.getMessage());
        }
    }

    // ──────────────── Login Endpoint ────────────────
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        User user = userService.findByEmail(loginRequest.getEmail());

        if(user != null && user.getPassword().equals(loginRequest.getPassword())) {
            Map<String,Object> response = new HashMap<>();
            response.put("id", user.getId());
            response.put("username", user.getUsername()); // already combined
            response.put("role", user.getRole());
            return ResponseEntity.ok(response);
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
    }
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(
            @PathVariable Long id,
            @RequestBody User updatedUser){

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setFirstName(updatedUser.getFirstName());
        user.setLastName(updatedUser.getLastName());
        user.setUsername(updatedUser.getUsername());
        user.setEmail(updatedUser.getEmail());
        user.setPassword(updatedUser.getPassword());
        user.setRole(updatedUser.getRole());

        User savedUser = userRepository.save(user);

        return ResponseEntity.ok(savedUser);
    }
    @PutMapping("/{id}/change-password")
    public ResponseEntity<?> changePassword(
            @PathVariable Long id,
            @RequestBody Map<String,String> body){

        String currentPassword = body.get("currentPassword");
        String newPassword = body.get("newPassword");

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // check current password
        if(!user.getPassword().equals(currentPassword)){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Current password incorrect");
        }

        // update password
        user.setPassword(newPassword);
        userRepository.save(user);

        return ResponseEntity.ok(Map.of("message","Password updated successfully"));
    }
}