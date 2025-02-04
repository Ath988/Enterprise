package com.bilgeadam.enterprise.utility;

import com.bilgeadam.enterprise.dto.request.NewMessageDto;
import com.bilgeadam.enterprise.dto.response.NewMessageResponseDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessageConsumer {
	
	private final SimpMessagingTemplate messagingTemplate;
	private final ObjectMapper objectMapper;
	
	@RabbitListener(queues = "private.queue")
	public void consumePrivateMessage(String jsonMessage) {
		try {
			NewMessageResponseDto messageDto = objectMapper.readValue(jsonMessage, NewMessageResponseDto.class);
			System.out.println("📥 Private Mesaj Alındı: " + messageDto.content());
			messagingTemplate.convertAndSend("/topic/private/" + messageDto.chatId(), messageDto);
		} catch (JsonProcessingException e) {
			System.err.println("❌ JSON parse hatası (Private): " + e.getMessage());
		}
	}
	
	@RabbitListener(queues = "group.queue")
	public void consumeGroupMessage(String jsonMessage) {
		try {
			NewMessageResponseDto messageDto = objectMapper.readValue(jsonMessage, NewMessageResponseDto.class);
			System.out.println("📥 Group Mesaj Alındı: " + messageDto.content());
			messagingTemplate.convertAndSend("/topic/group/" + messageDto.chatId(), messageDto);
		} catch (JsonProcessingException e) {
			System.err.println("❌ JSON parse hatası (Group): " + e.getMessage());
		}
	}
}