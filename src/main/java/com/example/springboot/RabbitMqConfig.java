package com.example.springboot;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

    @Bean
    public Queue rfaUploadedQueue() {
        return new Queue("rfa.uploaded");
    }

}