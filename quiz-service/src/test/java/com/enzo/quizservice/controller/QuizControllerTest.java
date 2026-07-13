package com.enzo.quizservice.controller;

import com.enzo.quizservice.dto.QuizDTO;
import com.enzo.quizservice.dto.QuizResultDTO;
import com.enzo.quizservice.dto.QuizSubmissionDTO;
import com.enzo.quizservice.service.QuizService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * The controller only delegates to the service; the important behaviour is
 * that submit overwrites the body userId with the gateway identity header.
 */
@ExtendWith(MockitoExtension.class)
class QuizControllerTest {

    @Mock
    private QuizService quizService;
    @InjectMocks
    private QuizController controller;

    @Test
    void submitQuiz_overwritesBodyUserId_withGatewayHeader() {
        QuizSubmissionDTO submission = new QuizSubmissionDTO();
        submission.setUserId(99999L); // forged by the client
        when(quizService.submitQuiz(submission)).thenReturn(new QuizResultDTO());

        controller.submitQuiz(submission, 2L); // 2 = X-User-Id set by the gateway

        assertThat(submission.getUserId()).isEqualTo(2L);
        verify(quizService).submitQuiz(submission);
    }

    @Test
    void crudEndpoints_delegateToService() {
        QuizDTO dto = new QuizDTO();
        when(quizService.create(dto)).thenReturn(dto);
        when(quizService.findAll()).thenReturn(List.of(dto));
        when(quizService.findById(1L)).thenReturn(dto);
        when(quizService.updateQuiz(1L, dto)).thenReturn(dto);

        assertThat(controller.save(dto)).isSameAs(dto);
        assertThat(controller.getAllQuiz()).hasSize(1);
        assertThat(controller.getQuizById(1L)).isSameAs(dto);
        assertThat(controller.updateQuiz(1L, dto)).isSameAs(dto);
        controller.deleteQuiz(1L);

        verify(quizService).delete(1L);
    }
}
