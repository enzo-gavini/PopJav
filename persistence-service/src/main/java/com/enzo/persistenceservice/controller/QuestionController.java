package com.enzo.persistenceservice.controller;

import com.enzo.persistenceservice.entity.Question;
import com.enzo.persistenceservice.service.QuestionService;
import com.enzo.persistenceservice.service.dto.QuestionCreateDTO;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 *  REST CRUD endpoints over the question table
 */
@RestController
@RequestMapping("/api/questions")
@AllArgsConstructor
public class QuestionController {
    private final QuestionService questionService;

    @PostMapping
    public Question save(@RequestBody QuestionCreateDTO dto) {
        return questionService.create(dto);
    }

    @GetMapping
    public List<Question> getAllQuestion() {
        return questionService.findAll();
    }

    @GetMapping("/{id}")
    public Question getQuestionById(@PathVariable Long id) {
        return questionService.findById(id);
    }

    @PutMapping("/{id}")
    public Question updateQuestion(@PathVariable Long id, @RequestBody QuestionCreateDTO dto) {
        return questionService.updateQuestion(id, dto);
    }

    @DeleteMapping("/{id}")
    public void deleteQuestion(@PathVariable Long id) {
        questionService.delete(id);
    }
}
