package com.enzo.uiservice.dto;

import com.enzo.uiservice.entity.Role;
import lombok.Data;

@Data
public class AuthResponseDTO {
    private String token;
    private Long userId;
    private String email;
    private Role role;
}
