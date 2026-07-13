package com.enzo.authservice.config;

import com.enzo.authservice.exception.EmailAlreadyExistsException;
import com.enzo.authservice.exception.InvalidCredentialsException;
import com.enzo.authservice.exception.InvalidRequestException;
import com.enzo.authservice.exception.UsernameAlreadyExistsException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;

/** Each typed business exception must map to its HTTP status code. */
class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void emailConflict_returns409() {
        var response = handler.handleConflict(new EmailAlreadyExistsException("Email already exists"));
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(response.getBody()).contains("Email");
    }

    @Test
    void usernameConflict_returns409() {
        var response = handler.handleConflict(new UsernameAlreadyExistsException("Username already exists"));
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    }

    @Test
    void invalidCredentials_returns401() {
        var response = handler.handleInvalidCredentials(new InvalidCredentialsException("Invalid credentials"));
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    void invalidRequest_returns400() {
        var response = handler.handleInvalidRequest(new InvalidRequestException("Invalid email format"));
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void unexpectedError_returns500() {
        var response = handler.handleRuntimeException(new RuntimeException("boom"));
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
