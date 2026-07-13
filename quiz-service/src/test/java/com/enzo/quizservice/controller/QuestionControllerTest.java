package com.enzo.quizservice.controller;

import com.enzo.quizservice.dto.QuestionDTO;
import com.enzo.quizservice.service.QuestionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/** Pure delegation to QuestionService. */
@ExtendWith(MockitoExtension.class)
class QuestionControllerTest {

    @Mock
    private QuestionService questionService;
    @InjectMocks
    private QuestionController controller;

    @Test
    void crudEndpoints_delegateToService() {
        QuestionDTO dto = new QuestionDTO();
        when(questionService.create(dto)).thenReturn(dto);
        when(questionService.findAll()).thenReturn(List.of(dto));
        when(questionService.findById(1L)).thenReturn(dto);
        when(questionService.updateQuestion(1L, dto)).thenReturn(dto);

        assertThat(controller.save(dto)).isSameAs(dto);
        assertThat(controller.getAllQuestion()).hasSize(1);
        assertThat(controller.getQuestionById(1L)).isSameAs(dto);
        assertThat(controller.updateQuestion(1L, dto)).isSameAs(dto);
        controller.deleteQuestion(1L);

        verify(questionService).delete(1L);
    }
}
