package com.enzo.quizservice.controller;

import com.enzo.quizservice.dto.QuizDTO;
import com.enzo.quizservice.dto.QuizResultDTO;
import com.enzo.quizservice.dto.QuizSubmissionDTO;
import com.enzo.quizservice.service.QuizService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST CRUD endpoints for quizzes, relayed to persistence-service.
 * Also the last link of the X-User-Id identity chain (submit).
 */
@RestController
@RequestMapping("/api/quizzes")
@AllArgsConstructor
public class QuizController {
    private final QuizService quizService;

    @PostMapping
    public QuizDTO save(@RequestBody QuizDTO quizDTO) {
        return quizService.create(quizDTO);
    }

    @GetMapping
    public List<QuizDTO> getAllQuiz() {
        return quizService.findAll();
    }

    @GetMapping("/{id}")
    public QuizDTO getQuizById(@PathVariable Long id) {
        return quizService.findById(id);
    }

    @PutMapping("/{id}")
    public QuizDTO updateQuiz(@PathVariable Long id, @RequestBody QuizDTO quizDTO) {
        return quizService.updateQuiz(id, quizDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteQuiz(@PathVariable Long id) {
        quizService.delete(id);
    }
    // The body userId is replaced by the X-User-Id injected by the gateway, so a
    // client can never forge the JSON to play as another person. Last link of the
    // identity chain.
    @PostMapping("/submit")
    public QuizResultDTO submitQuiz(@RequestBody QuizSubmissionDTO submission,
                                    @RequestHeader("X-User-Id") Long userId) {
        submission.setUserId(userId);
        return quizService.submitQuiz(submission);
    }
}
