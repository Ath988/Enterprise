package com.bilgeadam.enterprise.config;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.List;
import java.util.Map;

public class HttpHandshakeInterceptor implements HandshakeInterceptor {
	
	@Override
	public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
	                               WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
		List<String> authHeader = request.getHeaders().get("Authorization");
		if (authHeader != null && !authHeader.isEmpty()) {
			attributes.put("Authorization", authHeader.get(0));
		}
		return true;
	}
	
	@Override
	public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
	                           WebSocketHandler wsHandler, Exception exception) {
	}
}