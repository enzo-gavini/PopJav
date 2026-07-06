package com.enzo.uiservice.controller;


import com.enzo.uiservice.dto.AuthResponseDTO;
import com.enzo.uiservice.dto.LoginRequestDTO;
import com.enzo.uiservice.dto.RegisterRequestDTO;
import com.enzo.uiservice.proxy.AuthFeignClient;
import com.enzo.uiservice.service.SessionService;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
/**
 * After login/register, the auth-service response is stored in the session
 * via SessionService. Logout invalidates the session.
 */
@Controller
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {
    private final AuthFeignClient authFeignClient;
    private final SessionService sessionService;

    @GetMapping("/register")
    public String showRegisterPage(Model model) {
        model.addAttribute("registerRequest", new RegisterRequestDTO());
        return "register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute RegisterRequestDTO request, HttpSession session) {
        AuthResponseDTO reponse = authFeignClient.register(request);
        sessionService.storeSession(session, reponse);
        return "redirect:/";
    }

    @GetMapping("/login")
    public String showLoginPage (Model model) {
        model.addAttribute("loginRequest", new LoginRequestDTO());
        return "login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute LoginRequestDTO request, HttpSession session) {
        AuthResponseDTO response = authFeignClient.login(request);
        sessionService.storeSession(session, response);
        return "redirect:/";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

}
