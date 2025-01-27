package com.bilgeadam.enterprise.config;

import com.bilgeadam.enterprise.utility.JwtManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry.addEndpoint("/ws").setAllowedOrigins("*"); // WEBSOCKET'A BAGLANMAK ICIN GEREKLI EP :
		//ORN : ws://localhost:9090/ws
	}
	
	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) {
		registry.enableSimpleBroker("/topic"); // BURADAN DA MESAJLAR CEKILIYOR
		//ORN: /topic/private/${chatId}
		registry.setApplicationDestinationPrefixes("/app"); //DESTINATION OLARAK BURAYA GONDERILECEK MESAJLAR
		//ORN: /app/private/${chatId}/sendMessage
	}
	
	@Override
	public void configureClientInboundChannel(ChannelRegistration registration) {
		//TODO: HANDSHAKE VE AUTH SIFIRLANDIGI ICIN BU KISIM DA SIFIRLANDI
	}
}