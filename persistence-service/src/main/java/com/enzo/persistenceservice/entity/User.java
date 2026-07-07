package com.enzo.persistenceservice.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * User account stored in the users table: unique username/email and the
 * BCrypt password hash.
 */
@Entity
@Data
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Username cannot be blank")
    @Size(min = 3, max = 20)
    @Column(unique = true)
    private String username;

    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Invalid email format")
    // Uniqueness is enforced by the database itself: two accounts can never share
    // the same email, even if the application check is bypassed (the username
    // column above has the same guarantee).
    @Column(unique = true)
    private String email;

    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;
    // JPA lifecycle hook: sets the creation date just before the INSERT, so the
    // timestamp is always set by the server and never comes from a client.
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
    @Column(nullable = false)
    private LocalDateTime createdAt;
}