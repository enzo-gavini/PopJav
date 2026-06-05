package com.enzo.quizservice.service.proxy;

import com.enzo.quizservice.dto.AnswerDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "persistence-service", contextId = "answerClient", path = "/api/answers")
public interface AnswerFeignClient {

    @PostMapping
    public AnswerDTO save(@RequestBody AnswerDTO answerDTO);

    @GetMapping
    public List<AnswerDTO> getAllAnswer();

    @GetMapping("/{id}")
    public AnswerDTO getAnswerById(@PathVariable Long id);

    @PutMapping("/{id}")
    public AnswerDTO updateAnswer(@PathVariable Long id, @RequestBody AnswerDTO answerDTO);

    @DeleteMapping("/{id}")
    public void deleteAnswer(@PathVariable Long id);
}
