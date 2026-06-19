package com.enzo.uiservice.service;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

@Service
public class SessionService {

    public void storeTokenAndRole(HttpSession session, String token) {
        session.setAttribute("token", token);
        String[] parts = token.split("\\.");
        String payload = new String(java.util.Base64.getUrlDecoder().decode(parts[1]));
        if (payload.contains("ADMIN")) {
            session.setAttribute("role", "ADMIN");
        } else {
            session.setAttribute("role", "USER");
        }
    }
}