package com.enzo.uiservice.proxy;

import com.enzo.uiservice.dto.ChapterDTO;
import com.enzo.uiservice.dto.ChapterSummaryDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "api-gateway", contextId = "chapterClient", path = "/api/chapters")
public interface ChapterFeignClient {
    @PostMapping
    public ChapterDTO save(@RequestBody ChapterDTO chapterDTO);

    @GetMapping
    public List<ChapterDTO> getAllChapter();

    @GetMapping("/summary")
    public List<ChapterSummaryDTO> getChapterSummaries();

    @GetMapping("/{id}")
    public ChapterDTO getChapterById(@PathVariable Long id);

    @PutMapping("/{id}")
    public ChapterDTO updateChapter(@PathVariable Long id, @RequestBody ChapterDTO chapterDTO);

    @DeleteMapping("/{id}")
    public void deleteChapter(@PathVariable Long id);

}
