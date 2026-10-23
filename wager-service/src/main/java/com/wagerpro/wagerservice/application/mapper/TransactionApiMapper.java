package com.wagerpro.wagerservice.application.mapper;

import org.mapstruct.Mapper;

import com.wagerpro.wagerservice.application.dto.request.CreateTransactionRequestDto;
import com.wagerpro.wagerservice.domain.model.TransactionDomain;

@Mapper(componentModel = "spring")
public interface TransactionApiMapper {

	TransactionDomain toDomain(CreateTransactionRequestDto request);
	
	
}
