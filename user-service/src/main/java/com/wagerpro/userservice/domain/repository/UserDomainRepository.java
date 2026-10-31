package com.wagerpro.userservice.domain.repository;

import java.util.Optional;

import com.wagerpro.userservice.domain.model.UserDomain;

public interface UserDomainRepository {

	UserDomain save(UserDomain user);

	Optional<UserDomain> findById(Long id);

	Optional<UserDomain> findByUsername(String username);

	Optional<UserDomain> findByUsernameOrEmail(String identifier);

	boolean existsByUsername(String username);

	boolean existsByEmail(String email);
}
