package com.enzo.quizservice.service;

import com.enzo.quizservice.dto.AnswerDTO;
import com.enzo.quizservice.dto.QuestionDTO;
import com.enzo.quizservice.dto.ResultDTO;
import com.enzo.quizservice.service.proxy.AnswerFeignClient;
import com.enzo.quizservice.service.proxy.QuestionFeignClient;
import com.enzo.quizservice.service.proxy.ResultFeignClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * The facade services own no logic: these tests pin the delegation contract
 * to the persistence Feign clients.
 */
@ExtendWith(MockitoExtension.class)
class FacadeServicesTest {

    @Mock
    private AnswerFeignClient answerFeignClient;
    @Mock
    private QuestionFeignClient questionFeignClient;
    @Mock
    private ResultFeignClient resultFeignClient;

    @Test
    void answerService_delegatesEveryCall() {
        AnswerService service = new AnswerService(answerFeignClient);
        AnswerDTO dto = new AnswerDTO();
        when(answerFeignClient.save(dto)).thenReturn(dto);
        when(answerFeignClient.getAllAnswer()).thenReturn(List.of(dto));
        when(answerFeignClient.getAnswerById(1L)).thenReturn(dto);
        when(answerFeignClient.updateAnswer(1L, dto)).thenReturn(dto);

        assertThat(service.create(dto)).isSameAs(dto);
        assertThat(service.findAll()).hasSize(1);
        assertThat(service.findById(1L)).isSameAs(dto);
        assertThat(service.updateAnswer(1L, dto)).isSameAs(dto);
        service.delete(1L);

        verify(answerFeignClient).deleteAnswer(1L);
    }

    @Test
    void questionService_delegatesEveryCall() {
        QuestionService service = new QuestionService(questionFeignClient);
        QuestionDTO dto = new QuestionDTO();
        when(questionFeignClient.save(dto)).thenReturn(dto);
        when(questionFeignClient.getAllQuestion()).thenReturn(List.of(dto));
        when(questionFeignClient.getQuestionById(1L)).thenReturn(dto);
        when(questionFeignClient.updateQuestion(1L, dto)).thenReturn(dto);

        assertThat(service.create(dto)).isSameAs(dto);
        assertThat(service.findAll()).hasSize(1);
        assertThat(service.findById(1L)).isSameAs(dto);
        assertThat(service.updateQuestion(1L, dto)).isSameAs(dto);
        service.delete(1L);

        verify(questionFeignClient).deleteQuestion(1L);
    }

    @Test
    void resultService_delegatesEveryCall() {
        ResultService service = new ResultService(resultFeignClient);
        ResultDTO dto = new ResultDTO();
        when(resultFeignClient.save(dto)).thenReturn(dto);
        when(resultFeignClient.getAllResult()).thenReturn(List.of(dto));
        when(resultFeignClient.getResultById(1L)).thenReturn(dto);
        when(resultFeignClient.getResultsByUserId(2L)).thenReturn(List.of(dto));
        when(resultFeignClient.updateResult(1L, dto)).thenReturn(dto);

        assertThat(service.create(dto)).isSameAs(dto);
        assertThat(service.findAll()).hasSize(1);
        assertThat(service.findById(1L)).isSameAs(dto);
        assertThat(service.findByUserId(2L)).hasSize(1);
        assertThat(service.updateResult(1L, dto)).isSameAs(dto);
        service.delete(1L);

        verify(resultFeignClient).deleteResult(1L);
    }
}
