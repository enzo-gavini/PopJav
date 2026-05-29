package com.enzo.persistenceservice.controller;

import com.enzo.persistenceservice.entity.Quiz;
import com.enzo.persistenceservice.service.QuizService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/quizzes")
@AllArgsConstructor
public class QuizController {
    private final QuizService quizService;

    @PostMapping
    public Quiz save(@RequestBody Quiz quiz) {
        return quizService.create(quiz);
    }

    @GetMapping
    public List<Quiz> getAllQuiz() {
        return quizService.findAll();
    }

    @GetMapping("/{id}")
    public Quiz getQuizById(@PathVariable Long id) {
        return quizService.findById(id);
    }

    @PutMapping("/{id}")
    public Quiz updateQuiz(@PathVariable Long id, @RequestBody Quiz quiz) {
        return quizService.updateQuiz(quiz);
    }

    @DeleteMapping("/{id}")
    public void deleteQuiz(@PathVariable Long id) {
        quizService.delete(id);
    }
}
