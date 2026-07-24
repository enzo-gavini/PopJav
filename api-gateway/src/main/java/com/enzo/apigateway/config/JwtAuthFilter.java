package com.enzo.apigateway.config;

import com.enzo.apigateway.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * Global filter executed before routing. Order of the checks: public routes,
 * JWT validation, admin rights, then injection of X-User-Id/X-User-Role.
 */
@Component
@RequiredArgsConstructor
public class JwtAuthFilter implements GlobalFilter, Ordered {
    private final JwtService jwtService;

    private final List<String> publicRoutes = List.of("/auth/register", "/auth/login");

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getPath().toString();

        if (isPublicRoute(path) || isPublicCatalog(exchange.getRequest().getMethod(), path)) {
            return chain.filter(exchange);
        }

        String authHeader = exchange.getRequest().getHeaders().getFirst("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        String token = authHeader.substring(7);

        if (!jwtService.validateToken(token)) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        if (requiresAdmin(exchange.getRequest().getMethod(), path) && !jwtService.isAdmin(token)) {
            exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
            return exchange.getResponse().setComplete();
        }
        // The identity comes from the signed token, not from the client:
        // incoming values are overwritten so a client can never forge them
        String userId = jwtService.extractUserId(token);
        ServerHttpRequest request = exchange.getRequest().mutate()
                .headers(headers -> {
                    headers.remove("X-User-Id");
                    headers.remove("X-User-Role");
                    if (userId != null) {
                        headers.set("X-User-Id", userId);
                    }
                    headers.set("X-User-Role", jwtService.extractRole(token));
                })
                .build();

        return chain.filter(exchange.mutate().request(request).build());
    }

    private boolean isPublicRoute(String path) {
        return publicRoutes.stream().anyMatch(path::startsWith);
    }

    // Public read-only catalog: the chapter list (metadata only) is browsable
    // by anonymous visitors. The full content requires a login
    private boolean isPublicCatalog(HttpMethod method, String path) {
        return HttpMethod.GET.equals(method) && path.equals("/api/chapters/summary");
    }

    private boolean requiresAdmin(HttpMethod method, String path) {
        // Internal credentials lookup (it returns the password hash) must never be
        // reachable by a regular user through the gateway
        if (path.startsWith("/api/users/credentials")) {
            return true;
        }
        // Listing every user is for admins only
        if (HttpMethod.GET.equals(method) && path.equals("/api/users")) {
            return true;
        }
        // Deleting any user account is for admins only
        if (HttpMethod.DELETE.equals(method) && path.startsWith("/api/users/")) {
            return true;
        }
        // Reading a question or an answer directly exposes the correct flag and
        // would defeat hideCorrectAnswers: these two reads are for admins only
        if (HttpMethod.GET.equals(method)
                && (path.startsWith("/api/answers") || path.startsWith("/api/questions"))) {
            return true;
        }

        // Apart from the reads listed above, only writes can need admin rights
        boolean isWrite = HttpMethod.POST.equals(method)
                || HttpMethod.PUT.equals(method)
                || HttpMethod.DELETE.equals(method);
        if (!isWrite) {
            return false;
        }

        // Normal user actions: submitting a quiz, posting a comment and saving
        // a quiz result are never admin-only
        if (path.equals("/api/quizzes/submit")) {
            return false;
        }
        if (HttpMethod.POST.equals(method) && path.startsWith("/api/comments")) {
            return false;
        }
        if (path.startsWith("/api/results")) {
            return false;
        }

        // Content authoring: only admins can create, update or delete
        return path.startsWith("/api/chapters")
                || path.startsWith("/api/lessons")
                || path.startsWith("/api/quizzes")
                || path.startsWith("/api/questions")
                || path.startsWith("/api/answers")
                || path.startsWith("/api/comments");
    }

    @Override
    public int getOrder() {
        return -1;
    }
}