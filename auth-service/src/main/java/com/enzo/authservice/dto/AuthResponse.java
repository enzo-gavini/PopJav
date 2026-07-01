package com.enzo.authservice.dto;

import com.enzo.authservice.entity.Role;
import lombok.Data;

@Data
public class AuthResponse {
    private String token;
    private Long userId;
    private String email;
    private Role role;
}
