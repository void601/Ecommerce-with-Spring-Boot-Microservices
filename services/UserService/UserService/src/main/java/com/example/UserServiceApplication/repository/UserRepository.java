package com.example.UserServiceApplication.repository;

// Import necessary dependencies
import com.example.UserServiceApplication.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for performing database operations on the Users entity.
 * - Extends JpaRepository to provide CRUD operations.
 * - Includes custom methods to find users by username and email.
 */
@Repository // Marks this interface as a Spring Data repository
public interface UserRepository extends JpaRepository<Users, Long> {

    /**
     * Finds a user by their username.
     *
     * @param username The username to search for.
     * @return An Optional containing the user if found, otherwise empty.
     */
    Optional<Users> findByUsername(String username);

    /**
     * Finds a user by their email.
     *
     * @param email The email to search for.
     * @return An Optional containing the user if found, otherwise empty.
     */
    Optional<Users> findByEmail(String email);
}
