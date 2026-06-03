package com.enzo.persistenceservice.controller;

import com.enzo.persistenceservice.entity.Lesson;
import com.enzo.persistenceservice.service.LessonService;
import com.enzo.persistenceservice.service.dto.LessonCreateDTO;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/lessons")
@AllArgsConstructor
public class LessonController {
    private final LessonService lessonService;

    @PostMapping
    public Lesson save(@RequestBody LessonCreateDTO dto) {
        return lessonService.create(dto);
    }

    @GetMapping
    public List<Lesson> getAllLesson() {
        return lessonService.findAll();
    }

    @GetMapping("/{id}")
    public Lesson getLessonById(@PathVariable Long id) {
        return lessonService.findById(id);
    }

    @PutMapping("/{id}")
    public Lesson updateLesson(@PathVariable Long id, @RequestBody Lesson lesson) {
        return lessonService.updateLesson(lesson);
    }

    @DeleteMapping("/{id}")
    public void deleteLesson(@PathVariable Long id) {
        lessonService.delete(id);
    }
}
