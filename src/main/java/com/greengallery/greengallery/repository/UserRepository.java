package com.greengallery.greengallery.repository;

import com.greengallery.greengallery.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // Find by username
    Optional<User> findByUsername(String username);

    // Optional: Find by email
    Optional<User> findByEmail(String email);
}