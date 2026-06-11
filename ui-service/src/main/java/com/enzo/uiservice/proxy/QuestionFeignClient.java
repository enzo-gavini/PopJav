package com.enzo.uiservice.proxy;

import com.enzo.uiservice.dto.QuestionDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "api-gateway", contextId = "questionClient", path = "/api/questions")
public interface QuestionFeignClient {
    @PostMapping
    public QuestionDTO save(@RequestBody QuestionDTO questionDTO);

    @GetMapping
    public List<QuestionDTO> getAllQuestion();

    @GetMapping("/{id}")
    public QuestionDTO getQuestionById(@PathVariable Long id);

    @PutMapping("/{id}")
    public QuestionDTO updateQuestion(@PathVariable Long id, @RequestBody QuestionDTO questionDTO);

    @DeleteMapping("/{id}")
    public void  deleteQuestion(@PathVariable Long id);
}
