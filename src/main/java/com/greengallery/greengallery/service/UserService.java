package com.greengallery.greengallery.service;

import com.greengallery.greengallery.model.User;
import com.greengallery.greengallery.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User findByUsername(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        return user.orElse(null);
    }

    // Optional: find by email
    public User findByEmail(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        return user.orElse(null);
    }

    // Save user (register)
    public User saveUser(User user) {
        return userRepository.save(user);
    }
}