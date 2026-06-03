package com.enzo.persistenceservice.service;

import com.enzo.persistenceservice.entity.Chapter;
import com.enzo.persistenceservice.entity.Lesson;
import com.enzo.persistenceservice.repository.ChapterRepository;
import com.enzo.persistenceservice.repository.LessonRepository;
import com.enzo.persistenceservice.service.dto.LessonCreateDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class LessonService {
    private final LessonRepository lessonRepository;
    private final ChapterRepository chapterRepository;

    public Lesson create(LessonCreateDTO dto) {
        Chapter chapter = chapterRepository.findById(dto.getChapterId())
                .orElseThrow(()-> new RuntimeException("Chapter not found"));

        Lesson lesson = new Lesson();
        lesson.setTitle(dto.getTitle());
        lesson.setContent(dto.getContent());
        lesson.setOrderIndex(dto.getOrderIndex());
        lesson.setChapter(chapter);
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
