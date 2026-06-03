package com.enzo.persistenceservice.controller;

import com.enzo.persistenceservice.entity.Answer;
import com.enzo.persistenceservice.service.AnswerService;
import com.enzo.persistenceservice.service.dto.AnswerCreateDTO;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/answers")
@AllArgsConstructor
public class AnswerController {
    private final AnswerService answerService;

    @PostMapping
    public Answer save(@RequestBody AnswerCreateDTO dto) {
        return answerService.create(dto);
    }

    @GetMapping
    public List<Answer> getAllAnswer() {
        return answerService.findAll();
    }

    @GetMapping("/{id}")
    public Answer getAnswerById(@PathVariable Long id) {
        return answerService.findById(id);
    }

    @PutMapping("/{id}")
    public Answer updateAnswer(@PathVariable Long id, @RequestBody Answer answer) {
        return answerService.updateAnswer(answer);
    }

    @DeleteMapping("/{id}")
    public void deleteAnswer(@PathVariable Long id) {
        answerService.delete(id);
    }
}
