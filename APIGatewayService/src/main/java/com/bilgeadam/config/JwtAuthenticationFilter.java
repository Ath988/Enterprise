package com.bilgeadam.config;

import com.bilgeadam.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import java.util.List;

@Component
public class JwtAuthenticationFilter implements WebFilter {
	
	@Autowired
	private JwtService jwtService;
	
	// JWT doğrulaması gerektirmeyen yollar
	private static final List<String> PUBLIC_PATHS = List.of(
			"/home",
			"/about"
	);
	
	@Override
	public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
		ServerHttpRequest request = exchange.getRequest();
		String path = request.getURI().getPath();
		
		// Eğer istek herkese açık bir path içeriyorsa doğrulama yapmadan devam et
		if (isPublicPath(path)) {
			return chain.filter(exchange);
		}
		
		String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
		
		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			return unauthorizedResponse(exchange);
		}
		
		String token = authHeader.substring(7);
		
		if (!jwtService.isValidToken(token)) {
			return unauthorizedResponse(exchange);
		}
		
		return chain.filter(exchange);
	}
	
	private boolean isPublicPath(String path) {
		return PUBLIC_PATHS.stream().anyMatch(path::startsWith);
	}
	
	private Mono<Void> unauthorizedResponse(ServerWebExchange exchange) {
		return Mono.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "JWT Token missing or invalid"));
	}
}