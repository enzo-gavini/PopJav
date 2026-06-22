package com.enzo.uiservice.config;

import feign.FeignException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(FeignException.Unauthorized.class)
    public String handleUnauthorized() {
        return "redirect:/auth/login";
    }

    @ExceptionHandler(FeignException.class)
    public String handleFeignError(Model model) {
        model.addAttribute("message", "Service indisponible");
        return "error";
    }

    @ExceptionHandler(Exception.class)
    public String handleGenericError(Model model) {
        model.addAttribute("message", "Une erreur est survenue");
        return "error";
    }
}
