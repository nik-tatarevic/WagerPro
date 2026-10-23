package com.wagerpro.userservice.domain.strategy;

import com.wagerpro.userservice.domain.model.UserDomain;

public interface TokenProvider {
    String createAccessToken(UserDomain user);
    long getAccessTokenLifetimeSeconds();
}
