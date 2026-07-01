package com.enzo.uiservice.controller;

import com.enzo.uiservice.dto.ChapterDTO;
import com.enzo.uiservice.proxy.ChapterFeignClient;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/chapters")
@AllArgsConstructor
public class ChapterController {
    private final ChapterFeignClient chapterFeignClient;

    @GetMapping
    public String showChapters(Model model) {
        List<ChapterDTO> chapters = chapterFeignClient.getAllChapter();
        model.addAttribute("chapters", chapters);
        return "chapters";
    }

    @GetMapping("/{id}")
    public String showChapterById(Model model, @PathVariable Long id) {
        model.addAttribute("chapterDTO", chapterFeignClient.getChapterById(id));
        return "chapter-detail";
    }

    @GetMapping("/create")
    public String showCreatePage(Model model) {
        model.addAttribute("chapterDTO", new ChapterDTO());
        return "chapter-create";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute ("chapterDTO") ChapterDTO chapter) {
        chapterFeignClient.save(chapter);
        return "redirect:/chapters";
    }

    @GetMapping("/edit/{id}")
    public String showEditPage(@PathVariable Long id, Model model) {
        model.addAttribute("chapterDTO", chapterFeignClient.getChapterById(id));
        return "chapter-edit";
    }

    @PostMapping("/edit/{id}")
    public String updateChapter(@PathVariable Long id, @ModelAttribute("chapterDTO") ChapterDTO chapter) {
        chapterFeignClient.updateChapter(id, chapter);
        return "redirect:/chapters";
    }

    @GetMapping("/delete/{id}")
    public String deleteChapter(@PathVariable Long id) {
        chapterFeignClient.deleteChapter(id);
        return "redirect:/chapters";
    }
}
