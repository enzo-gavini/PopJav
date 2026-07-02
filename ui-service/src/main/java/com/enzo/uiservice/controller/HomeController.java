package com.enzo.uiservice.controller;

import com.enzo.uiservice.proxy.ChapterFeignClient;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Collections;

@Controller
@AllArgsConstructor
public class HomeController {
    private final ChapterFeignClient chapterFeignClient;

    @GetMapping("/")
    public String home(Model model) {
        // Public catalog: the chapter list (metadata only) is shown to everyone,
        // logged in or not. Full content stays behind authentication.
        try {
            model.addAttribute("chapters", chapterFeignClient.getChapterSummaries());
        } catch (Exception e) {
            model.addAttribute("chapters", Collections.emptyList());
        }
        return "home";
    }
}
