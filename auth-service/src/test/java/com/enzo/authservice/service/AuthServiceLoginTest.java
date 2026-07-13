package com.enzo.authservice.service;

import com.enzo.authservice.dto.AuthResponse;
import com.enzo.authservice.dto.LoginRequest;
import com.enzo.authservice.dto.RegisterRequest;
import com.enzo.authservice.dto.UserFullDTO;
import com.enzo.authservice.entity.Role;
import com.enzo.authservice.exception.InvalidCredentialsException;
import com.enzo.authservice.exception.InvalidRequestException;
import com.enzo.authservice.service.proxy.UserFeignClient;
import feign.FeignException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Login success path, unknown-email anti-enumeration and the server-side
 * email format validation.
 */
@ExtendWith(MockitoExtension.class)
class AuthServiceLoginTest {

    @Mock
    private UserFeignClient userFeignClient;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtService jwtService;
    @InjectMocks
    private AuthService authService;

    @Test
    void login_validCredentials_returnsTokenAndIdentity() {
        LoginRequest request = new LoginRequest();
        request.setEmail("alice@popjav.io");
        request.setPassword("Passw0rd!");
        UserFullDTO user = new UserFullDTO();
        user.setId(1L);
        user.setEmail("alice@popjav.io");
        user.setPassword("HASH");
        user.setRole(Role.USER);
        when(userFeignClient.getUserByEmail("alice@popjav.io")).thenReturn(user);
        when(passwordEncoder.matches("Passw0rd!", "HASH")).thenReturn(true);
        when(jwtService.generateToken(any(), eq(1L))).thenReturn("TOKEN");

        AuthResponse response = authService.login(request);

        assertThat(response.getToken()).isEqualTo("TOKEN");
        assertThat(response.getUserId()).isEqualTo(1L);
        assertThat(response.getRole()).isEqualTo(Role.USER);
    }

    @Test
    void login_unknownEmail_throwsTheSameGenericError() {
        LoginRequest request = new LoginRequest();
        request.setEmail("ghost@popjav.io");
        request.setPassword("whatever");
        when(userFeignClient.getUserByEmail("ghost@popjav.io"))
                .thenThrow(mock(FeignException.class));

        assertThatThrownBy(() -> authService.login(request))
                .isInstanceOf(InvalidCredentialsException.class)
                .hasMessage("Invalid credentials"); // same message as a wrong password
    }

    @Test
    void register_invalidEmailFormat_throwsBadRequest() {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("alice");
        request.setEmail("not-an-email");
        request.setPassword("Passw0rd!");
        when(userFeignClient.existsByEmail(anyString())).thenReturn(false);
        when(userFeignClient.existsByUsername(anyString())).thenReturn(false);

        assertThatThrownBy(() -> authService.register(request))
                .isInstanceOf(InvalidRequestException.class)
                .hasMessageContaining("email");
    }
}
