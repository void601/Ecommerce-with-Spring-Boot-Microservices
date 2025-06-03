package com.example.APIGateway.util;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class JwtValidationFilter implements WebFilter { // Implement WebFilter

    private final WebClient webClient;

    public JwtValidationFilter(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://localhost:8081").build();
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        System.out.println("Inside JwtValidationFilter"); // Debugging statement
        ServerHttpRequest request = exchange.getRequest();
        String authorizationHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        // Skip authentication for public endpoints
        String requestURI = request.getURI().getPath();
        System.out.println(requestURI);
        if (requestURI.equals("/users/login") || requestURI.equals("/users/register") || requestURI.equals("/users/auth/validate-token")) {
            System.out.println("Skipping JWT validation for public endpoint: " + requestURI); // Debugging statement
            return chain.filter(exchange); // Skip JWT validation
        }

        // Check if the Authorization header is present and formatted correctly
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            System.out.println("Missing or invalid Authorization header"); // Debugging statement
            return onError(exchange, "Missing or invalid Authorization header", HttpStatus.UNAUTHORIZED);
        }

        String jwt = authorizationHeader.substring(7); // Extract the token
        System.out.println("Validating JWT token: " + jwt); // Debugging statement

        // Call User Service to validate the token
        return webClient.post()
                .uri("/users/auth/validate-token") // User Service endpoint
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
                .retrieve()
                .bodyToMono(Map.class) // Expect a HashMap response
                .flatMap(response -> {
                    boolean isValid = (Boolean) response.get("isValid");
                    List<String> roles = (List<String>) response.get("roles");
                    String email = (String) response.get("email"); // Extract email from the response

                    if (isValid) {
                        System.out.println("Token is valid for user: " + email); // Debugging statement
                        // Token is valid, create an Authentication object
                        List<SimpleGrantedAuthority> authorities = roles.stream()
                                .map(SimpleGrantedAuthority::new)
                                .collect(Collectors.toList());
                        System.out.println(authorities);
                        Authentication authentication = new UsernamePasswordAuthenticationToken(
                                email, jwt, authorities // Use email as the principal
                        );
                        System.out.println(authentication.getAuthorities());
                        // Set the authentication in the SecurityContext
                        SecurityContext securityContext = new SecurityContextImpl(authentication);
                        return chain.filter(exchange)
                                .contextWrite(ReactiveSecurityContextHolder.withSecurityContext(Mono.just(securityContext)));
                    } else {
                        System.out.println("Invalid JWT token"); // Debugging statement
                        return onError(exchange, "Invalid JWT token", HttpStatus.UNAUTHORIZED);
                    }
                })
                .onErrorResume(e -> {
                    System.out.println("Token validation failed: " + e.getMessage()); // Debugging statement
                    return onError(exchange, "Token validation failed", HttpStatus.INTERNAL_SERVER_ERROR);
                });
    }

    private Mono<Void> onError(ServerWebExchange exchange, String error, HttpStatus status) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(status);
        return response.setComplete();
    }
}