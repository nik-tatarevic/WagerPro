package com.wagerpro.userservice.infrastructure.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.wagerpro.userservice.infrastructure.redis.UserSessionRedisAdapter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final CustomUserDetailsService userDetailsService;
    private final UserSessionRedisAdapter userSessionRedisService;

    public JwtFilter(JwtProvider jwtProvider, 
		    		 CustomUserDetailsService userDetailsService, 
		    		 UserSessionRedisAdapter userSessionRedisService) {
        this.jwtProvider = jwtProvider;
        this.userDetailsService = userDetailsService;
        this.userSessionRedisService = userSessionRedisService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
						             HttpServletResponse response,
						             FilterChain filterChain) throws ServletException, IOException {

        String authorization = request.getHeader("Authorization");
        if (authorization != null && authorization.startsWith("Bearer ")
                && SecurityContextHolder.getContext().getAuthentication() == null) {
            String token = authorization.substring(7).trim();
            try {
                if (jwtProvider.isTokenValid(token)) {
                	Claims claims = jwtProvider.parseClaims(token);
                	Long userId = Long.valueOf(claims.getSubject());
                    UserDetails userDetails = userDetailsService.loadUserById(userId);

                    if (userDetails.isEnabled()) {
                        UsernamePasswordAuthenticationToken authentication =
                                new UsernamePasswordAuthenticationToken(
                                        userDetails, null, userDetails.getAuthorities());
                        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                        
                        userSessionRedisService.refreshSessionIfExists(claims.getId());
                    }
                }
                
            } catch (JwtException | IllegalArgumentException | UsernameNotFoundException exception) {
                // Invalid, expired, or revoked-by-account-state tokens remain unauthenticated.
                SecurityContextHolder.clearContext();
            }
        }

        filterChain.doFilter(request, response);
    }
}
