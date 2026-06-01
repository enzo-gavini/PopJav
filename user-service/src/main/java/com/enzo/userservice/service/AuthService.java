package com.enzo.userservice.service;

import com.enzo.userservice.entity.User;
import com.enzo.userservice.repository.UserRepository;
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
