package com.enzo.persistenceservice.controller;

import com.enzo.persistenceservice.entity.User;
import com.enzo.persistenceservice.service.UserService;
import com.enzo.persistenceservice.service.dto.UserResponseDTO;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public UserResponseDTO getUserByEmail(@RequestParam String email) {
        return toResponse(userService.findByEmail(email));
    }

    /**
     * Internal endpoint returning the full user (including the password hash) so
     * auth-service can verify credentials. auth-service calls persistence directly
     * via service discovery; the API gateway restricts this path to ADMIN.
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
