package com.example.UserServiceApplication.config;

// Import required dependencies
import com.example.UserServiceApplication.serviceimpl.MyUserDetailsService;
import com.example.UserServiceApplication.util.JwtRequestFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

/**
 * Security configuration class for defining authentication and authorization settings.
 * - Enables Spring Security.
 * - Configures JWT authentication.
 * - Defines role-based access control.
 */
@Configuration // Marks this class as a configuration class
@EnableWebSecurity // Enables Spring Security for the application
@EnableMethodSecurity // Enables method-level security annotations like @PreAuthorize
public class SecurityConfig {

    private final MyUserDetailsService userDetailsService; // Custom user details service
    private final JwtRequestFilter jwtRequestFilter; // JWT filter for token validation

    /**
     * Constructor-based dependency injection.
     *
     * @param userDetailsService Service to load user details
     * @param jwtRequestFilter   Filter for processing JWT authentication
     */
    public SecurityConfig(MyUserDetailsService userDetailsService, JwtRequestFilter jwtRequestFilter) {
        this.userDetailsService = userDetailsService;
        this.jwtRequestFilter = jwtRequestFilter;
    }

    /**
     * Configures security settings, including:
     * - Disabling CSRF (since JWT is used)
     * - Setting session management to stateless (JWT-based authentication)
     * - Defining access control rules for different endpoints
     * - Adding the JWT authentication filter before UsernamePasswordAuthenticationFilter
     *
     * @param http The HttpSecurity object to configure security settings
     * @return SecurityFilterChain containing the security configuration
     * @throws Exception If any configuration error occurs*/

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
              //  .cors(cors -> cors.configurationSource(corsConfigurationSource())) // Configure CORS
                .csrf(csrf -> csrf.disable()) // Disable CSRF protection (not needed for JWT)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Use stateless session
                .authorizeHttpRequests(auth -> auth
                      // Permit access to registration and login endpoints without authentication
                      //.requestMatchers("users/register", "users/login").permitAll()
                       // Restrict PUT and DELETE requests on /users/** to users with "ROLE_USER" or "ROLE_ADMIN"
                        //.requestMatchers(HttpMethod.PUT, "/users/**").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")
                      // .requestMatchers(HttpMethod.DELETE, "/users/**").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                      // Require authentication for all other requests
                       .anyRequest().permitAll()

                )
                // Add the JWT authentication filter before the default authentication filter
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
                return http.build();
    }
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000")); // Allow requests from your frontend
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS")); // Allow these HTTP methods
        configuration.setAllowedHeaders(Arrays.asList("*")); // Allow all headers
        configuration.setAllowCredentials(true); // Allow credentials (e.g., cookies)
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // Apply this configuration to all endpoints
        return source;
    }

    /**
     * Defines an authentication provider using DAO authentication.
     * - Configures user details service for loading user data.
     * - Uses BCrypt for password encoding.
     *
     * @return Configured AuthenticationProvider
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService); // Set custom user details service
        authProvider.setPasswordEncoder(passwordEncoder()); // Set password encoder
        return authProvider;
    }

    /**
     * Provides an AuthenticationManager bean for authentication purposes.
     *
     * @param authenticationConfiguration Spring's authentication configuration
     * @return Configured AuthenticationManager
     * @throws Exception If authentication manager cannot be retrieved
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    /**
     * Provides a BCrypt password encoder with a strength of 12.
     * - Used to securely store passwords.
     *
     * @return PasswordEncoder instance
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }


}
