package com.bilgeadam.enterprise.config;

import com.bilgeadam.enterprise.exception.EnterpriseException;
import com.bilgeadam.enterprise.exception.ErrorType;
import com.bilgeadam.enterprise.utility.JwtManager;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import java.util.List;
import java.util.Map;

@Component
public class WebSocketAuthInterceptor implements ChannelInterceptor {
	private final JwtManager jwtManager;
	
	public WebSocketAuthInterceptor(JwtManager jwtManager) {
		this.jwtManager = jwtManager;
	}
	
	@Override
	public Message<?> preSend(Message<?> message, MessageChannel channel) {
		StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
		
		if (StompCommand.CONNECT.equals(accessor.getCommand())) {
			String sessionId = accessor.getSessionId();
			System.out.println("ChannelInterceptor: sessionId=" + sessionId);
			
			
			String token = accessor.getFirstNativeHeader("Authorization");
			
			if (token == null || !token.startsWith("Bearer ")) {
				throw new EnterpriseException(ErrorType.USER_NOT_AUTHORIZED, "Authorization header is missing or invalid!");
			}
			
			Long userId = jwtManager.validateToken(token.replace("Bearer ", ""))
			                          .orElseThrow(() -> new EnterpriseException(ErrorType.USER_NOT_AUTHORIZED));
			System.out.println("METODA GIRIYOR MU KONTROLU!");
			System.out.println("USER ID:" + userId);
			
			UsernamePasswordAuthenticationToken authentication =
					new UsernamePasswordAuthenticationToken(userId, null, List.of());
			
			accessor.setUser(authentication);
			accessor.getSessionAttributes().put("userId", userId);
			SecurityContextHolder.getContext().setAuthentication(authentication);
		}
		
		return message;
	}

	
	
}