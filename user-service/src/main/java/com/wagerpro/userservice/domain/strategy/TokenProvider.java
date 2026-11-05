package com.wagerpro.userservice.domain.strategy;

import com.wagerpro.userservice.domain.model.UserDomain;
import com.wagerpro.userservice.infrastructure.security.dto.TokenResult;

public interface TokenProvider {
	
	TokenResult createAccessToken(UserDomain user);
    
    long getAccessTokenLifetimeSeconds();
    
}
