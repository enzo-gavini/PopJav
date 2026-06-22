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

        int start = payload.indexOf("\"userId\":") + 9;
        int end = payload.indexOf(",", start);
        if (end == -1) end = payload.indexOf("}", start);

        session.setAttribute("userId", Long.parseLong(payload.substring(start, end)));

        int emailStart = payload.indexOf("\"sub\":\"") + 6;
        int emailEnd = payload.indexOf("\"", emailStart);
        session.setAttribute("email", payload.substring(emailStart, emailEnd));
    }
}