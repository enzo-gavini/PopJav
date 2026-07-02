package com.enzo.apigateway.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Service
public class JwtService {
    @Value("${jwt.secret}")
    private String secretKey;


    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean isAdmin(String token) {
        try {
            Object role = extractAllClaims(token).get("role");
            return role != null && role.toString().contains("ADMIN");
        } catch (Exception e) {
            return false;
        }
    }

    public String extractUserId(String token) {
        try {
            Object userId = extractAllClaims(token).get("userId");
            return userId != null ? userId.toString() : null;
        } catch (Exception e) {
            return null;
        }
    }

    public String extractRole(String token) {
        return isAdmin(token) ? "ADMIN" : "USER";
    }

    public boolean validateToken(String token) {
        try {
            Claims claims = extractAllClaims(token);
            return claims.getExpiration().after(new Date());
        } catch (Exception e) {
            return false;
        }
    }

}
