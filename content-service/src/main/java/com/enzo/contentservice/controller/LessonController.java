package com.enzo.contentservice.controller;

import com.enzo.contentservice.dto.LessonDTO;
import com.enzo.contentservice.service.LessonService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 *  REST CRUD endpoints for lesson, relayed to persistence-service.
 */
@RestController
@RequestMapping("/api/lessons")
@AllArgsConstructor
public class LessonController {
    private final LessonService lessonService;

    @PostMapping
    public LessonDTO save(@RequestBody LessonDTO lessonDTO) {
        return lessonService.create(lessonDTO);
    }

    @GetMapping
    public List<LessonDTO> getAllLesson() {
        return lessonService.findAll();
    }

    @GetMapping("/{id}")
    public LessonDTO getLessonById(@PathVariable Long id) {
        return lessonService.findById(id);
    }

    @PutMapping("/{id}")
    public LessonDTO updateLesson(@PathVariable Long id, @RequestBody LessonDTO lessonDTO) {
        return lessonService.updateLesson(id, lessonDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteLesson(@PathVariable Long id) {lessonService.delete(id);}
}
