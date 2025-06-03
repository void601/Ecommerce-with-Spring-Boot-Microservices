package com.example.UserServiceApplication.serviceimpl;

import com.example.UserServiceApplication.model.MyUserDetails;
import com.example.UserServiceApplication.model.Users;
import com.example.UserServiceApplication.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Custom implementation of Spring Security's UserDetailsService.
 * This service is responsible for retrieving user details from the database
 * to facilitate authentication and authorization.
 */
@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository; // Repository to interact with user data in DB

    /**
     * Loads user details by email (used as username in authentication).
     * <p>
     * Steps:
     * - Fetch the user from the database using `findByEmail`.
     * - If the user is not found, throws `UsernameNotFoundException`.
     * - If found, wraps the user entity inside `MyUserDetails` (which implements UserDetails).
     *
     * @param email The email of the user attempting to authenticate.
     * @return UserDetails containing authentication-related information.
     * @throws UsernameNotFoundException if the user is not found in the database.
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Retrieve user by email from the database
        Optional<Users> user = userRepository.findByEmail(email);

        // If user is not found, throw an exception
        if (user.isEmpty()) {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }

        // Convert Users entity to Spring Security's UserDetails implementation
        return new MyUserDetails(user.get());
    }
}
