package com.enzo.persistenceservice.service;
import com.enzo.persistenceservice.exception.ResourceNotFoundException;

import com.enzo.persistenceservice.entity.Lesson;
import com.enzo.persistenceservice.entity.Quiz;
import com.enzo.persistenceservice.repository.LessonRepository;
import com.enzo.persistenceservice.repository.QuizRepository;
import com.enzo.persistenceservice.service.dto.LessonCreateDTO;
import com.enzo.persistenceservice.service.dto.QuizCreateDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *  CRUD logic for quiz
 */
@Service
@AllArgsConstructor
public class QuizService {
    private final QuizRepository quizRepository;
    private final LessonRepository lessonRepository;

    public Quiz create(QuizCreateDTO dto){
        Lesson lesson = lessonRepository.findById(dto.getLessonId())
                .orElseThrow(()-> new ResourceNotFoundException("Lesson not found"));

        Quiz quiz = new Quiz();
        quiz.setTitle(dto.getTitle());
        quiz.setLives(dto.getLives());
        quiz.setPassingScore(dto.getPassingScore());
        quiz.setLesson(lesson);
        return quizRepository.save(quiz);
    }

    public List<Quiz> findAll() {
        return quizRepository.findAll();
    }

    public Quiz findById(Long id) {
        return quizRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Quiz not found"));
    }

    public Quiz updateQuiz(Long id, QuizCreateDTO dto) {
        Quiz existingQuiz = findById(id);
        Lesson lesson = lessonRepository.findById(dto.getLessonId())
                .orElseThrow(() -> new ResourceNotFoundException("Lesson not found"));

        existingQuiz.setTitle(dto.getTitle());
        existingQuiz.setLives(dto.getLives());
        existingQuiz.setPassingScore(dto.getPassingScore());
        existingQuiz.setLesson(lesson);
        return quizRepository.save(existingQuiz);
    }

    public void delete(Long id) {quizRepository.deleteById(id);}
}