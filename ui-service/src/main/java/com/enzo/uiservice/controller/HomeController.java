package com.enzo.uiservice.controller;

import com.enzo.uiservice.proxy.ChapterFeignClient;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@AllArgsConstructor
public class HomeController {
    private final ChapterFeignClient chapterFeignClient;

    @GetMapping("/")
    public String home(HttpSession session, Model model) {
        if (session.getAttribute("token") != null) {
            model.addAttribute("chapters", chapterFeignClient.getAllChapter());
        }
        return "home";
    }
}