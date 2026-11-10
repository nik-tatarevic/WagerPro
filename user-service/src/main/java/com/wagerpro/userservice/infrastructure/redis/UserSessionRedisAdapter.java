package com.wagerpro.userservice.infrastructure.redis;

import java.time.Duration;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserSessionRedisAdapter {
	
	private final StringRedisTemplate redisTemplate;
	private static final String SESSION_PREFIX = "session:jti:";
    private static final Duration SESSION_TTL = Duration.ofMinutes(60);
	
	public void createSession(String jti, String username) {
		String key = SESSION_PREFIX + jti;
		redisTemplate.opsForValue().set(key, username, SESSION_TTL);
	}
	
	public boolean refreshSessionIfExists(String jti) {
		String key = SESSION_PREFIX + jti;
		return Boolean.TRUE.equals(redisTemplate.expire(key, SESSION_TTL));
	}
	
	public void deleteSession(String jti) {
        String key = SESSION_PREFIX + jti;
        redisTemplate.delete(key);
    }

}
