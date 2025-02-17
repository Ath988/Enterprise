package com.bilgeadam.enterprise.config;

import com.bilgeadam.enterprise.service.ChatService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.Map;

@Component
public class WebSocketEventListener {
	
	private static final Logger logger = LoggerFactory.getLogger(WebSocketEventListener.class);
	private final ChatService chatService;
	@Autowired
	private SimpMessagingTemplate simpMessagingTemplate;
	
	
	public WebSocketEventListener(ChatService chatService) {
		this.chatService = chatService;
	}
	
	@EventListener
	public void handleWebSocketConnectListener(SessionConnectEvent event) {
		StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
		
		// 1) Read from session attributes instead of getUser()
		String userId = (String) accessor.getSessionAttributes().get("userId");
		if (userId == null) {
			userId = "unknown";
		}
		
		// 2) Mark the user online
		chatService.setUserOnlineStatus(userId, true);
		simpMessagingTemplate.convertAndSend("/topic/online-status",
		                                     Map.of("userId", userId, "status", true));
		System.out.println("SessionConnectEvent: sessionId=" + accessor.getSessionId());
		System.out.println("SessionConnectEvent: userId=" + userId);
	}
	
	
	@EventListener
	public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
		StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
		String userId = (String) accessor.getSessionAttributes().get("userId");
		if (userId != null) {
			chatService.setUserOnlineStatus(userId, false);
			logger.info("User {} disconnected", userId);
			simpMessagingTemplate.convertAndSend("/topic/online-status",
			                                     Map.of("userId", userId, "status", false));
		} else {
			logger.warn("User id not found in session attributes on disconnect");
		}
	}
	
	
}