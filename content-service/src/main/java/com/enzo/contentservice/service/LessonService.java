package com.enzo.contentservice.service;

import com.enzo.contentservice.dto.LessonDTO;
import com.enzo.contentservice.service.proxy.LessonFeignClient;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
/**
 * CRUD logic for lessons: a Feign facade to persistence-service.
 */
@Service
@AllArgsConstructor
public class LessonService {
    private final LessonFeignClient lessonFeignClient;

    public LessonDTO create(LessonDTO lessonDTO) {
        return lessonFeignClient.save(lessonDTO);
    }

    public List<LessonDTO> findAll() {
        return lessonFeignClient.getAllLesson();
    }

    public LessonDTO findById(Long id) {
        return lessonFeignClient.getLessonById(id);
    }

    public LessonDTO updateLesson(Long id, LessonDTO lessonDTO) {
        return lessonFeignClient.updateLesson(id, lessonDTO);
    }

    public void delete(Long id) {lessonFeignClient.deleteLesson(id);}
}
