package com.wagerpro.wagerservice.application.mapper;

import org.mapstruct.Mapper;

import com.wagerpro.wagerservice.application.dto.response.WalletResponseDto;
import com.wagerpro.wagerservice.domain.model.WalletDomain;

@Mapper(componentModel = "spring")
public interface WalletApiMapper {
	

	WalletResponseDto toResponseDto(WalletDomain walletDomain);

}
