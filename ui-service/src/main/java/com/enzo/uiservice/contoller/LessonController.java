package com.enzo.uiservice.contoller;

import com.enzo.uiservice.dto.LessonDTO;
import com.enzo.uiservice.proxy.LessonFeignClient;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/lessons")
@AllArgsConstructor
public class LessonController {
    private final LessonFeignClient lessonFeignClient;

    @GetMapping
    public String showLesson(Model model) {
        List<LessonDTO> lessons = lessonFeignClient.getAllLesson();
        model.addAttribute("lessons", lessons);
        return "lessons";
    }

    @GetMapping("/{id}")
    public String showLessonById(Model model, @PathVariable Long id) {
        model.addAttribute("lessonDTO", lessonFeignClient.getLessonById(id));
        return "lesson-detail";
    }

    @GetMapping("/create")
    public String showCreatePage(Model model) {
        model.addAttribute("lessonDTO", new LessonDTO());
        return "lesson-create";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute("lessonDTO") LessonDTO lesson) {
        lessonFeignClient.save(lesson);
        return "redirect:/lessons";
    }

    @GetMapping("/edit/{id}")
    public String showEditPage(@PathVariable Long id, Model model) {
        model.addAttribute("lessonDTO", lessonFeignClient.getLessonById(id));
        return "lesson-edit";
    }

    @PostMapping("/edit/{id}")
    public String updateLesson(@PathVariable Long id, @ModelAttribute("lessonDTO") LessonDTO lesson) {
        lessonFeignClient.updateLesson(id, lesson);
        return "redirect:/lessons";
    }

    @GetMapping("/delete/{id}")
    public String deleteLesson(@PathVariable Long id) {
        lessonFeignClient.deleteLesson(id);
        return "redirect:/lessons";
    }
}
