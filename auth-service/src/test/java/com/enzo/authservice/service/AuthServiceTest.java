package com.enzo.authservice.service;

import com.enzo.authservice.dto.*;
import com.enzo.authservice.entity.Role;
import com.enzo.authservice.exception.EmailAlreadyExistsException;
import com.enzo.authservice.exception.InvalidCredentialsException;
import com.enzo.authservice.exception.InvalidRequestException;
import com.enzo.authservice.service.proxy.UserFeignClient;
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
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserFeignClient userFeignClient;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtService jwtService;
    @InjectMocks
    private AuthService authService;

    private RegisterRequest registerRequest(String username, String email, String password) {
        RegisterRequest r = new RegisterRequest();
        r.setUsername(username);
        r.setEmail(email);
        r.setPassword(password);
        return r;
    }

    private LoginRequest loginRequest(String email, String password) {
        LoginRequest r = new LoginRequest();
        r.setEmail(email);
        r.setPassword(password);
        return r;
    }

    @Test
    void register_success_returnsTokenAndStoresHashedPassword() {
        RegisterRequest req = registerRequest("alice", "alice@popjav.io", "Passw0rd!");
        when(userFeignClient.existsByEmail("alice@popjav.io")).thenReturn(false);
        when(userFeignClient.existsByUsername("alice")).thenReturn(false);
        when(passwordEncoder.encode("Passw0rd!")).thenReturn("HASH");
        UserDTO saved = new UserDTO();
        saved.setId(1L);
        saved.setEmail("alice@popjav.io");
        saved.setRole(Role.USER);
        when(userFeignClient.save(any(UserCreateRequest.class))).thenReturn(saved);
        when(jwtService.generateToken(any(), eq(1L))).thenReturn("TOKEN");

        AuthResponse res = authService.register(req);

        assertThat(res.getToken()).isEqualTo("TOKEN");
        assertThat(res.getRole()).isEqualTo(Role.USER);
        verify(userFeignClient).save(argMatchesHashed());
    }

    private static UserCreateRequest argMatchesHashed() {
        return org.mockito.ArgumentMatchers.argThat(u -> "HASH".equals(u.getPassword()) && u.getRole() == Role.USER);
    }

    @Test
    void register_emailAlreadyExists_throwsConflict_andDoesNotSave() {
        RegisterRequest req = registerRequest("alice", "alice@popjav.io", "Passw0rd!");
        when(userFeignClient.existsByEmail("alice@popjav.io")).thenReturn(true);

        assertThatThrownBy(() -> authService.register(req))
                .isInstanceOf(EmailAlreadyExistsException.class);
        verify(userFeignClient, never()).save(any());
    }

    @Test
    void register_weakPassword_throwsBadRequest() {
        RegisterRequest req = registerRequest("alice", "alice@popjav.io", "abc");
        when(userFeignClient.existsByEmail(anyString())).thenReturn(false);
        when(userFeignClient.existsByUsername(anyString())).thenReturn(false);

        assertThatThrownBy(() -> authService.register(req))
                .isInstanceOf(InvalidRequestException.class)
                .hasMessageContaining("Password");
    }

    @Test
    void register_usernameAlreadyExists_throwsConflict() {
        RegisterRequest req = registerRequest("alice", "alice@popjav.io", "Passw0rd!");
        when(userFeignClient.existsByEmail(anyString())).thenReturn(false);
        when(userFeignClient.existsByUsername("alice")).thenReturn(true);

        assertThatThrownBy(() -> authService.register(req))
                .isInstanceOf(com.enzo.authservice.exception.UsernameAlreadyExistsException.class);
    }

    @Test
    void login_wrongPassword_throwsGenericInvalidCredentials() {
        LoginRequest req = loginRequest("alice@popjav.io", "wrong");
        UserFullDTO user = new UserFullDTO();
        user.setId(1L);
        user.setEmail("alice@popjav.io");
        user.setPassword("HASH");
        user.setRole(Role.USER);
        when(userFeignClient.getUserByEmail("alice@popjav.io")).thenReturn(user);
        when(passwordEncoder.matches("wrong", "HASH")).thenReturn(false);

        assertThatThrownBy(() -> authService.login(req))
                .isInstanceOf(InvalidCredentialsException.class)
                .hasMessage("Invalid credentials");
    }
}
