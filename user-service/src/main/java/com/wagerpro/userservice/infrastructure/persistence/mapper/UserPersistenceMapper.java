package com.wagerpro.userservice.infrastructure.persistence.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import com.wagerpro.userservice.domain.model.Role;
import com.wagerpro.userservice.domain.model.UserDomain;
import com.wagerpro.userservice.infrastructure.persistence.entity.User;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface UserPersistenceMapper {

    @Mapping(target = "hashedPassword", source = "password")
    @Mapping(target = "role", source = "role")
    UserDomain toDomain(User user);

    @Mapping(target = "password", source = "hashedPassword")
    @Mapping(target = "role", expression = "java(domain.role().authority())")
    User toEntity(UserDomain domain);

    default Role mapRole(String role) {
        return Role.fromAuthority(role);
    }
}
