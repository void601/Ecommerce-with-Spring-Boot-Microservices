package com.example.UserServiceApplication.controller;

// Import necessary dependencies
import com.example.UserServiceApplication.model.LoginData;
import com.example.UserServiceApplication.model.MyUserDetails;
import com.example.UserServiceApplication.model.Users;
import com.example.UserServiceApplication.serviceimpl.MyUserDetailsService;
import com.example.UserServiceApplication.serviceimpl.UserServiceImpl;
import com.example.UserServiceApplication.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.ListView;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * REST Controller for user-related operations.
 * - Handles user registration, authentication (login), updating, and deletion.
 */
@RestController // Marks this class as a REST controller
@RequestMapping("/users") // Base path for all user-related endpoints
public class UserController {

    @Autowired
    private UserServiceImpl userService; // Service for user operations

    @Autowired
    private MyUserDetailsService myUserDetailsService; // Custom user details service

    @Autowired
    private AuthenticationManager authenticationManager; // Spring Security authentication manager

    @Autowired
    private JwtUtil jwtUtil; // Utility for JWT token generation and validation

    /**
     * Registers a new user.
     *
     * @param user The user object received from the request body.
     * @return ResponseEntity containing the registered user details.
     */
    @PostMapping("/register")
    public ResponseEntity<Users> registerUser(@RequestBody Users user) {
        Users registeredUser = userService.registerUser(user); // Call service to register user
        return ResponseEntity.ok(registeredUser); // Return registered user details in response
    }

    @GetMapping("/profile")
    public ResponseEntity<Users> getUserDetails(@RequestHeader("Authorization") String token) {
        String extractToken = token.substring(7);
        System.out.println("Token: "+extractToken);
        String email = jwtUtil.extractEmail(extractToken); // Extract email from JWT
        MyUserDetails userDetails = (MyUserDetails) myUserDetailsService.loadUserByUsername(email);
        Users userDetail = userService.getUserDetails(userDetails.getId()); // Call service to register user
        return ResponseEntity.ok(userDetail); // Return registered user details in response
    }

    /**
     * Authenticates a user and returns a JWT token if credentials are valid.
     *
     * @param email    The email of the user.
     * @param password The password of the user.
     * @return ResponseEntity containing the JWT token or an error message.
     */
    @PostMapping("/login")
    public ResponseEntity<LoginData> loginUser(@RequestParam String email, @RequestParam String password) {
        try {
            // Authenticate user using AuthenticationManager
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password)
            );

            // Retrieve authenticated user details
            MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();

            // Generate JWT token for authenticated user
            String token = jwtUtil.generateToken(userDetails);
            Long id=userDetails.getId();
            return ResponseEntity.ok(new LoginData(id,token)); // Return token in response
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null); // Return error response
        }
    }

    /**
     * Updates user details. Access restricted to the user themselves or an admin.
     *
     * @param userId      The ID of the user to be updated (from URL path).
     * @param updatedUser The updated user details (from request body).
     * @return ResponseEntity containing the updated user details.
     */
    @PreAuthorize("#userId == authentication.principal.getId() or hasAuthority('ROLE_ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<Users> updateUser(@PathVariable("id") Long userId, @RequestBody Users updatedUser) {
        Users user = userService.updateUser(userId, updatedUser); // Call service to update user
        return ResponseEntity.ok(user); // Return updated user details
    }

    /**
     * Deletes a user. Access restricted to the user themselves or an admin.
     *
     * @param userId The ID of the user to be deleted (from URL path).
     * @return ResponseEntity with no content on successful deletion.
     */
    @PreAuthorize("#userId == authentication.principal.getId() or hasAuthority('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Users> deleteUser(@PathVariable("id") Long userId) {
        userService.deleteUser(userId); // Call service to delete user
        return ResponseEntity.noContent().build(); // Return no content response
    }

    @PostMapping("auth/validate-token")
    public ResponseEntity<Map<String, Object>> validateToken(@RequestHeader("Authorization") String authHeader) {
        Map<String, Object> response = new HashMap<>();
        boolean isValid = false;
        List<String> roles = Collections.emptyList();
        String email = null;

        try {
            String token = authHeader.substring(7); // Remove "Bearer " prefix
            System.out.println(token);
            email = jwtUtil.extractEmail(token); // Extract email from JWT
            System.out.println(email);
            roles = jwtUtil.extractRoles(token); // Extract roles from JWT
            System.out.println(roles);

            // Load user details from the database
            MyUserDetails userDetails = (MyUserDetails) myUserDetailsService.loadUserByUsername(email);
            System.out.println(userDetails);
            // Validate the token
            isValid = jwtUtil.validateToken(token, userDetails);
            System.out.println(isValid);
            // Populate the response map
            response.put("isValid", isValid);
            response.put("roles", roles);
            response.put("email", email); // Include email in the response

            // Return the response
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            // Populate the response map with invalid status, empty roles, and null email
            System.out.println(e.getMessage());
            response.put("isValid", isValid);
            response.put("roles", roles);
            response.put("email", email);

            // Return the response
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }
}
