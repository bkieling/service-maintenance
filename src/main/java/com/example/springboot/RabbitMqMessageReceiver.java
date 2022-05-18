package com.example.springboot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

@RabbitListener(queues = {"rfa.uploaded"})
public class RabbitMqMessageReceiver {
    Logger logger = LoggerFactory.getLogger(RabbitMqMessageReceiver.class);

    @RabbitHandler
    public void receive(RfaUploadedEvent rfaUploadedEvent) {
        logger.info("Received RFA uploaded. Id = '" + rfaUploadedEvent.getId() + "'");
    }


}
