package com.enzo.uiservice.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
/**
 * Re-attaches the session JWT as a Bearer token on every outgoing Feign call,
 * otherwise the gateway would answer 401. The browser never sees the JWT:
 * it only knows the session cookie.
 */
@Component
public class FeignInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate template) {
        ServletRequestAttributes attributes = (ServletRequestAttributes)
                RequestContextHolder.getRequestAttributes();

        if (attributes != null) {
            // getSession(false): do not create a session for an anonymous visitor
            HttpSession session = attributes.getRequest().getSession(false);

            if (session != null) {
                String token = (String) session.getAttribute("token");

                if (token != null) {
                    template.header("Authorization", "Bearer " + token);
                }
            }
        }
    }
}
