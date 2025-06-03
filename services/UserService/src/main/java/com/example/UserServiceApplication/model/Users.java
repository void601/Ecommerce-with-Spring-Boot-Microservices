package com.example.UserServiceApplication.model;

// Import JPA annotations for entity mapping
import jakarta.persistence.*;

/**
 * Entity class representing the Users table in the database.
 * - Stores user details including username, email, password, and role.
 */
@Entity // Marks this class as a JPA entity
@Table(name="users") // Maps this entity to the "users" table in the database
public class Users {

    @Id // Marks this field as the primary key
    @GeneratedValue(strategy=GenerationType.IDENTITY) // Auto-increments ID using database strategy
    private Long id;

    @Column(nullable = false, unique = true) // Ensures username is unique and not null
    private String username;

    @Column(nullable = false, unique = true) // Ensures email is unique and not null
    private String email;

    @Column(nullable = false)// Ensures password is not null
    private String password;

    @Column(nullable = false) // Ensures role is not null (e.g., "ROLE_USER", "ROLE_ADMIN")
    private String role;

    /**
     * Default constructor (required by JPA).
     */
    public Users() {
    }

    /**
     * Parameterized constructor to initialize all fields.
     *
     * @param id       User ID (auto-generated).
     * @param username Unique username.
     * @param email    Unique email address.
     * @param password Encrypted password.
     * @param role     User role (e.g., "ROLE_USER", "ROLE_ADMIN").
     */
    public Users(Long id, String username, String email, String password, String role) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    /**
     * Getter for user ID.
     *
     * @return User ID.
     */
    public Long getId() {
        return id;
    }

    /**
     * Setter for user ID.
     *
     * @param id New user ID.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Getter for username.
     *
     * @return Username.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Setter for username.
     *
     * @param username New username.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Getter for email.
     *
     * @return User email.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Setter for email.
     *
     * @param email New email.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Getter for password.
     *
     * @return Encrypted password.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Setter for password.
     *
     * @param password New encrypted password.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Getter for role.
     *
     * @return User role (e.g., "ROLE_USER", "ROLE_ADMIN").
     */
    public String getRole() {
        return role;
    }

    /**
     * Setter for role.
     *
     * @param role New role.
     */
    public void setRole(String role) {
        this.role = role;
    }
}
