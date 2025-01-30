package com.bilgeadam.enterprise.utility;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import static com.bilgeadam.enterprise.config.RabbitMQConfig.*;

@Service
@RequiredArgsConstructor
public class MessageProducer {
	
	private final RabbitTemplate rabbitTemplate;
	
	public void sendMessage(String message) {
		rabbitTemplate.convertAndSend(MESSAGE_EXCHANGE, ROUTING_KEY, message);
		System.out.println("RabbitMQ'ya mesaj gönderildi: " + message);
	}
}