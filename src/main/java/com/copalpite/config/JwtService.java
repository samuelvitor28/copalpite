package com.copalpite.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    private SecretKey getKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String gerarToken(Long usuarioId, String username) {
        return Jwts.builder()
                .subject(username)
                .claim("id", usuarioId)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getKey())
                .compact();
    }

    public Claims extrairClaims(String token) {
        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String extrairUsername(String token) {
        return extrairClaims(token).getSubject();
    }

    public Long extrairId(String token) {
        return extrairClaims(token).get("id", Long.class);
    }

    public boolean tokenValido(String token) {
        try {
            return extrairClaims(token).getExpiration().after(new Date());
        } catch (Exception e) {
            return false;
        }
    }
}