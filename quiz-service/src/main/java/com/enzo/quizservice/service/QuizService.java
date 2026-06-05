package com.enzo.quizservice.service;

import com.enzo.quizservice.dto.QuizDTO;
import com.enzo.quizservice.service.proxy.QuizFeignClient;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class QuizService {
    private final QuizFeignClient quizFeignClient;

    public QuizDTO create(QuizDTO quizDTO) {
        return quizFeignClient.save(quizDTO);
    }

    public List<QuizDTO> findAll() {
        return quizFeignClient.getAllQuiz();
    }

    public QuizDTO findById(Long id) {
        return quizFeignClient.getQuizById(id);
    }

    public QuizDTO updateQuiz(Long id, QuizDTO quizDTO) {
        return quizFeignClient.updateQuiz(id, quizDTO);
    }

    public void delete(Long id) {
        quizFeignClient.deleteQuiz(id);
    }
}
