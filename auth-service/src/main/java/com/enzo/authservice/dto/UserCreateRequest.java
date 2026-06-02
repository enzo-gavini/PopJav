package com.enzo.authservice.dto;

import com.enzo.authservice.entity.Role;
import lombok.Data;

@Data
public class UserCreateRequest {
    private String username;
    private String email;
    private String password;
    private Role role;
}