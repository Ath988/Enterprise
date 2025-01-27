package com.bilgeadam.enterprise.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
				.csrf(csrf -> csrf.disable())
				.authorizeHttpRequests(auth -> auth
						.requestMatchers("/ws/**", "/app/**").permitAll() // WebSocket ve STOMP endpoint'lerine izin ver
						.anyRequest().authenticated()
				)
				.formLogin(form -> form.disable()) // Default login sayfasını devre dışı bırak
				.httpBasic(httpBasic -> httpBasic.disable()); // Basic Auth'u devre dışı bırak
		
		return http.build();
	}

}