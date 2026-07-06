package com.enzo.authservice.service;

import com.enzo.authservice.dto.*;
import com.enzo.authservice.entity.Role;
import com.enzo.authservice.exception.EmailAlreadyExistsException;
import com.enzo.authservice.exception.InvalidCredentialsException;
import com.enzo.authservice.exception.InvalidRequestException;
import com.enzo.authservice.exception.UsernameAlreadyExistsException;
import com.enzo.authservice.service.proxy.UserFeignClient;
import feign.FeignException;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
/**
 * Service for login and registration. It does not own user data: persistence
 * is relayed to persistence-service via Feign. Issues the JWT on success.
 */
@Service
@AllArgsConstructor
public class AuthService {
    private final UserFeignClient userFeignClient;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthResponse register(RegisterRequest request) {
        if (userFeignClient.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException("Email already exists");
        }

        if (userFeignClient.existsByUsername(request.getUsername())) {
            throw new UsernameAlreadyExistsException("Username already exists");
        }

        // Validation by server because validation from the front can be bypassed by calling the API directly
        if (request.getPassword().length() < 8 ||
                !request.getPassword().matches(".*[A-Z].*") ||
                !request.getPassword().matches(".*[a-z].*") ||
                !request.getPassword().matches(".*\\d.*") ||
                !request.getPassword().matches(".*[!@#$%^&*].*")) {
            throw new InvalidRequestException("Password must contain at least 8 characters, one uppercase, one lowercase, one digit and one special character");
        }

        if (!request.getEmail().matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) {
            throw new InvalidRequestException("Invalid email format");
        }
        // Passwords are hashed here before being sent to user-service, so persistence-service will only see the BCrypt hash
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
        response.setUserId(savedUser.getId());
        response.setEmail(savedUser.getEmail());
        response.setRole(savedUser.getRole());
        return  response;
    }

    public AuthResponse login(LoginRequest request) {
        UserFullDTO user;
        try {
            user = userFeignClient.getUserByEmail(request.getEmail());
        } catch (FeignException e) {
            // Unknown email and wrong password will return the same error for security, so the response doesn't reveal whether a user exists
            throw new InvalidCredentialsException("Invalid credentials");
        }
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Invalid credentials");
        }

        UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .roles(user.getRole().name())
                .build();

        String token = jwtService.generateToken(userDetails, user.getId());

        AuthResponse response = new AuthResponse();
        response.setToken(token);
        response.setUserId(user.getId());
        response.setEmail(user.getEmail());
        response.setRole(user.getRole());
        return  response;
    }


}
