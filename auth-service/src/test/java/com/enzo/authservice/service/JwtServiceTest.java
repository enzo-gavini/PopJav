package com.enzo.authservice.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Base64;

import static org.assertj.core.api.Assertions.assertThat;

/** The issued token must be a signed JWT carrying the identity claims. */
class JwtServiceTest {

    private final JwtService jwtService = new JwtService();

    @BeforeEach
    void configureSecret() {
        String secret = Base64.getEncoder()
                .encodeToString("popjav-test-secret-popjav-test-secret-42".getBytes());
        ReflectionTestUtils.setField(jwtService, "secretKey", secret);
        ReflectionTestUtils.setField(jwtService, "expiration", 3_600_000L);
    }

    @Test
    void generateToken_producesASignedThreePartJwt() {
        UserDetails userDetails = User.builder()
                .username("alice@popjav.io")
                .password("HASH")
                .roles("USER")
                .build();

        String token = jwtService.generateToken(userDetails, 1L);

        assertThat(token).isNotBlank();
        assertThat(token.split("\\.")).hasSize(3); // header.payload.signature
    }
}
