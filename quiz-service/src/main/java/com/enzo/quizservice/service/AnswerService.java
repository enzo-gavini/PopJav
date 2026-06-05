package com.enzo.quizservice.service;

import com.enzo.quizservice.dto.AnswerDTO;
import com.enzo.quizservice.service.proxy.AnswerFeignClient;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class AnswerService {
    private final AnswerFeignClient answerFeignClient;

    public AnswerDTO create(AnswerDTO answerDTO) {
        return answerFeignClient.save(answerDTO);
    }

    public List<AnswerDTO> findAll() {
        return answerFeignClient.getAllAnswer();
    }

    public AnswerDTO findById(Long id) {
        return answerFeignClient.getAnswerById(id);
    }

    public AnswerDTO updateAnswer(Long id, AnswerDTO answerDTO) {
        return answerFeignClient.updateAnswer(id, answerDTO);
    }

    public void delete(Long id) {
        answerFeignClient.deleteAnswer(id);
    }
}
