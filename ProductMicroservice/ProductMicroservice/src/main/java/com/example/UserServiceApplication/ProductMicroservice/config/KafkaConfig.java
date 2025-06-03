package com.example.UserServiceApplication.ProductMicroservice.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {

    @Bean  // Marks this method as a Spring Bean, so it is managed by the Spring container
    public NewTopic logsTopic() {
        return TopicBuilder.name("application-logs")  // Creates a topic named "application-logs"
                .partitions(3)  // Defines the topic with 3 partitions for parallel processing
                .replicas(1)  // 1 leader + 2 follower(in case of 3 replicas) (safer in case of failure)
                .build();  // Builds and returns the topic configuration
    }

}