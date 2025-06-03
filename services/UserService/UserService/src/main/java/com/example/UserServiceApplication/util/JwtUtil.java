package com.example.UserServiceApplication.util;

import com.example.UserServiceApplication.model.MyUserDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Utility class for handling JWT (JSON Web Token) generation, validation, and extraction.
 */
@Component
public class JwtUtil {

    // Securely generate a secret key for signing JWTs using HS256 algorithm
    private static final Key SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    //private final SecretKey secretKey;

    // Token expiration time (1 hour in milliseconds)
    private static final long EXPIRATION_TIME = 1000 * 60 * 60;


//    public JwtUtil(@Value("${security.jwt.secret-key}") String secret) {
//        this.secretKey = Keys.hmacShaKeyFor(Base64.getDecoder().decode(secret));
//    }
    /**
     * Generates a JWT token for the given user.
     *
     * @param userDetails The user details object containing email and roles.
     * @return A signed JWT token as a String.
     */
    public String generateToken(MyUserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", userDetails.getAuthorities()
                .stream().map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList())); // Store roles in claims
        claims.put("userid",userDetails.getId());
        return createToken(claims, userDetails.getEmail());
    }

    /**
     * Creates a JWT token with the given claims and subject (email).
     *
     * @param claims  Additional claims to include in the token.
     * @param subject The email of the user.
     * @return A signed JWT token.
     */
    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject) // Set email as subject
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .setIssuer("http://USER-SERVICE")
                .signWith(SECRET_KEY) // Sign with secret key
                .compact();
    }

    /**
     * Validates the provided JWT token against the given user details.
     *
     * @param token       The JWT token to validate.
     * @param userDetails The user details to compare with.
     * @return True if the token is valid, false otherwise.
     */
    public boolean validateToken(String token, MyUserDetails userDetails) {
        final String email = extractEmail(token);
        final List<String> tokenRoles = extractRoles(token);
        List<String> userRoles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        return (email.equals(userDetails.getEmail())
                && !isTokenExpired(token)
                && tokenRoles.containsAll(userRoles)); // Ensure roles match
    }

    /**
     * Extracts the email (subject) from the given JWT token.
     *
     * @param token The JWT token.
     * @return The email contained in the token.
     */
    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extracts the roles from the given JWT token.
     *
     * @param token The JWT token.
     * @return A list of roles stored in the token.
     */
    public List<String> extractRoles(String token) {
        final Claims claims = extractAllClaims(token);
        return claims.get("roles", List.class); // Extract roles as a list
    }

    public Long extractUserId(String token) {
        final Claims claims = extractAllClaims(token);
        return claims.get("userid", Long.class); // Extract userid
    }

    /**
     * Extracts the expiration date from the given JWT token.
     *
     * @param token The JWT token.
     * @return The expiration date of the token.
     */
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Extracts a specific claim from the JWT token using a resolver function.
     *
     * @param token         The JWT token.
     * @param claimsResolver A function to extract a specific claim.
     * @param <T>           The type of the claim.
     * @return The extracted claim.
     */
    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Extracts all claims from the JWT token.
     *
     * @param token The JWT token.
     * @return The claims contained in the token.
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Checks if the JWT token has expired.
     *
     * @param token The JWT token.
     * @return True if the token is expired, false otherwise.
     */
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }
}
