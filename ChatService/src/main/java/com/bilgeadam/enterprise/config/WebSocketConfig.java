package com.bilgeadam.enterprise.config;

import com.bilgeadam.enterprise.utility.JwtManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
	
	private final WebSocketAuthInterceptor webSocketAuthInterceptor;
	
	public WebSocketConfig(WebSocketAuthInterceptor webSocketAuthInterceptor) {
		this.webSocketAuthInterceptor = webSocketAuthInterceptor;
	}
	
	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry.addEndpoint("/ws")
		        .setAllowedOriginPatterns("http://localhost:5173", "http://192.168.1.106:5173", "https://6239-85-107-89-21.ngrok-free.app")
		        .addInterceptors(new HttpHandshakeInterceptor());
	}
	
	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) {
		registry.enableSimpleBroker("/topic");
		registry.setApplicationDestinationPrefixes("/app");
	}
	
	@Override
	public void configureClientInboundChannel(ChannelRegistration registration) {
		registration.interceptors(webSocketAuthInterceptor);
	}
}