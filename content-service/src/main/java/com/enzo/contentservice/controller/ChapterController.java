package com.enzo.contentservice.controller;

import com.enzo.contentservice.dto.ChapterDTO;
import com.enzo.contentservice.dto.ChapterSummaryDTO;
import com.enzo.contentservice.service.ChapterService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chapters")
@AllArgsConstructor
public class ChapterController {
    private final ChapterService chapterService;

    @PostMapping
    public ChapterDTO save(@RequestBody ChapterDTO chapterDTO) {
        return chapterService.create(chapterDTO);
    }

    @GetMapping
    public List<ChapterDTO> getAllChapter() {
        return chapterService.findAll();
    }

    @GetMapping("/summary")
    public List<ChapterSummaryDTO> getChapterSummaries() {
        return chapterService.findAllSummary();
    }

    @GetMapping("/{id}")
    public ChapterDTO getChapterById(@PathVariable Long id) {
        return chapterService.findById(id);
    }

    @PutMapping("/{id}")
    public ChapterDTO updateChapter(@PathVariable Long id, @RequestBody ChapterDTO chapterDTO) {
        return chapterService.updateChapter(id,chapterDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteChapter(@PathVariable Long id) {chapterService.delete(id);}
}
