package com.enzo.apigateway.config;

import com.enzo.apigateway.service.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Access control rules applied by the gateway before routing. Reading raw
 * questions or answers is admin-only: it would otherwise reveal which answer
 * is correct and defeat the anti-cheat of quiz-service.
 */
class JwtAuthFilterTest {

    private JwtService jwtService;
    private GatewayFilterChain chain;
    private JwtAuthFilter filter;

    @BeforeEach
    void setUp() {
        jwtService = mock(JwtService.class);
        chain = mock(GatewayFilterChain.class);
        filter = new JwtAuthFilter(jwtService);

        // The token itself is never parsed here: JwtService is mocked, so the
        // test only exercises the routing rules
        when(chain.filter(any(ServerWebExchange.class))).thenReturn(Mono.empty());
        when(jwtService.validateToken(anyString())).thenReturn(true);
        when(jwtService.extractUserId(anyString())).thenReturn("2");
        when(jwtService.extractRole(anyString())).thenReturn("USER");
    }

    private MockServerWebExchange call(HttpMethod method, String path) {
        MockServerHttpRequest request = MockServerHttpRequest
                .method(method, path)
                .header("Authorization", "Bearer any-valid-token")
                .build();
        return MockServerWebExchange.from(request);
    }

    @Test
    void userReadingAnswers_isForbidden() {
        when(jwtService.isAdmin(anyString())).thenReturn(false);
        MockServerWebExchange exchange = call(HttpMethod.GET, "/api/answers");

        filter.filter(exchange, chain).block();

        assertEquals(HttpStatus.FORBIDDEN, exchange.getResponse().getStatusCode());
        verify(chain, never()).filter(any(ServerWebExchange.class));
    }

    @Test
    void userReadingOneQuestion_isForbidden() {
        when(jwtService.isAdmin(anyString())).thenReturn(false);
        MockServerWebExchange exchange = call(HttpMethod.GET, "/api/questions/1");

        filter.filter(exchange, chain).block();

        assertEquals(HttpStatus.FORBIDDEN, exchange.getResponse().getStatusCode());
        verify(chain, never()).filter(any(ServerWebExchange.class));
    }

    @Test
    void adminReadingAnswers_isAllowed() {
        when(jwtService.isAdmin(anyString())).thenReturn(true);
        MockServerWebExchange exchange = call(HttpMethod.GET, "/api/answers");

        filter.filter(exchange, chain).block();

        assertNotEquals(HttpStatus.FORBIDDEN, exchange.getResponse().getStatusCode());
        verify(chain).filter(any(ServerWebExchange.class));
    }

    // The player route must keep working: it serves quizzes with the correct
    // flags already hidden by quiz-service
    @Test
    void userPlayingAQuiz_isStillAllowed() {
        when(jwtService.isAdmin(anyString())).thenReturn(false);
        MockServerWebExchange exchange = call(HttpMethod.GET, "/api/quizzes/1");

        filter.filter(exchange, chain).block();

        assertNotEquals(HttpStatus.FORBIDDEN, exchange.getResponse().getStatusCode());
        verify(chain).filter(any(ServerWebExchange.class));
    }

    @Test
    void userSubmittingAQuiz_isStillAllowed() {
        when(jwtService.isAdmin(anyString())).thenReturn(false);
        MockServerWebExchange exchange = call(HttpMethod.POST, "/api/quizzes/submit");

        filter.filter(exchange, chain).block();

        assertNotEquals(HttpStatus.FORBIDDEN, exchange.getResponse().getStatusCode());
        verify(chain).filter(any(ServerWebExchange.class));
    }

    @Test
    void requestWithoutToken_isUnauthorized() {
        MockServerHttpRequest request = MockServerHttpRequest
                .method(HttpMethod.GET, "/api/answers")
                .build();
        MockServerWebExchange exchange = MockServerWebExchange.from(request);

        filter.filter(exchange, chain).block();

        assertEquals(HttpStatus.UNAUTHORIZED, exchange.getResponse().getStatusCode());
        verify(chain, never()).filter(any(ServerWebExchange.class));
    }
}