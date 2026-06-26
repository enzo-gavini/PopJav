package com.enzo.authservice.service;

import com.enzo.authservice.dto.*;
import com.enzo.authservice.entity.Role;
import com.enzo.authservice.service.proxy.UserFeignClient;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthService {
    private final UserFeignClient userFeignClient;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthResponse register(RegisterRequest request) {
        if (userFeignClient.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        if (userFeignClient.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already exists");
        }

        if (request.getPassword().length() < 8 ||
                !request.getPassword().matches(".*[A-Z].*") ||
                !request.getPassword().matches(".*[a-z].*") ||
                !request.getPassword().matches(".*\\d.*") ||
                !request.getPassword().matches(".*[!@#$%^&*].*")) {
            throw new RuntimeException("Password must contain at least 8 characters, one uppercase, one lowercase, one digit and one special character");
        }

        if (!request.getEmail().matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) {
            throw new RuntimeException("Invalid email format");
        }

        String hashedPassWord = passwordEncoder.encode(request.getPassword());

        UserCreateRequest userCreate = new UserCreateRequest();
        userCreate.setUsername(request.getUsername());
        userCreate.setEmail(request.getEmail());
        userCreate.setPassword(hashedPassWord);
        userCreate.setRole(Role.USER);

        UserDTO savedUser = userFeignClient.save(userCreate);

        UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
                .username(savedUser.getEmail())
                .password(hashedPassWord)
                .roles(savedUser.getRole().name())
                .build();

        String token = jwtService.generateToken(userDetails, savedUser.getId());

        AuthResponse response = new AuthResponse();
        response.setToken(token);
        return  response;
    }

    public AuthResponse login(LoginRequest request) {
        UserFullDTO user = userFeignClient.getUserByEmail(request.getEmail());
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .roles(user.getRole().name())
                .build();

        String token = jwtService.generateToken(userDetails, user.getId());

        AuthResponse response = new AuthResponse();
        response.setToken(token);
        return  response;
    }


}
