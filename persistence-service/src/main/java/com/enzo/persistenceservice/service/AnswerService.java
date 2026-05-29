package com.enzo.persistenceservice.service;

import com.enzo.persistenceservice.entity.Answer;
import com.enzo.persistenceservice.repository.AnswerRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class AnswerService {
    private final AnswerRepository answerRepository;

    public Answer create(Answer answer) {
        return answerRepository.save(answer);
    }

    public List<Answer> findAll() {
        return answerRepository.findAll();
    }

    public Answer findById(Long id) {
        return answerRepository.findById(id).orElseThrow(() -> new RuntimeException("Answer not found"));
    }

    public Answer updateAnswer(Answer answer) {
        return answerRepository.save(answer);
    }

    public void delete(Long id) {answerRepository.deleteById(id);}
}
