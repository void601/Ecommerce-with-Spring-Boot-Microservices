package com.example.UserServiceApplication.service;

import com.example.UserServiceApplication.model.Users;
import java.util.Optional;

/**
 * Service interface for user-related operations.
 * Defines methods for registering, authenticating, updating, and deleting users.
 */
public interface UserService {

    /**
     * Registers a new user in the system.
     *
     * @param user The user details to register.
     * @return The registered user with updated details (e.g., ID).
     */
    Users registerUser(Users user);

    Users getUserDetails(Long userId);

    /**
     * Authenticates a user based on email and password.
     *
     * @param email The user's email.
     * @param password The user's password.
     * @return An Optional containing the authenticated user if valid, otherwise empty.
     */
    Optional<Users> authenticateUser(String email, String password);

    /**
     * Updates an existing user's details.
     *
     * @param userId The ID of the user to update.
     * @param user The updated user details.
     * @return The updated user object.
     */
    Users updateUser(Long userId, Users user);

    /**
     * Deletes a user from the system.
     *
     * @param userId The ID of the user to delete.
     */
    void deleteUser(Long userId);
}
