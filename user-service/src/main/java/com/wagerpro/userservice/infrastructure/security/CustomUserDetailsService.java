package com.wagerpro.userservice.infrastructure.security;

import com.wagerpro.userservice.domain.model.UserDomain;
import com.wagerpro.userservice.domain.repository.UserDomainRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserDomainRepository userRepository;

    public CustomUserDetailsService(UserDomainRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        return userRepository.findByUsernameOrEmail(usernameOrEmail.strip().toLowerCase(Locale.ROOT))
                .map(this::toUserDetails)
                .orElseThrow(() -> new UsernameNotFoundException("User account was not found."));
    }

    public UserDetails loadUserById(Long userId) throws UsernameNotFoundException {
        return userRepository.findById(userId)
                .map(this::toUserDetails)
                .orElseThrow(() -> new UsernameNotFoundException("User account was not found."));
    }

    private UserDetails toUserDetails(UserDomain user) {
        return User.withUsername(user.username())
                .password(user.hashedPassword())
                .authorities(user.role().authority())
                .disabled(!user.enabled())
                .build();
    }
}
