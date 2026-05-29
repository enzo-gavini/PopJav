package com.enzo.persistenceservice.service;

import com.enzo.persistenceservice.entity.Lesson;
import com.enzo.persistenceservice.repository.LessonRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class LessonService {
    private final LessonRepository lessonRepository;

    public Lesson create(Lesson lesson) {
        return lessonRepository.save(lesson);
    }

    public List<Lesson> findAll() {
        return lessonRepository.findAll();
    }

    public Lesson findById(Long id) {
        return lessonRepository.findById(id).orElseThrow(() -> new RuntimeException("Lesson not found"));
    }

    public Lesson updateLesson(Lesson lesson) {
        return lessonRepository.save(lesson);
    }

    public void delete(Long id) {lessonRepository.deleteById(id);}
}
