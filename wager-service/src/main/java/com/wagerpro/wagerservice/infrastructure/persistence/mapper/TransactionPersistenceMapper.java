package com.wagerpro.wagerservice.infrastructure.persistence.mapper;

import org.mapstruct.Mapper;

import com.wagerpro.wagerservice.domain.model.TransactionDomain;
import com.wagerpro.wagerservice.infrastructure.persistence.entity.Transaction;

@Mapper(componentModel = "spring")
public interface TransactionPersistenceMapper {
	
	Transaction toEntity(TransactionDomain transactionDomain);
	
	TransactionDomain toDomain(Transaction transaction);
}
