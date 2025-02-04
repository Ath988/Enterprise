package com.bilgeadam.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
public class WebSecurityConfig {
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		
		http.authorizeHttpRequests(req->req
				.requestMatchers("/**").permitAll());
		
		
		http.csrf(AbstractHttpConfigurer::disable);
		//Geçici olarak cors tamamen devre dışı bırakıldı.
		http.cors(AbstractHttpConfigurer::disable);
		
		return http.build();
	}
	
}