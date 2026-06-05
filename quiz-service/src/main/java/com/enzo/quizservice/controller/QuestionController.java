package com.enzo.quizservice.controller;

import com.enzo.quizservice.dto.QuestionDTO;
import com.enzo.quizservice.service.QuestionService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/questions")
@AllArgsConstructor
public class QuestionController {
    private final QuestionService questionService;

    @PostMapping
    public QuestionDTO save(@RequestBody QuestionDTO questionDTO) {
        return questionService.create(questionDTO);
    }

    @GetMapping
    public List<QuestionDTO> getAllQuestion() {
        return questionService.findAll();
    }

    @GetMapping("/{id}")
    public QuestionDTO getQuestionById(@PathVariable Long id) {
        return questionService.findById(id);
    }

    @PutMapping("/{id}")
    public QuestionDTO updateQuestion(@PathVariable Long id, @RequestBody QuestionDTO questionDTO) {
        return questionService.updateQuestion(id, questionDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteQuestion(@PathVariable Long id) {
        questionService.delete(id);
    }
}
