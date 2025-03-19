package com.bilgeadam.enterprise.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/ws/**")
		        .allowedOriginPatterns("http://localhost:5173", "http://192.168.1.106:5173", "https://6239-85-107-89-21.ngrok-free.app")
		        .allowCredentials(true);
	}
}