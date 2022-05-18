package com.example.springboot;

import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class RabbitMqMessageSender {

    private final RabbitTemplate rabbitTemplate;
    private final FanoutExchange fanout;

    public RabbitMqMessageSender(RabbitTemplate rabbitTemplate, FanoutExchange fanoutExchange) {
        this.rabbitTemplate = rabbitTemplate;
        this.fanout = fanoutExchange;
    }

    public void publishRfaUploadedEvent(Long id){
        RfaUploadedEvent rfaUploadedEvent = new RfaUploadedEvent();
        rfaUploadedEvent.setId(id);
        rabbitTemplate.convertAndSend(fanout.getName(), "", rfaUploadedEvent);
    }

}
