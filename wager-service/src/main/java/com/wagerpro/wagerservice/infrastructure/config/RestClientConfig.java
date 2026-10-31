package com.wagerpro.wagerservice.infrastructure.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import com.wagerpro.wagerservice.infrastructure.rest.UserServiceClient;

@Configuration
public class RestClientConfig {

    @Bean
    UserServiceClient userServiceClient(@Value("${services.user-service.url}") String url) {
        RestClient restClient = RestClient.builder()
                .baseUrl(url)
                .build();

        HttpServiceProxyFactory factory =
                HttpServiceProxyFactory.builderFor(
                        RestClientAdapter.create(restClient))
                        .build();

        return factory.createClient(UserServiceClient.class);
    }
    
}
