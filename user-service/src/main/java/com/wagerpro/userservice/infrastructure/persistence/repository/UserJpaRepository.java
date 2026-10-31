package com.wagerpro.userservice.infrastructure.persistence.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wagerpro.userservice.infrastructure.persistence.entity.User;

public interface UserJpaRepository extends JpaRepository<User, Long> {
	
	Optional<User> findByUsernameIgnoreCase(String username);
	
    Optional<User> findByUsernameIgnoreCaseOrEmailIgnoreCase(String username, String email);
    
    boolean existsByUsernameIgnoreCase(String username);
    
    boolean existsByEmailIgnoreCase(String email);
}
