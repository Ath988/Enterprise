package com.bilgeadam.enterprise.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
	
	@Override
	public void configureMessageBroker(MessageBrokerRegistry config) {
		config.enableSimpleBroker("/topic"); // Mesajların yayınlanacağı hedef
		config.setApplicationDestinationPrefixes("/app"); // Mesajların işleneceği prefix
	}
	
	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry.addEndpoint("/ws") // WebSocket endpoint’i
		        .setAllowedOrigins("http://localhost:3000") // Kimlere açık olduğunu belirtir.
		        .addInterceptors(new WebSocketAuthInterceptor()) //Kimlik doğrulama
		        .withSockJS(); // Tarayıcı uyumluluğu için SockJS kullanımı, eski tarayıcılarda websocket desteği
		// olmayabiliyor.
	}
}