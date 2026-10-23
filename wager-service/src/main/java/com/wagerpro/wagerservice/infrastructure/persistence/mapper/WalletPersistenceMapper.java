package com.wagerpro.wagerservice.infrastructure.persistence.mapper;

import org.mapstruct.Mapper;

import com.wagerpro.wagerservice.domain.model.WalletDomain;
import com.wagerpro.wagerservice.infrastructure.persistence.entity.Wallet;

@Mapper(componentModel = "spring")
public interface WalletPersistenceMapper {
	
	Wallet toEntity(WalletDomain walletDomain);
	WalletDomain toDomain(Wallet wallet);

}
