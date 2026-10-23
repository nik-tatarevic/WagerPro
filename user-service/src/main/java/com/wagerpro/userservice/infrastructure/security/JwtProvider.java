package com.wagerpro.userservice.infrastructure.security;

import com.wagerpro.userservice.domain.model.UserDomain;
import com.wagerpro.userservice.domain.strategy.TokenProvider;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;

@Component
public class JwtProvider implements TokenProvider {

    private final SecretKey signingKey;
    private final long expirationMillis;

    public JwtProvider(
            @Value("${security.jwt.secret}") String base64Secret,
            @Value("${security.jwt.expiration-ms:3600000}") long expirationMillis) {
        byte[] keyBytes = Decoders.BASE64.decode(base64Secret);
        this.signingKey = Keys.hmacShaKeyFor(keyBytes);
        if (expirationMillis <= 0) {
            throw new IllegalArgumentException("JWT expiration must be greater than zero.");
        }
        this.expirationMillis = expirationMillis;
    }

    @Override
    public String createAccessToken(UserDomain user) {
        Instant issuedAt = Instant.now();
        Instant expiresAt = issuedAt.plusMillis(expirationMillis);

        return Jwts.builder()
                .issuer("wagerpro-user-service")
                .subject(user.id().toString())
                .claim("username", user.username())
                .claim("role", user.role().authority())
                .issuedAt(Date.from(issuedAt))
                .expiration(Date.from(expiresAt))
                .signWith(signingKey)
                .compact();
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
