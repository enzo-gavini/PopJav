package com.enzo.persistenceservice.service;

import com.enzo.persistenceservice.entity.Question;
import com.enzo.persistenceservice.repository.QuestionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class QuestionService {
    private final QuestionRepository questionRepository;

    public Question create(Question question) {
        return questionRepository.save(question);
    }

    public List<Question> findAll() {
        return questionRepository.findAll();
    }

    public Question findById(Long id) {
        return questionRepository.findById(id).orElseThrow(() -> new RuntimeException("Question not found"));
    }

    public Question updateQuestion(Question question) {
        return questionRepository.save(question);
    }

    public void delete(Long id) {questionRepository.deleteById(id);}

}
