package com.example.UserServiceApplication.ProductMicroservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/public/**").permitAll() // Public endpoints
                        .requestMatchers("/secure/**").hasRole("USER") // Requires ROLE_USER
                        .anyRequest().authenticated() // Any other request must be authenticated
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter())) // Use custom converter
                );

        return http.build();
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(jwt -> {
            JwtGrantedAuthoritiesConverter defaultConverter = new JwtGrantedAuthoritiesConverter();
            Collection<GrantedAuthority> defaultAuthorities = defaultConverter.convert(jwt);
            // Extract roles from the "realm_access" claim
            Map<String, Object> realmAccess = jwt.getClaimAsMap("realm_access");
            List<SimpleGrantedAuthority> keycloakRoles = realmAccess != null && realmAccess.get("roles") instanceof List
                    ? ((List<String>) realmAccess.get("roles")).stream()
                    .map(role -> new SimpleGrantedAuthority("ROLE_" + role.toUpperCase())) // Convert to ROLE_ format
                    .collect(Collectors.toList())
                    : List.of();
            System.out.println(defaultAuthorities.stream().collect(Collectors.toList()).toString());
            System.out.println(keycloakRoles.stream().collect(Collectors.toList()).toString());
            return Stream.concat(defaultAuthorities.stream(), keycloakRoles.stream()).collect(Collectors.toList());
        });
        return converter;
    }
}
