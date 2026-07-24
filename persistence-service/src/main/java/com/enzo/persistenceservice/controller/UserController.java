package com.enzo.persistenceservice.controller;

import com.enzo.persistenceservice.entity.User;
import com.enzo.persistenceservice.service.UserService;
import com.enzo.persistenceservice.service.dto.UserResponseDTO;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * REST CRUD endpoints over the users table. Every response is mapped to
 * UserResponseDTO; only /credentials returns the raw entity.
 */
@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    public UserResponseDTO save(@RequestBody User user) {
        return toResponse(userService.save(user));
    }

    @GetMapping
    public List<UserResponseDTO> getAllUser() {
        return userService.findAll().stream().map(this::toResponse).toList();
    }

    @GetMapping("/{id}")
    public UserResponseDTO getUserById(@PathVariable Long id) {
        return toResponse(userService.findById(id));
    }

    @PutMapping("/{id}")
    public UserResponseDTO updateUser(@PathVariable Long id, @RequestBody User user) {
        return toResponse(userService.updateProfile(user));
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
         userService.delete(id);
    }

    @GetMapping("/search")
    public UserResponseDTO getUserByEmail(@RequestParam String email,
                                          @RequestHeader(value = "X-User-Id", required = false) Long callerId,
                                          @RequestHeader(value = "X-User-Role", required = false) String callerRole) {
        User user = userService.findByEmail(email);
        // A user can only look up their own account; an admin can look up anyone.
        // Without this check, any logged-in account could read every email and role.
        if (!user.getId().equals(callerId) && !"ADMIN".equals(callerRole)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied");
        }
        return toResponse(user);
    }

    /**
     * Internal endpoint that returns the full user (with the password hash) so
     * auth-service can verify credentials. auth-service calls persistence directly
     * via service discovery; the API gateway restricts this path to ADMIN only.
     */
    @GetMapping("/credentials")
    public User getCredentialsByEmail(@RequestParam String email) {
        return userService.findByEmail(email);
    }

    @GetMapping("/exists/email")
    public boolean existsByEmail(@RequestParam String email) {
        return userService.existsByEmail(email);
    }

    @GetMapping("/exists/username")
    public boolean existsByUsername(@RequestParam String username) {
        return userService.existsByUsername(username);
    }
    // All the responses go through UserResponseDTO: the password hash can never
    // leak since the DTO has no password field. The only deliberate exception
    // is /credentials.
    private UserResponseDTO toResponse(User user) {
        return new UserResponseDTO(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole(),
                user.getCreatedAt()
        );
    }
}
