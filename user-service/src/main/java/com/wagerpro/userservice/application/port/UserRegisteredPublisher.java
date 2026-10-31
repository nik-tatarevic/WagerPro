package com.wagerpro.userservice.application.port;

import com.wagerpro.userservice.domain.model.UserDomain;

public interface UserRegisteredPublisher {
	
    void publish(UserDomain user);
    
}