package com.example.UserServiceApplication.serviceimpl;

import com.example.UserServiceApplication.model.Users;
import com.example.UserServiceApplication.repository.UserRepository;
import com.example.UserServiceApplication.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Implementation of the UserService interface.
 * <p>
 * This service handles user registration, authentication, updating, and deletion.
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository; // Repository for user database operations

    @Autowired
    private PasswordEncoder passwordEncoder; // Encoder for hashing passwords

    /**
     * Registers a new user by encoding their password and assigning a default role.
     *
     * @param user The user entity to be registered.
     * @return The saved user entity.
     */
    @Override
    @Transactional
    public Users registerUser(Users user) {
        user.setRole("USER"); // Assigns default role as "USER"
        user.setPassword(passwordEncoder.encode(user.getPassword())); // Encrypts the password before storing it
        return userRepository.save(user); // Saves the user entity in the database
    }

    @Override
    @Transactional
    public Users getUserDetails(Long userId) {
        return userRepository.findById(userId).orElseThrow(()->new RuntimeException("User not found")); // Saves the user entity in the database
    }

    /**
     * Authenticates a user by checking their email and password.
     *
     * @param email    The email entered by the user.
     * @param password The password entered by the user.
     * @return An Optional containing the user if authentication is successful, otherwise empty.
     */
    @Override
    public Optional<Users> authenticateUser(String email, String password) {
        Optional<Users> user = userRepository.findByEmail(email); // Fetches user by email
        // Checks if the user exists and if the password matches the encoded password in the database
        if (user.isPresent() && passwordEncoder.matches(password, user.get().getPassword())) {
            return user; // Return the authenticated user
        }
        return Optional.empty(); // Return empty if authentication fails
    }

    /**
     * Updates an existing user's details.
     *
     * @param userId The ID of the user to be updated.
     * @param user   The updated user details.
     * @return The updated user entity.
     * @throws RuntimeException if the user is not found.
     */
    @Override
    @Transactional
    public Users updateUser(Long userId, Users user) {
        // Fetch the existing user or throw an exception if not found
        Users existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Update user details
        existingUser.setEmail(user.getEmail());
        existingUser.setUsername(user.getUsername());

        return userRepository.save(existingUser); // Save and return the updated user
    }

    /**
     * Deletes a user from the database.
     *
     * @param userId The ID of the user to be deleted.
     */
    @Override
    @Transactional
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId); // Delete the user by ID
    }
}
