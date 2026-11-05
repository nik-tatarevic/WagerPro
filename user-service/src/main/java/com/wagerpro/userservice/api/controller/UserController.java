package com.wagerpro.userservice.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wagerpro.userservice.application.dto.request.LoginUserRequestDto;
import com.wagerpro.userservice.application.dto.request.RegisterUserRequestDto;
import com.wagerpro.userservice.application.dto.request.UpdateUserRequestDto;
import com.wagerpro.userservice.application.dto.response.LoginResponseDto;
import com.wagerpro.userservice.application.dto.response.UserResponseDto;
import com.wagerpro.userservice.application.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {
	
	private final UserService userService;

	@PostMapping("/register")
    public ResponseEntity<UserResponseDto> register(@Valid @RequestBody RegisterUserRequestDto request) {
		UserResponseDto response = userService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public LoginResponseDto login(@Valid @RequestBody LoginUserRequestDto request) {
        return userService.login(request);
    }

    @GetMapping("/me")
    public UserResponseDto getCurrentUser(Authentication authentication) {
        return userService.getByUsername(authentication.getName());
    }

    @PutMapping("/update")
    public UserResponseDto updateCurrentUser(
            Authentication authentication,
            @Valid @RequestBody UpdateUserRequestDto request) {
        return userService.update(authentication.getName(), request);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public UserResponseDto getById(@PathVariable Long id) {
        return userService.getById(id);
    }
    
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request) {
    	userService.logout(request);
        return ResponseEntity.ok().build();
    }
	
}
