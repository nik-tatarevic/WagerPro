package com.wagerpro.catalogservice.infrastructure.security;

import java.io.IOException;
import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.wagerpro.catalogservice.infrastructure.rest.UserPrincipal;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
	
	private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
	private final StringRedisTemplate redisTemplate;
	private static final String SESSION_PREFIX = "session:jti:";
	private static final Duration SESSION_TTL = Duration.ofMinutes(60);

	@Value("${security.jwt.secret}")
	private String secret;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		String token = request.getHeader(HttpHeaders.AUTHORIZATION);

	    if (token == null) {
	        filterChain.doFilter(request, response);
	        return;
	    }
		
		try {
			byte[] keyBytes = Decoders.BASE64.decode(secret);
			Claims claims = Jwts.parser()
					.verifyWith(Keys.hmacShaKeyFor(keyBytes))
					.build()
					.parseSignedClaims(token)
					.getPayload();
			
			// id -> jti
			String redisKey = SESSION_PREFIX + claims.getId();
	        Boolean hasKey = redisTemplate.expire(redisKey, SESSION_TTL);
	        
	        if (!hasKey) {
	        	handleUnauthorized(response, "Session expired or user logged out.");
	            return;
	        }
			
            Long userId = Long.valueOf(claims.getSubject());
			String username = String.valueOf(claims.get("username"));
			Object rolesObj = claims.get("role");
			List<SimpleGrantedAuthority> authorities = parseAuthorities(rolesObj);

			UserPrincipal principal = new UserPrincipal(userId, username, rolesObj);
			UsernamePasswordAuthenticationToken authentication = 
					new UsernamePasswordAuthenticationToken(principal, null, authorities);

			authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

			SecurityContextHolder.getContext().setAuthentication(authentication);

		} catch (io.jsonwebtoken.ExpiredJwtException ex) {
            handleUnauthorized(response, "JWT token is expired.");
            return;
        } catch (io.jsonwebtoken.security.SignatureException ex) {
            handleUnauthorized(response, "Invalid JWT signature.");
            return;
        } catch (io.jsonwebtoken.MalformedJwtException ex) {
            handleUnauthorized(response, "Malformed JWT token.");
            return;
        } catch (JwtException ex) {
        	log.error("JWT validation error: {} - {}", ex.getClass().getName(), ex.getMessage());
            handleUnauthorized(response, "Invalid JWT token.");
            return;
        }
			
		filterChain.doFilter(request, response);
	}
	
	private List<SimpleGrantedAuthority> parseAuthorities(Object rolesObj) {
	    if (rolesObj instanceof List<?>) {
	        return ((List<?>) rolesObj).stream()
	                .filter(Objects::nonNull)
	                .map(Object::toString)
	                .map(SimpleGrantedAuthority::new)
	                .toList();
	    }
	    return Collections.emptyList();
	}
	
	private void handleUnauthorized(HttpServletResponse response, String message) throws IOException {
	    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
	    response.getWriter().write(message);
	}
	
}
