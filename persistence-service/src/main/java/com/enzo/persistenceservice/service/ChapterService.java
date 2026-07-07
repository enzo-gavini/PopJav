package com.enzo.persistenceservice.service;
import com.enzo.persistenceservice.exception.ResourceNotFoundException;

import com.enzo.persistenceservice.entity.Chapter;
import com.enzo.persistenceservice.repository.ChapterRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *  CRUD logic for chapter
 */
@Service
@AllArgsConstructor
public class ChapterService {
    private final  ChapterRepository chapterRepository;

    public Chapter create(Chapter chapter) {
        return chapterRepository.save(chapter);
    }

    public List<Chapter> findAll() {
        return chapterRepository.findAll();
    }

    public Chapter findById(Long id){
        return chapterRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Chapter not found"));
    }

    public Chapter updateChapter(Chapter chapter) {
        return chapterRepository.save(chapter);
    }

    public void delete(Long id) {chapterRepository.deleteById(id);}
}
