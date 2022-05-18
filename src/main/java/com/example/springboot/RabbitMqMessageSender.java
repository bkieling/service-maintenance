package com.example.springboot;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class RabbitMqMessageSender {

    private final RabbitTemplate rabbitTemplate;

    public RabbitMqMessageSender(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publishRfaUploadedEvent(Long id){
        RfaUploadedEvent rfaUploadedEvent = new RfaUploadedEvent();
        rfaUploadedEvent.setId(id);
        rabbitTemplate.convertAndSend("rfa.uploaded", rfaUploadedEvent);
    }

}
