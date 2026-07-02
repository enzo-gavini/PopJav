package com.enzo.uiservice.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping("/about")
    public String about() {
        return "about";
    }

    @GetMapping("/contact")
    public String contact() {
        return "contact";
    }

    @GetMapping("/legal")
    public String legal() {
        return "legal";
    }

    @GetMapping("/privacy")
    public String privacy() {
        return "privacy";
    }
}
