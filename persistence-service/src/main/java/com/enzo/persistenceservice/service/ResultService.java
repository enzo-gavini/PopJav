package com.enzo.persistenceservice.service;
import com.enzo.persistenceservice.exception.ResourceNotFoundException;

import com.enzo.persistenceservice.entity.Quiz;
import com.enzo.persistenceservice.entity.Result;
import com.enzo.persistenceservice.repository.QuizRepository;
import com.enzo.persistenceservice.repository.ResultRepository;
import com.enzo.persistenceservice.service.dto.ResultCreateDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ResultService {
    private final ResultRepository resultRepository;
    private final QuizRepository quizRepository;

    public Result create(ResultCreateDTO dto) {
        Quiz quiz = quizRepository.findById(dto.getQuizId())
                .orElseThrow(() -> new ResourceNotFoundException("Quiz not found"));

        Result result = new Result();
        result.setScore(dto.getScore());
        result.setCompleted(dto.isCompleted());
        result.setAttempts(dto.getAttempts());
        result.setUserId(dto.getUserId());
        result.setQuiz(quiz);
        return resultRepository.save(result);
    }

    public List<Result> findAll() {
        return resultRepository.findAll();
    }

    public Result findById(Long id) {
        return resultRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Result not found"));
    }

    public List<Result> findByUserId(Long userId) {
        return resultRepository.findByUserId(userId);
    }

    public Result updateResult(Long id, ResultCreateDTO dto) {
        Result existingResult = findById(id);
        Quiz quiz = quizRepository.findById(dto.getQuizId())
                .orElseThrow(() -> new ResourceNotFoundException("Quiz not found"));

        existingResult.setScore(dto.getScore());
        existingResult.setCompleted(dto.isCompleted());
        existingResult.setAttempts(dto.getAttempts());
        existingResult.setUserId(dto.getUserId());
        existingResult.setQuiz(quiz);
        return resultRepository.save(existingResult);
    }

    public void delete(Long id) {
        resultRepository.deleteById(id);
    }
}