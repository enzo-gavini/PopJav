package com.enzo.uiservice.proxy;

import com.enzo.uiservice.dto.LessonDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "api-gateway", contextId = "lessonClient", path = "/api/lessons")
public interface LessonFeignClient {

    @PostMapping
    public LessonDTO save(@RequestBody LessonDTO lessonDTO);

    @GetMapping
    public List<LessonDTO> getAllLesson();

    @GetMapping("/{id}")
    public LessonDTO getLessonById(@PathVariable Long id);

    @PutMapping("/{id}")
    public LessonDTO updateLesson(@PathVariable Long id, @RequestBody LessonDTO lessonDTO);

    @DeleteMapping("/{id}")
    public void deleteLesson(@PathVariable Long id);
}
