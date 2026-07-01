package com.enzo.uiservice.config;

import feign.FeignException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(FeignException.Unauthorized.class)
    public String handleUnauthorized(FeignException.Unauthorized e, Model model) {
        // A 401 on the login call means wrong credentials: show the message.
        if (e.request() != null && e.request().url().contains("/auth/login")) {
            model.addAttribute("message", "Email ou mot de passe incorrect.");
            return "error";
        }
        // Otherwise the session token is missing/expired: send the user to login.
        return "redirect:/auth/login";
    }


    @ExceptionHandler(FeignException.class)
    public String handleFeignError(FeignException e, Model model) {
        String message = e.contentUTF8();
        if (message.contains("Password must")) {
            model.addAttribute("message", "Le mot de passe doit contenir au moins 8 caractères, une majuscule, une minuscule, un chiffre et un caractère spécial.");
        } else if (message.contains("Email already")) {
            model.addAttribute("message", "Cet email est déjà utilisé.");
        } else if (message.contains("Username already")) {
            model.addAttribute("message", "Ce nom d'utilisateur est déjà pris.");
        } else if (message.contains("Invalid email")) {
            model.addAttribute("message", "Format d'email invalide.");
        } else {
            model.addAttribute("message", "Service indisponible");
        }
        return "error";
    }

    @ExceptionHandler(Exception.class)
    public String handleGenericError(Model model) {
        model.addAttribute("message", "Une erreur est survenue");
        return "error";
    }
}