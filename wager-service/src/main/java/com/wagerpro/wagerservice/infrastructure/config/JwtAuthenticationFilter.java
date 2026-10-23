package com.wagerpro.wagerservice.infrastructure.config;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.wagerpro.wagerservice.infrastructure.rest.UserPrincipal;

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

	@Value("${security.jwt.secret}")
	private String secret;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		String header = "Bearer " + request.getHeader(HttpHeaders.AUTHORIZATION);

		if (header == null || !header.startsWith("Bearer ")) {
			filterChain.doFilter(request, response);
			return;
		}

		String token = header.substring(7);
		
		try {
			byte[] keyBytes = Decoders.BASE64.decode(secret);
			Claims claims = Jwts.parser()
					.verifyWith(Keys.hmacShaKeyFor(keyBytes))
					.build()
					.parseSignedClaims(token)
					.getPayload();
			
            Long userId = Long.valueOf(claims.getSubject());
			String username = String.valueOf(claims.get("username"));
			Object rolesObj = claims.get("role");
			List<SimpleGrantedAuthority> authorities = Collections.emptyList();

			if (rolesObj instanceof List<?>) {
			    authorities = ((List<?>) rolesObj).stream()
			            .filter(Objects::nonNull)
			            .map(Object::toString)
			            .map(SimpleGrantedAuthority::new)
			            .toList();
			}

			UserPrincipal principal = new UserPrincipal(userId, username, rolesObj);

			UsernamePasswordAuthenticationToken authentication = 
					new UsernamePasswordAuthenticationToken(principal, null, authorities);

			authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

			SecurityContextHolder.getContext().setAuthentication(authentication);

		} catch (io.jsonwebtoken.ExpiredJwtException ex) {
            System.out.println("-> Greška: Token je istekao! " + ex.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        } catch (io.jsonwebtoken.security.SignatureException ex) {
            System.out.println("-> Greška: Nevalidan potpis tokena! Proveri secret ključ. " + ex.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        } catch (io.jsonwebtoken.MalformedJwtException ex) {
            System.out.println("-> Greška: Token je loše formatiran (malformed)! " + ex.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        } catch (JwtException ex) {
            System.out.println("-> Opšta JWT greška: " + ex.getClass().getName() + " - " + ex.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
			
		filterChain.doFilter(request, response);
	}
}
