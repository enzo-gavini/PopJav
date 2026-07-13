package com.enzo.authservice.controller;

import com.enzo.authservice.dto.AuthResponse;
import com.enzo.authservice.dto.LoginRequest;
import com.enzo.authservice.dto.RegisterRequest;
import com.enzo.authservice.service.AuthService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/** The controller exposes register/login and delegates everything to AuthService. */
@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private AuthService authService;
    @InjectMocks
    private AuthController controller;

    @Test
    void register_delegatesToService() {
        RegisterRequest request = new RegisterRequest();
        AuthResponse response = new AuthResponse();
        when(authService.register(request)).thenReturn(response);

        assertThat(controller.register(request)).isSameAs(response);
        verify(authService).register(request);
    }

    @Test
    void login_delegatesToService() {
        LoginRequest request = new LoginRequest();
        AuthResponse response = new AuthResponse();
        when(authService.login(request)).thenReturn(response);

        assertThat(controller.login(request)).isSameAs(response);
        verify(authService).login(request);
    }
}
