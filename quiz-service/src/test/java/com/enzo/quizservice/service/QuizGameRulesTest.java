package com.enzo.quizservice.service;

import com.enzo.quizservice.dto.AnswerDTO;
import com.enzo.quizservice.dto.QuestionDTO;
import com.enzo.quizservice.dto.QuizDTO;
import com.enzo.quizservice.dto.QuizResultDTO;
import com.enzo.quizservice.dto.QuizSubmissionDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.enzo.quizservice.service.proxy.QuizFeignClient;
import com.enzo.quizservice.service.proxy.ResultFeignClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Game rules of the quiz engine: lives countdown, early stop at zero lives
 * and the passing-score threshold.
 */
@ExtendWith(MockitoExtension.class)
class QuizGameRulesTest {

    @Mock
    private QuizFeignClient quizFeignClient;
    @Mock
    private ResultFeignClient resultFeignClient;
    @InjectMocks
    private QuizService quizService;

    private AnswerDTO answer(long id, boolean correct) {
        AnswerDTO a = new AnswerDTO();
        a.setId(id);
        a.setCorrect(correct);
        return a;
    }

    private QuestionDTO question(long id, AnswerDTO... answers) {
        QuestionDTO q = new QuestionDTO();
        q.setId(id);
        q.setAnswers(new ArrayList<>(List.of(answers)));
        return q;
    }

    private QuizDTO quiz(int lives, int passingScore, QuestionDTO... questions) {
        QuizDTO q = new QuizDTO();
        q.setId(10L);
        q.setLives(lives);
        q.setPassingScore(passingScore);
        q.setQuestions(new ArrayList<>(List.of(questions)));
        return q;
    }

    private QuizSubmissionDTO submission(Map<Long, Long> answers) {
        QuizSubmissionDTO s = new QuizSubmissionDTO();
        s.setQuizId(10L);
        s.setUserId(1L);
        s.setAnswers(answers);
        return s;
    }

    @Test
    void submitQuiz_wrongAnswers_costLives_andGameStopsAtZero() {
        // 2 lives, 4 questions: two wrong answers must end the game before question 4
        QuizDTO quiz = quiz(2, 50,
                question(1L, answer(11L, true), answer(12L, false)),
                question(2L, answer(21L, true), answer(22L, false)),
                question(3L, answer(31L, true), answer(32L, false)),
                question(4L, answer(41L, true), answer(42L, false)));
        when(quizFeignClient.getQuizById(10L)).thenReturn(quiz);

        Map<Long, Long> answers = new HashMap<>();
        answers.put(1L, 12L); // wrong -> 1 life left
        answers.put(2L, 22L); // wrong -> 0 life, game over
        answers.put(3L, 31L); // never evaluated
        answers.put(4L, 41L); // never evaluated

        QuizResultDTO result = quizService.submitQuiz(submission(answers));

        assertThat(result.getLivesRemaining()).isZero();
        assertThat(result.getDetails()).hasSize(2); // questions 3 and 4 not evaluated
        assertThat(result.isPassed()).isFalse();
    }

    @Test
    void submitQuiz_belowPassingScore_failsButKeepsScore() {
        // 1 correct out of 3 = 33 %, threshold 60 % -> failed
        QuizDTO quiz = quiz(3, 60,
                question(1L, answer(11L, true), answer(12L, false)),
                question(2L, answer(21L, true), answer(22L, false)),
                question(3L, answer(31L, true), answer(32L, false)));
        when(quizFeignClient.getQuizById(10L)).thenReturn(quiz);

        Map<Long, Long> answers = new HashMap<>();
        answers.put(1L, 11L); // correct
        answers.put(2L, 22L); // wrong
        answers.put(3L, 32L); // wrong

        QuizResultDTO result = quizService.submitQuiz(submission(answers));

        assertThat(result.getScore()).isEqualTo(33);
        assertThat(result.isPassed()).isFalse();
        assertThat(result.getLivesRemaining()).isEqualTo(1);
        verify(resultFeignClient).save(any());
    }

    @Test
    void crudMethods_delegateToPersistence() {
        QuizDTO dto = new QuizDTO();
        when(quizFeignClient.save(dto)).thenReturn(dto);
        when(quizFeignClient.getAllQuiz()).thenReturn(new ArrayList<>(List.of(dto)));
        when(quizFeignClient.updateQuiz(1L, dto)).thenReturn(dto);

        assertThat(quizService.create(dto)).isSameAs(dto);
        assertThat(quizService.findAll()).hasSize(1);
        assertThat(quizService.updateQuiz(1L, dto)).isSameAs(dto);
        quizService.delete(1L);

        verify(quizFeignClient).deleteQuiz(eq(1L));
    }
}
