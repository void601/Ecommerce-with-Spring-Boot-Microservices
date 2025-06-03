package com.example.UserServiceApplication.config;

// Import necessary classes
import com.example.UserServiceApplication.model.Users;
import com.example.UserServiceApplication.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * This class initializes an admin user in the database when the application starts.
 * It implements CommandLineRunner, allowing it to execute logic after the Spring Boot application has started.
 */
@Component // Marks this class as a Spring-managed component
public class AdminUserInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository; // Repository to interact with the Users table

    @Autowired
    private PasswordEncoder passwordEncoder; // Encoder to hash passwords securely



    /**
     * This method is executed after the application starts.
     * It checks if an admin user exists; if not, it creates one.
     *
     * @param args Command-line arguments passed to the application
     * @throws Exception If any exception occurs during execution
     */
    @Override
    @Transactional // Ensures the database operation runs within a transaction
    public void run(String... args) throws Exception {
        // Check if the admin user already exists in the database
        if (userRepository.findByUsername("admin").isEmpty()) {
            // Create a new admin user
            Users admin = new Users();
            admin.setUsername("admin"); // Set the username
            admin.setEmail("admin@admin.com"); // Set the email
            admin.setPassword(passwordEncoder.encode("admin123")); // Encrypt the password before saving
            admin.setRole("ADMIN"); // Assign the admin role
            // Save the new admin user to the database
            userRepository.save(admin);
            System.out.println("Admin user created successfully."); // Log message
        }
    }
}
