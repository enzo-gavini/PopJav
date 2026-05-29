package com.enzo.persistenceservice.service;

import com.enzo.persistenceservice.entity.Quiz;
import com.enzo.persistenceservice.repository.QuizRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class QuizService {
    private final QuizRepository quizRepository;

    public Quiz create(Quiz quiz){
        return quizRepository.save(quiz);
    }

    public List<Quiz> findAll() {
        return quizRepository.findAll();
    }

    public Quiz findById(Long id) {
        return quizRepository.findById(id).orElseThrow(()-> new RuntimeException("Quiz not found"));
    }

    public Quiz updateQuiz(Quiz quiz) {
        return quizRepository.save(quiz);
    }

    public void delete(Long id) {quizRepository.deleteById(id);}
}