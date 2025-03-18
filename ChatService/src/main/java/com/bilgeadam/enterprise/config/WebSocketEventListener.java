package com.bilgeadam.enterprise.config;

import com.bilgeadam.enterprise.manager.UserManagementManager;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class WebSocketEventListener {
	
	private static final Logger logger = LoggerFactory.getLogger(WebSocketEventListener.class);
	private final SimpMessagingTemplate simpMessagingTemplate;
	private final UserManagementManager manager;
	
	
	@EventListener
	public void handleWebSocketConnectListener(SessionConnectEvent event) {
		StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
		
		// 1) Read from session attributes instead of getUser()
		Long userId = (Long) accessor.getSessionAttributes().get("userId");
		if (userId == null) {
			userId = 1L;
		}
		
		// 2) Mark the user online
		try {
			manager.setUsersOnlineStatus(userId, true);
			simpMessagingTemplate.convertAndSend("/topic/online-status",
			                                     Map.of("userId", userId, "status", true));
		} catch (Exception e) {
			logger.error("Failed to update online status for user {}: {}", userId, e.getMessage());
		}
		System.out.println("SessionConnectEvent: sessionId=" + accessor.getSessionId());
		System.out.println("SessionConnectEvent: userId=" + userId);
	}
	
	
	@EventListener
	public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
		StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
		Long userId = (Long) accessor.getSessionAttributes().get("userId");
		if (userId != null) {
			try {
				manager.setUsersOnlineStatus(userId, false);
				simpMessagingTemplate.convertAndSend("/topic/online-status",
				                                     Map.of("userId", userId, "status", false));
			} catch (Exception e) {
				logger.error("Failed to update online status for user {}: {}", userId, e.getMessage());
			}
		} else {
			logger.debug("User ID not found in session attributes on disconnect.");
		}
	}
	
	
}