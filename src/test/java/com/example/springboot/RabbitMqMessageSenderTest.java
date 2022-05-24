package com.example.springboot;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class RabbitMqMessageSenderTest {

    @MockBean
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private Queue rfaUploadedQueue;

    @Autowired
    private RabbitMqMessageSender rabbitMqMessageSender;

    @Test
    void shouldSendRfaUploaded() {
        // Given
        Long id = 1L;
        
        // When
        rabbitMqMessageSender.publishRfaUploadedEvent(id);

        // Then
        ArgumentCaptor<RfaUploadedEvent> captor = ArgumentCaptor.forClass(RfaUploadedEvent.class);
        Mockito.verify(rabbitTemplate).convertAndSend(Mockito.eq(rfaUploadedQueue.getName()), captor.capture());
        assertEquals(id, captor.getValue().getId());
    }

}
