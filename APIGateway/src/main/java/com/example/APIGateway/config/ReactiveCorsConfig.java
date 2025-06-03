package com.example.APIGateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.server.adapter.WebHttpHandlerBuilder;

import java.util.Arrays;

@Configuration
public class ReactiveCorsConfig {

    @Bean
    public CorsWebFilter corsWebFilter() {
        // Create a CorsConfigurationSource
        CorsConfigurationSource source = corsConfigurationSource();
        // Create and return a CorsWebFilter with the configuration source
        return new CorsWebFilter(source);
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        // Create a CorsConfiguration object
        CorsConfiguration configuration = new CorsConfiguration();
        // Configure CORS settings
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000")); // Allow requests from your frontend
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS")); // Allow these HTTP methods
        configuration.setAllowedHeaders(Arrays.asList("*")); // Allow all headers
        configuration.setAllowCredentials(true); // Allow credentials (e.g., cookies)
        // Create a UrlBasedCorsConfigurationSource and register the CorsConfiguration
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // Apply to all paths
        return source;
    }
}