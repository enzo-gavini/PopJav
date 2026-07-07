package com.enzo.persistenceservice.controller;

import com.enzo.persistenceservice.entity.Chapter;
import com.enzo.persistenceservice.service.ChapterService;
import com.enzo.persistenceservice.service.dto.ChapterSummaryDTO;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 *  REST CRUD endpoints over the chapter table
 */
@RestController
@RequestMapping("/api/chapters")
@AllArgsConstructor
public class ChapterController {
    private final ChapterService chapterService;

    @PostMapping
    public Chapter save(@RequestBody Chapter chapter) {
        return chapterService.create(chapter);
    }

    @GetMapping
    public List<Chapter> getAllChapter() {
        return chapterService.findAll();
    }

    @GetMapping("/summary")
    public List<ChapterSummaryDTO> getChapterSummaries() {
        return chapterService.findAll().stream()
                .map(c -> new ChapterSummaryDTO(c.getId(), c.getTitle(), c.getDescription(), c.getOrderIndex()))
                .toList();
    }

    @GetMapping("/{id}")
    public Chapter getChapterById(@PathVariable Long id) {
        return chapterService.findById(id);
    }

    @PutMapping("/{id}")
    public Chapter updateChapter(@PathVariable Long id, @RequestBody Chapter chapter) {
        return chapterService.updateChapter(chapter);
    }

    @DeleteMapping ("/{id}")
    public void deleteChapter(@PathVariable Long id) {
        chapterService.delete(id);
    }



}
