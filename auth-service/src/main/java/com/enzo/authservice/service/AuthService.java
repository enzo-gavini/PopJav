package com.enzo.authservice.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthService {
    private final UserRepository userRepository;

    public User register(User user) {
        return userRepository.save(user);
    }

}
