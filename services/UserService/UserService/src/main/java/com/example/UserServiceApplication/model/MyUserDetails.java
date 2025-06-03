package com.example.UserServiceApplication.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

/**
 * Custom implementation of Spring Security's UserDetails.
 * - Wraps the `Users` entity and provides authentication-related details.
 */
public class MyUserDetails implements UserDetails {

    private final Users user; // Stores user entity

    /**
     * Constructor to initialize MyUserDetails with a user entity.
     *
     * @param user Users entity from the database.
     */
    public MyUserDetails(Users user) {
        this.user = user;
    }

    /**
     * Returns the authorities granted to the user.
     * - Currently assigns a default "USER" role.
     * - Can be enhanced to fetch roles dynamically.
     *
     * @return A collection of granted authorities.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority("ROLE_"+user.getRole())); // Assign "USER" role
    }

    /**
     * Retrieves the user ID.
     *
     * @return User ID.
     */
    public Long getId() {
        return user.getId();
    }
    public String getEmail() {
        return user.getEmail();
    }

    /**
     * Retrieves the user entity.
     *
     * @return User entity.
     */
    public Users getUser() {
        return user;
    }

    /**
     * Returns the user's password.
     *
     * @return Encrypted password.
     */
    @Override
    public String getPassword() {
        return user.getPassword();
    }

    /**
     * Returns the user's username.
     *
     * @return Username.
     */
    @Override
    public String getUsername() {
        return user.getUsername();
    }



    /**
     * Checks if the user's account is non-expired.
     *
     * @return true (default behavior).
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Checks if the user's account is non-locked.
     *
     * @return true (default behavior).
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Checks if the user's credentials (password) are non-expired.
     *
     * @return true (default behavior).
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Checks if the user is enabled.
     *
     * @return true (default behavior).
     */
    @Override
    public boolean isEnabled() {
        return true;
    }
}
