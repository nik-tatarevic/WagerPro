package com.wagerpro.userservice.application.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wagerpro.userservice.application.dto.request.LoginUserRequestDto;
import com.wagerpro.userservice.application.dto.request.RegisterUserRequestDto;
import com.wagerpro.userservice.application.dto.request.UpdateUserRequestDto;
import com.wagerpro.userservice.application.dto.response.LoginResponseDto;
import com.wagerpro.userservice.application.dto.response.UserResponseDto;
import com.wagerpro.userservice.application.mapper.UserApiMapper;
import com.wagerpro.userservice.application.port.UserRegisteredPublisher;
import com.wagerpro.userservice.domain.exception.EmailAlreadyExistsException;
import com.wagerpro.userservice.domain.exception.InvalidCredentialsException;
import com.wagerpro.userservice.domain.exception.UserNotFoundException;
import com.wagerpro.userservice.domain.exception.UsernameAlreadyExistsException;
import com.wagerpro.userservice.domain.model.Role;
import com.wagerpro.userservice.domain.model.UserDomain;
import com.wagerpro.userservice.domain.repository.UserDomainRepository;
import com.wagerpro.userservice.domain.strategy.PasswordEncoderStrategy;
import com.wagerpro.userservice.domain.strategy.TokenProvider;
import com.wagerpro.userservice.domain.validation.UserValidation;
import com.wagerpro.userservice.infrastructure.redis.UserSessionRedisService;
import com.wagerpro.userservice.infrastructure.security.dto.TokenResult;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserDomainRepository userRepository;
    private final UserValidation userValidation;
    private final PasswordEncoderStrategy passwordEncoder;
    private final TokenProvider tokenProvider;
    private final UserApiMapper userMapper;
    private final UserRegisteredPublisher userRegisteredPublisher;
    private final UserSessionRedisService userSessionRedisService;

    @Transactional
    public UserResponseDto register(RegisterUserRequestDto request) {
        String username = userValidation.normalizeAndValidateUsername(request.username());
        String email = userValidation.normalizeAndValidateEmail(request.email());
        userValidation.validatePassword(request.password());

        if (userRepository.existsByUsername(username)) {
            throw new UsernameAlreadyExistsException(username);
        }
        if (userRepository.existsByEmail(email)) {
            throw new EmailAlreadyExistsException(email);
        }

        UserDomain userDomain = new UserDomain(
                null,
                username,
                email,
                passwordEncoder.encode(request.password()),
                Role.USER,
                true,
                null,
                null);

        UserDomain savedUserDomain = userRepository.save(userDomain);
        userRegisteredPublisher.publish(savedUserDomain);
        return userMapper.toResponse(savedUserDomain);
    }

    @Transactional(readOnly = true)
    public LoginResponseDto login(LoginUserRequestDto request) {
        String identifier = userValidation.normalizeAndValidateIdentifier(request.identifier());
        UserDomain user = userRepository.findByUsernameOrEmail(identifier).orElse(null);

        boolean passwordMatches = user != null
                && passwordEncoder.matches(request.password(), user.hashedPassword());
        if (user == null || !user.enabled() || !passwordMatches) {
            throw new InvalidCredentialsException();
        }

        TokenResult tokenResult = tokenProvider.createAccessToken(user);
        
        userSessionRedisService.createSession(tokenResult.jti(), user.username());
        
        return new LoginResponseDto(tokenResult.accessToken(), 
					        		"Bearer", 
					        		tokenProvider.getAccessTokenLifetimeSeconds(),
					                userMapper.toResponse(user));
    }

    @Transactional(readOnly = true)
    public UserResponseDto getByUsername(String username) {
        String normalizedUsername = userValidation.normalizeAndValidateIdentifier(username);
        return userRepository.findByUsernameOrEmail(normalizedUsername)
                .map(userMapper::toResponse)
                .orElseThrow(() -> new UserNotFoundException("Current user was not found."));
    }

    @Transactional(readOnly = true)
    public UserResponseDto getById(Long id) {
        return userRepository.findById(id)
                .map(userMapper::toResponse)
                .orElseThrow(() -> new UserNotFoundException("User with id " + id + " was not found."));
    }

    @Transactional
    public UserResponseDto update(String currentUsername, UpdateUserRequestDto request) {
        String normalizedCurrentUsername = userValidation.normalizeAndValidateIdentifier(currentUsername);
        UserDomain persistedUser = userRepository.findByUsernameOrEmail(normalizedCurrentUsername)
                .orElseThrow(() -> new UserNotFoundException("Current user was not found."));

        String username = persistedUser.username();
        if (request.username() != null && !request.username().isBlank()) {
            username = userValidation.normalizeAndValidateUsername(request.username());
            if (!username.equals(persistedUser.username()) && userRepository.existsByUsername(username)) {
                throw new UsernameAlreadyExistsException(username);
            }
        }

        String email = persistedUser.email();
        if (request.email() != null && !request.email().isBlank()) {
            email = userValidation.normalizeAndValidateEmail(request.email());
            if (!email.equals(persistedUser.email()) && userRepository.existsByEmail(email)) {
                throw new EmailAlreadyExistsException(email);
            }
        }

        String hashedPassword = persistedUser.hashedPassword();
        if (request.newPassword() != null && !request.newPassword().isBlank()) {
//        	userValidation.compareNewAndOldPassword(request.newPassword(), persistedUser.hashedPassword(), passwordEncoder);
            userValidation.validatePassword(request.newPassword());
            hashedPassword = passwordEncoder.encode(request.newPassword());
        }

        UserDomain updated = new UserDomain(
                persistedUser.id(), username, email, hashedPassword, persistedUser.role(), persistedUser.enabled(),
                persistedUser.createdAt(), LocalDateTime.now());
        return userMapper.toResponse(userRepository.save(updated));
    }

	public void logout(HttpServletRequest request) {
	 	String bearerToken = request.getHeader("Authorization");
	        
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            String token = bearerToken.substring(7);
            userSessionRedisService.deleteSession(token);
        }
	}
	
}
