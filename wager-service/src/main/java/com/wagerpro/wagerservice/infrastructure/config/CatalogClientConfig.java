package com.wagerpro.wagerservice.infrastructure.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import com.wagerpro.wagerservice.infrastructure.rest.CatalogServiceClient;

@Configuration
public class CatalogClientConfig {
	
	@Bean
	public CatalogServiceClient catalogServiceClient(@Value("${services.catalog-service.url}") String url) {

	    RestClient restClient = RestClient.builder()
	            .baseUrl(url)
	            .requestInterceptor((request, body, execution) -> {

	                ServletRequestAttributes attributes =
	                        (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

	                if (attributes != null) {
	                    String authorization = attributes.getRequest().getHeader(HttpHeaders.AUTHORIZATION);

	                    if (authorization != null) {
	                        request.getHeaders().set(HttpHeaders.AUTHORIZATION, authorization);
	                    }
	                }

	                return execution.execute(request, body);
	                
	            }).build();

	    HttpServiceProxyFactory factory =
	            HttpServiceProxyFactory.builderFor(RestClientAdapter.create(restClient))
	                    .build();

	    return factory.createClient(CatalogServiceClient.class);
	}

}
