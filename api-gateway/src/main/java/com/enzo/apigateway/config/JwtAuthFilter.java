package com.enzo.apigateway.config;

import com.enzo.apigateway.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

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

        return chain.filter(exchange);
    }

    private boolean isPublicRoute(String path) {
        return publicRoutes.stream().anyMatch(path::startsWith);
    }

    // Public read-only catalog: the chapter list (metadata only) is browsable
    // by anonymous visitors. Full content stays authenticated.
    private boolean isPublicCatalog(HttpMethod method, String path) {
        return HttpMethod.GET.equals(method) && path.equals("/api/chapters/summary");
    }

    private boolean requiresAdmin(HttpMethod method, String path) {
        // Internal credentials lookup (returns the password hash) must never be
        // reachable by a regular user through the gateway.
        if (path.startsWith("/api/users/credentials")) {
            return true;
        }
        // Listing every user is an admin-only operation.
        if (HttpMethod.GET.equals(method) && path.equals("/api/users")) {
            return true;
        }
        // Deleting any user account is reserved to admins.
        if (HttpMethod.DELETE.equals(method) && path.startsWith("/api/users/")) {
            return true;
        }
        return false;
    }

    @Override
    public int getOrder() {
        return -1;
    }
}