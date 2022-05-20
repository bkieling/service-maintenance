package com.example.springboot;

import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class RabbitMqMessageSender {

    private final RabbitTemplate rabbitTemplate;
    private final Queue rfaUploadedQueue;
//    private final FanoutExchange fanout;

    public RabbitMqMessageSender(RabbitTemplate rabbitTemplate, Queue rfaUploadedQueue) {
        this.rabbitTemplate = rabbitTemplate;
//        this.fanout = fanoutExchange;
        this.rfaUploadedQueue = rfaUploadedQueue;
    }

    public void publishRfaUploadedEvent(Long id){
        RfaUploadedEvent rfaUploadedEvent = new RfaUploadedEvent();
        rfaUploadedEvent.setId(id);
        rabbitTemplate.convertAndSend(rfaUploadedQueue.getName(), rfaUploadedEvent);
    }

}
