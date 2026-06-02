package com.enzo.authservice.dto;

import com.enzo.authservice.entity.Role;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserFullDTO {
    private Long id;
    private String username;
    private String email;
    private String password;
    private Role role;
    private LocalDateTime createdAt;
}