package com.enzo.quizservice.controller;

import com.enzo.quizservice.dto.AnswerDTO;
import com.enzo.quizservice.service.AnswerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/** Pure delegation to AnswerService. */
@ExtendWith(MockitoExtension.class)
class AnswerControllerTest {

    @Mock
    private AnswerService answerService;
    @InjectMocks
    private AnswerController controller;

    @Test
    void crudEndpoints_delegateToService() {
        AnswerDTO dto = new AnswerDTO();
        when(answerService.create(dto)).thenReturn(dto);
        when(answerService.findAll()).thenReturn(List.of(dto));
        when(answerService.findById(1L)).thenReturn(dto);
        when(answerService.updateAnswer(1L, dto)).thenReturn(dto);

        assertThat(controller.save(dto)).isSameAs(dto);
        assertThat(controller.getAllAnswer()).hasSize(1);
        assertThat(controller.getAnswerById(1L)).isSameAs(dto);
        assertThat(controller.updateAnswer(1L, dto)).isSameAs(dto);
        controller.deleteAnswer(1L);

        verify(answerService).delete(1L);
    }
}
