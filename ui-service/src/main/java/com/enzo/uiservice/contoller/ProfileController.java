package com.enzo.uiservice.contoller;

import com.enzo.uiservice.dto.UserDTO;
import com.enzo.uiservice.proxy.UserFeignClient;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@AllArgsConstructor
public class ProfileController {
    private final UserFeignClient userFeignClient;

    @GetMapping("/profile")
    public String showProfile(HttpSession session, Model model) {
        String email = (String) session.getAttribute("email");
        UserDTO user = userFeignClient.getUserByEmail(email);
        model.addAttribute("user", user);
        return "profile";
    }
}