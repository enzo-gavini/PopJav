package com.enzo.quizservice.service;

import com.enzo.quizservice.dto.AnswerDTO;
import com.enzo.quizservice.dto.QuestionDTO;
import com.enzo.quizservice.dto.QuizDTO;
import com.enzo.quizservice.dto.QuizResultDTO;
import com.enzo.quizservice.dto.QuizSubmissionDTO;
import com.enzo.quizservice.service.proxy.QuizFeignClient;
import com.enzo.quizservice.service.proxy.ResultFeignClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class QuizServiceTest {

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
    void submitQuiz_allCorrect_scores100_passes_andSavesResult() {
        QuizDTO quiz = quiz(3, 70,
                question(1L, answer(1L, true), answer(2L, false)),
                question(2L, answer(3L, true), answer(4L, false)));
        when(quizFeignClient.getQuizById(10L)).thenReturn(quiz);

        QuizResultDTO result = quizService.submitQuiz(submission(Map.of(1L, 1L, 2L, 3L)));

        assertThat(result.getScore()).isEqualTo(100);
        assertThat(result.isPassed()).isTrue();
        assertThat(result.getLivesRemaining()).isEqualTo(3);
        verify(resultFeignClient).save(argThat(r ->
                r.getScore() == 100 && r.isCompleted() && r.getUserId() == 1L && r.getQuizId() == 10L));
    }

    @Test
    void submitQuiz_noQuestions_returnsZero_withoutDivisionByZero() {
        QuizDTO quiz = quiz(3, 70); // no questions
        when(quizFeignClient.getQuizById(10L)).thenReturn(quiz);

        QuizResultDTO[] holder = new QuizResultDTO[1];
        assertThatCode(() -> holder[0] = quizService.submitQuiz(submission(Map.of())))
                .doesNotThrowAnyException();

        assertThat(holder[0].getScore()).isEqualTo(0);
        assertThat(holder[0].isPassed()).isFalse();
    }

    @Test
    void findById_hidesCorrectFlag_fromClient() {
        QuizDTO quiz = quiz(3, 70, question(1L, answer(1L, true), answer(2L, false)));
        when(quizFeignClient.getQuizById(10L)).thenReturn(quiz);

        QuizDTO served = quizService.findById(10L);

        assertThat(served.getQuestions().get(0).getAnswers())
                .allMatch(a -> !a.isCorrect());
    }
}
