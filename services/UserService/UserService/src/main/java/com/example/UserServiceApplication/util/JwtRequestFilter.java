package com.example.UserServiceApplication.util;

import com.example.UserServiceApplication.model.MyUserDetails;
import com.example.UserServiceApplication.serviceimpl.MyUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

/**
 * JWT Authentication Filter that executes once per request.
 * <p>
 * Responsibilities:
 * - Extracts the JWT token from the Authorization header.
 * - Validates the token and sets authentication in the SecurityContext.
 * - Skips authentication for login and registration endpoints.
 */
@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private MyUserDetailsService userDetailsService; // Service to load user details from the database

    @Autowired
    private JwtUtil jwtUtil; // Utility class for JWT token operations

    /**
     * Filters incoming HTTP requests to apply JWT authentication.
     *
     * @param request  Incoming HTTP request.
     * @param response Outgoing HTTP response.
     * @param chain    Filter chain to pass control to the next filter.
     * @throws ServletException if servlet-related errors occur.
     * @throws IOException      if an input-output error occurs.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        // Retrieve the Authorization header from the HTTP request
        final String authorizationHeader = request.getHeader("Authorization");

        String email = null; // Stores the email extracted from the JWT token
        String jwt = null; // Stores the extracted JWT token
        String requestURI = request.getRequestURI(); // Get the URI of the requested resource

        /**
         * Bypass JWT authentication for specific public endpoints.
         * These endpoints should be accessible without authentication:
         * - /users/login (User login)
         * - /users/register (User registration)
         */
        if (requestURI.equals("/users/login") || requestURI.equals("/users/register")) {
            chain.doFilter(request, response); // Skip JWT authentication and proceed
            return;
        }

        // Check if the Authorization header is present and formatted correctly
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7); // Remove "Bearer " prefix
            email = jwtUtil.extractEmail(jwt); // Extract email (username) from token
        }

        /**
         * If a valid email is extracted and the user is not already authenticated,
         * validate the JWT token and set authentication in the SecurityContext.
         */
        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // Load user details from the database
            MyUserDetails userDetails = (MyUserDetails) this.userDetailsService.loadUserByUsername(email);

            // Validate the JWT token against the user details
            if (jwtUtil.validateToken(jwt, userDetails)) {
                // Create an authentication token with user details and granted authorities
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                // Attach additional details to the authentication token
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Set the authentication token in the SecurityContext
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }

        // Continue processing the request
        chain.doFilter(request, response);
    }
}
