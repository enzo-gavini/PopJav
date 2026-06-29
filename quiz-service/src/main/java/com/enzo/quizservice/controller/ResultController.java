package com.enzo.quizservice.controller;

import com.enzo.quizservice.dto.ResultDTO;
import com.enzo.quizservice.service.ResultService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/results")
@AllArgsConstructor
public class ResultController {
    private final ResultService resultService;

    @PostMapping
    public ResultDTO save(@RequestBody ResultDTO resultDTO) {
        return resultService.create(resultDTO);
    }

    @GetMapping
    public List<ResultDTO> getAllResult() {
        return resultService.findAll();
    }

    @GetMapping("/{id}")
    public ResultDTO getResultById(@PathVariable Long id) {
        return resultService.findById(id);
    }

    @GetMapping("/user/{userId}")
    public List<ResultDTO> getResultsByUserId(@PathVariable Long userId) {
        return resultService.findByUserId(userId);
    }

    @PutMapping("/{id}")
    public ResultDTO updateResult(@PathVariable Long id, @RequestBody ResultDTO resultDTO) {
        return resultService.updateResult(id, resultDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteResult(@PathVariable Long id) {
        resultService.delete(id);
    }
}