package com.enzo.persistenceservice.service;
import com.enzo.persistenceservice.exception.ResourceNotFoundException;

import com.enzo.persistenceservice.entity.Answer;
import com.enzo.persistenceservice.entity.Question;
import com.enzo.persistenceservice.repository.AnswerRepository;
import com.enzo.persistenceservice.repository.QuestionRepository;
import com.enzo.persistenceservice.service.dto.AnswerCreateDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *  CRUD logic for answer
 */
@Service
@AllArgsConstructor
public class AnswerService {
    private final AnswerRepository answerRepository;
    private final QuestionRepository questionRepository;

    public Answer create(AnswerCreateDTO dto) {
        Question question = questionRepository.findById(dto.getQuestionId())
                .orElseThrow(() -> new ResourceNotFoundException("Question not found"));

        Answer answer = new Answer();
        answer.setText(dto.getText());
        answer.setCorrect(dto.isCorrect());
        answer.setQuestion(question);
        return answerRepository.save(answer);
    }

    public List<Answer> findAll() {
        return answerRepository.findAll();
    }

    public Answer findById(Long id) {
        return answerRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Answer not found"));
    }

    public Answer updateAnswer(Long id, AnswerCreateDTO dto) {
        Answer existingAnswer = findById(id);
        Question question = questionRepository.findById(dto.getQuestionId())
                .orElseThrow(() -> new ResourceNotFoundException("Question not found"));

        existingAnswer.setText(dto.getText());
        existingAnswer.setCorrect(dto.isCorrect());
        existingAnswer.setQuestion(question);
        return answerRepository.save(existingAnswer);
    }

    public void delete(Long id) {answerRepository.deleteById(id);}
}
