package com.bilgeadam.enterprise.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import java.util.Map;

public class WebSocketAuthInterceptor extends HttpSessionHandshakeInterceptor {
	@Override
	public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
	                               WebSocketHandler wsHandler, Map<String, Object> attributes) {
		// Örneğin JWT doğrulama:
		String token = request.getHeaders().getFirst("Authorization");
		if (isValidToken(token)) {
			// Kullanıcıyı doğrula ve devam et
			return true;
		}
		// Bağlantıyı reddet
		response.setStatusCode(HttpStatus.UNAUTHORIZED);
		return false;
	}
	
	private boolean isValidToken(String token) {
		// Token doğrulama mantığı
		return token != null && token.startsWith("Bearer ");
	}
}