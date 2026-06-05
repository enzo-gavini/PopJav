package com.enzo.quizservice.controller;

import com.enzo.quizservice.dto.QuizDTO;
import com.enzo.quizservice.service.QuizService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
}
