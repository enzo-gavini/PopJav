package com.enzo.quizservice.controller;

import com.enzo.quizservice.dto.AnswerDTO;
import com.enzo.quizservice.service.AnswerService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST CRUD endpoints for answers, relayed to persistence-service.
 */
@RestController
@RequestMapping("/api/answers")
@AllArgsConstructor
public class AnswerController {
    private final AnswerService answerService;

    @PostMapping
    public AnswerDTO save(@RequestBody AnswerDTO answerDTO) {
        return answerService.create(answerDTO);
    }

    @GetMapping
    public List<AnswerDTO> getAllAnswer() {
        return answerService.findAll();
    }

    @GetMapping("/{id}")
    public AnswerDTO getAnswerById(@PathVariable Long id) {
        return answerService.findById(id);
    }

    @PutMapping("/{id}")
    public AnswerDTO updateAnswer(@PathVariable Long id, @RequestBody AnswerDTO answerDTO) {
        return answerService.updateAnswer(id,answerDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteAnswer(@PathVariable Long id) {
        answerService.delete(id);
    }
}
