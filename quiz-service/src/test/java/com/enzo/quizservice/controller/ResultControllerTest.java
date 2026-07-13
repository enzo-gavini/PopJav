package com.enzo.quizservice.controller;

import com.enzo.quizservice.dto.ResultDTO;
import com.enzo.quizservice.service.ResultService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Ownership rules on results: a user reads only their own, an admin reads
 * anyone's, anybody else gets a 403.
 */
@ExtendWith(MockitoExtension.class)
class ResultControllerTest {

    @Mock
    private ResultService resultService;
    @InjectMocks
    private ResultController controller;

    @Test
    void getResultsByUserId_owner_isAllowed() {
        when(resultService.findByUserId(2L)).thenReturn(List.of(new ResultDTO()));

        assertThat(controller.getResultsByUserId(2L, 2L, "USER")).hasSize(1);
    }

    @Test
    void getResultsByUserId_admin_canReadAnyone() {
        when(resultService.findByUserId(2L)).thenReturn(List.of(new ResultDTO()));

        assertThat(controller.getResultsByUserId(2L, 1L, "ADMIN")).hasSize(1);
    }

    @Test
    void getResultsByUserId_otherUser_isForbidden() {
        assertThatThrownBy(() -> controller.getResultsByUserId(1L, 2L, "USER"))
                .isInstanceOf(ResponseStatusException.class)
                .extracting(e -> ((ResponseStatusException) e).getStatusCode())
                .isEqualTo(HttpStatus.FORBIDDEN);
        verify(resultService, never()).findByUserId(1L);
    }

    @Test
    void crudEndpoints_delegateToService() {
        ResultDTO dto = new ResultDTO();
        when(resultService.create(dto)).thenReturn(dto);
        when(resultService.findAll()).thenReturn(List.of(dto));
        when(resultService.findById(1L)).thenReturn(dto);
        when(resultService.updateResult(1L, dto)).thenReturn(dto);

        assertThat(controller.save(dto)).isSameAs(dto);
        assertThat(controller.getAllResult()).hasSize(1);
        assertThat(controller.getResultById(1L)).isSameAs(dto);
        assertThat(controller.updateResult(1L, dto)).isSameAs(dto);
        controller.deleteResult(1L);

        verify(resultService).delete(1L);
    }
}
