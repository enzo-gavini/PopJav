package com.enzo.uiservice.service;

import com.enzo.uiservice.dto.AuthResponseDTO;
import com.enzo.uiservice.entity.Role;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

@Service
public class SessionService {

    /**
     * Stores the session identity from the auth-service response.
     * The JWT stays hidden: ui-service never looks inside it and only keeps it
     * as a Bearer token for the outgoing calls. The identity (userId, email,
     * role) comes from the typed response emitted by auth-service.
     */
    public void storeSession(HttpSession session, AuthResponseDTO response) {
        session.setAttribute("token", response.getToken());
        session.setAttribute("userId", response.getUserId());
        session.setAttribute("email", response.getEmail());
        Role role = response.getRole() != null ? response.getRole() : Role.USER;
        session.setAttribute("role", role.name());
    }
}
