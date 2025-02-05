package com.bilgeadam.enterprise.utility;

import com.bilgeadam.enterprise.dto.request.NewMessageDto;
import com.bilgeadam.enterprise.dto.response.NewMessageResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import static com.bilgeadam.enterprise.config.RabbitMQConfig.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
@RequiredArgsConstructor
public class MessageProducer {
	
	private final RabbitTemplate rabbitTemplate;
	private final ObjectMapper objectMapper;
	
	public void sendMessage(NewMessageResponseDto messageDto, String routingKey) {
		try {
			String jsonMessage = objectMapper.writeValueAsString(messageDto);
			rabbitTemplate.convertAndSend(MESSAGE_EXCHANGE, routingKey, jsonMessage);
			System.out.println("✅ RabbitMQ'ya JSON mesaj gönderildi: " + messageDto.content());
		} catch (JsonProcessingException e) {
			throw new RuntimeException("❌ JSON dönüşüm hatası: " + e.getMessage(), e);
		}
	}
}