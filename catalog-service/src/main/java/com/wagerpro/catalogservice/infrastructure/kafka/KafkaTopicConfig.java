package com.wagerpro.catalogservice.infrastructure.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
//@ConditionalOnProperty(prefix = "app.kafka", name = "enabled", havingValue = "true")
public class KafkaTopicConfig {
	
	@Bean
	public NewTopic statusChangedTopic(
		@Value("${app.kafka.topic.match-status-changed}") String topicName) {
		
		return TopicBuilder.name(topicName).partitions(3).replicas(1).build();
	}

}
