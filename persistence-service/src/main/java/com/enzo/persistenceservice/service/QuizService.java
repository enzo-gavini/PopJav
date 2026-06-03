package com.enzo.persistenceservice.service;

import com.enzo.persistenceservice.entity.Lesson;
import com.enzo.persistenceservice.entity.Quiz;
import com.enzo.persistenceservice.repository.LessonRepository;
import com.enzo.persistenceservice.repository.QuizRepository;
import com.enzo.persistenceservice.service.dto.LessonCreateDTO;
import com.enzo.persistenceservice.service.dto.QuizCreateDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class QuizService {
    private final QuizRepository quizRepository;
    private final LessonRepository lessonRepository;

    public Quiz create(QuizCreateDTO dto){
        Lesson lesson = lessonRepository.findById(dto.getLessonId())
                .orElseThrow(()-> new RuntimeException("Lesson not found"));

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
        return quizRepository.findById(id).orElseThrow(()-> new RuntimeException("Quiz not found"));
    }

    public Quiz updateQuiz(Quiz quiz) {
        return quizRepository.save(quiz);
    }

    public void delete(Long id) {quizRepository.deleteById(id);}
}