package com.enzo.persistenceservice.service;
import com.enzo.persistenceservice.exception.ResourceNotFoundException;

import com.enzo.persistenceservice.entity.Question;
import com.enzo.persistenceservice.entity.Quiz;
import com.enzo.persistenceservice.repository.QuestionRepository;
import com.enzo.persistenceservice.repository.QuizRepository;
import com.enzo.persistenceservice.service.dto.QuestionCreateDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class QuestionService {
    private final QuestionRepository questionRepository;
    private final QuizRepository quizRepository;

    public Question create(QuestionCreateDTO dto) {
        Quiz quiz = quizRepository.findById(dto.getQuizId())
                .orElseThrow(()-> new ResourceNotFoundException("Quiz not found"));

        Question question = new Question();
        question.setText(dto.getText());
        question.setQuiz(quiz);
        return questionRepository.save(question);
    }

    public List<Question> findAll() {
        return questionRepository.findAll();
    }

    public Question findById(Long id) {
        return questionRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Question not found"));
    }

    public Question updateQuestion(Long id, QuestionCreateDTO dto) {
        Question existingQuestion = findById(id);
        Quiz quiz = quizRepository.findById(dto.getQuizId())
                .orElseThrow(() -> new ResourceNotFoundException("Quiz not found"));

        existingQuestion.setText(dto.getText());
        existingQuestion.setQuiz(quiz);
        return questionRepository.save(existingQuestion);
    }

    public void delete(Long id) {questionRepository.deleteById(id);}

}
