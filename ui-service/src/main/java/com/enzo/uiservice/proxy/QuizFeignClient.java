package com.enzo.uiservice.proxy;

import com.enzo.uiservice.dto.QuizDTO;
import com.enzo.uiservice.dto.QuizResultDTO;
import com.enzo.uiservice.dto.QuizSubmissionDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "api-gateway", contextId = "quizClient", path = "/api/quizzes")
public interface QuizFeignClient {

    @PostMapping
    public QuizDTO save(@RequestBody QuizDTO quizDTO);

    @GetMapping
    public List<QuizDTO> getAllQuiz();

    @GetMapping("/{id}")
    public QuizDTO getQuizById(@PathVariable Long id);

    @PutMapping("/{id}")
    public QuizDTO updateQuiz(@PathVariable Long id, @RequestBody QuizDTO quizDTO);

    @DeleteMapping("/{id}")
    public void deleteQuiz(@PathVariable Long id);

    @PostMapping("/submit")
    public QuizResultDTO submitQuiz(@RequestBody QuizSubmissionDTO submission);
}
