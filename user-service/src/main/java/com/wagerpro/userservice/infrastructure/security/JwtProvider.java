package com.wagerpro.userservice.infrastructure.security;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.wagerpro.userservice.domain.model.UserDomain;
import com.wagerpro.userservice.domain.strategy.TokenProvider;
import com.wagerpro.userservice.infrastructure.security.dto.TokenResult;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtProvider implements TokenProvider {

    private final SecretKey signingKey;
    private final long expirationMillis;

    public JwtProvider(
            @Value("${security.jwt.secret}") String base64Secret,
            @Value("${security.jwt.expiration-ms}") long expirationMillis) {
        byte[] keyBytes = Decoders.BASE64.decode(base64Secret);
        this.signingKey = Keys.hmacShaKeyFor(keyBytes);
        if (expirationMillis <= 0) {
            throw new IllegalArgumentException("JWT expiration must be greater than zero.");
        }
        this.expirationMillis = expirationMillis;
    }

    @Override
    public TokenResult createAccessToken(UserDomain user) {
        Instant issuedAt = Instant.now();
        Instant expiresAt = issuedAt.plusMillis(expirationMillis);
        // create jti to store in redis instead of token
        String jti = UUID.randomUUID().toString();

        String accessToken = Jwts.builder()
        		.id(jti)
                .issuer("wagerpro-user-service")
                .subject(user.id().toString())
                .claim("username", user.username())
                .claim("role", user.role().authority())
                .issuedAt(Date.from(issuedAt))
                .expiration(Date.from(expiresAt))
                .signWith(signingKey)
                .compact();
        
        return new TokenResult(accessToken, jti, expiresAt);
    }

    public Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(signingKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public Long extractUserId(String token) {
        return Long.valueOf(parseClaims(token).getSubject());
    }

    public boolean isTokenValid(String token) {
        try {
            Claims claims = parseClaims(token);
            return claims.getSubject() != null && !claims.getSubject().isBlank()
                    && "wagerpro-user-service".equals(claims.getIssuer());
        } catch (io.jsonwebtoken.JwtException | IllegalArgumentException exception) {
            return false;
        }
    }

    @Override
    public long getAccessTokenLifetimeSeconds() {
        return expirationMillis / 1000;
    }
}
