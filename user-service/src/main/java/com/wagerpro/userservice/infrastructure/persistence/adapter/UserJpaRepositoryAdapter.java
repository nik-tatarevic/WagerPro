package com.wagerpro.userservice.infrastructure.persistence.adapter;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.wagerpro.userservice.domain.model.UserDomain;
import com.wagerpro.userservice.domain.repository.UserDomainRepository;
import com.wagerpro.userservice.infrastructure.persistence.entity.User;
import com.wagerpro.userservice.infrastructure.persistence.mapper.UserPersistenceMapper;
import com.wagerpro.userservice.infrastructure.persistence.repository.UserJpaRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class UserJpaRepositoryAdapter implements UserDomainRepository {

	private final UserJpaRepository userJpaRepository;
    private final UserPersistenceMapper persistenceMapper;
	
    @Override
    public UserDomain save(UserDomain user) {
        User entity = persistenceMapper.toEntity(user);
        return persistenceMapper.toDomain(userJpaRepository.save(entity));
    }

    @Override
    public Optional<UserDomain> findById(Long id) {
        return userJpaRepository.findById(id).map(persistenceMapper::toDomain);
    }

    @Override
    public Optional<UserDomain> findByUsername(String username) {
        return userJpaRepository.findByUsernameIgnoreCase(username).map(persistenceMapper::toDomain);
    }

    @Override
    public Optional<UserDomain> findByUsernameOrEmail(String identifier) {
        return userJpaRepository.findByUsernameIgnoreCaseOrEmailIgnoreCase(identifier, identifier)
                .map(persistenceMapper::toDomain);
    }

    @Override
    public boolean existsByUsername(String username) {
        return userJpaRepository.existsByUsernameIgnoreCase(username);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userJpaRepository.existsByEmailIgnoreCase(email);
    }
}
