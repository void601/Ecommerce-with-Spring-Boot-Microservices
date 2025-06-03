package com.example.APIGateway.config;

import com.example.APIGateway.util.JwtValidationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import reactor.core.publisher.Mono;

import java.util.Arrays;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Autowired
    private JwtValidationFilter jwtValidationFilter; // Inject your custom filter

    @Autowired
    ReactiveCorsConfig reactiveCorsConfig;
    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        http
                .cors(cors -> cors.configurationSource(reactiveCorsConfig.corsConfigurationSource())) // Enable CORS
                .authorizeExchange(exchanges -> exchanges
                        // Public endpoints
                        .pathMatchers("/users/register", "/users/login").permitAll()

                        // Restrict PUT and DELETE requests on /users/** to users with "ROLE_USER" or "ROLE_ADMIN"
                        .pathMatchers("/users/**").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")
                        .pathMatchers("/products/**").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")
                        .pathMatchers("/cart/**").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")
                        .pathMatchers("/orders/**").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")
                        .pathMatchers("/inventory/**").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")
                        // All other endpoints require authentication
                        .anyExchange().authenticated()
                )
                .addFilterAt(jwtValidationFilter, SecurityWebFiltersOrder.AUTHENTICATION) // Add custom filter
                .csrf(csrf -> csrf.disable()) // Disable CSRF for stateless APIs
                .httpBasic(httpBasic -> httpBasic.disable()); // Disable HTTP Basic Authentication

        return http.build();
    }

    @Bean
    public ReactiveUserDetailsService reactiveUserDetailsService() {
        return username -> Mono.empty(); // Return an empty Mono (no user details)
    }

}