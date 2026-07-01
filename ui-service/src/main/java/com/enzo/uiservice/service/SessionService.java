package com.enzo.uiservice.service;

import com.enzo.uiservice.dto.AuthResponseDTO;
import com.enzo.uiservice.entity.Role;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

@Service
public class SessionService {

    /**
     * Stores the session identity from the auth-service response.
     * The JWT stays opaque to ui-service: it is only kept to be forwarded as a
     * Bearer token on outgoing calls. The identity (userId, email, role) comes
     * from the typed response emitted by auth-service, the identity authority.
     */
    public void storeSession(HttpSession session, AuthResponseDTO response) {
        session.setAttribute("token", response.getToken());
        session.setAttribute("userId", response.getUserId());
        session.setAttribute("email", response.getEmail());
        Role role = response.getRole() != null ? response.getRole() : Role.USER;
        session.setAttribute("role", role.name());
    }
}
