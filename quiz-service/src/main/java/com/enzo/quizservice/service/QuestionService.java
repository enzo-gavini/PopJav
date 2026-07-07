package com.enzo.quizservice.service;

import com.enzo.quizservice.dto.QuestionDTO;
import com.enzo.quizservice.service.proxy.QuestionFeignClient;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * CRUD logic for questions: a Feign facade to persistence-service, no local storage.
 */
@Service
@AllArgsConstructor
public class QuestionService {
    private final QuestionFeignClient questionFeignClient;

    public QuestionDTO create(QuestionDTO questionDTO) {
        return questionFeignClient.save(questionDTO);
    }

    public List<QuestionDTO> findAll() {
        return questionFeignClient.getAllQuestion();
    }

    public QuestionDTO findById(Long id) {
        return questionFeignClient.getQuestionById(id);
    }

    public QuestionDTO updateQuestion(Long id, QuestionDTO questionDTO) {
        return questionFeignClient.updateQuestion(id, questionDTO);
    }

    public void delete(Long id) {
        questionFeignClient.deleteQuestion(id);
    }
}
