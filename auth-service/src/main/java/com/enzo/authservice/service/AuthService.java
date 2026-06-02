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

        String token = jwtService.generateToken(userDetails);

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

        String token = jwtService.generateToken(userDetails);

        AuthResponse response = new AuthResponse();
        response.setToken(token);
        return  response;
    }


}
