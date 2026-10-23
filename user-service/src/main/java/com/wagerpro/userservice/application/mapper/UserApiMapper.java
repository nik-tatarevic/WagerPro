package com.wagerpro.userservice.application.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import com.wagerpro.userservice.application.dto.response.UserResponseDto;
import com.wagerpro.userservice.domain.model.UserDomain;

@Mapper(componentModel = "spring")
public interface UserApiMapper {

	// TODO proveri target, expression
	// proveri zasto nemam iz request -> toDomain
	
    @Mapping(target = "role", expression = "java(user.role().authority())")
    UserResponseDto toResponse(UserDomain user);
}
