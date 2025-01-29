package com.bilgeadam.enterprise.utility;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import static com.bilgeadam.enterprise.config.RabbitMQConfig.MESSAGE_QUEUE;

@Service
@RequiredArgsConstructor
public class MessageConsumer {
	
	@RabbitListener(queues = MESSAGE_QUEUE)
	public void consumeMessage(String message) {
		System.out.println("RabbitMQ'dan mesaj alındı: " + message);
	}
}