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
						.requestMatchers("/ws/**", "/app/**").permitAll() // WebSocket izin ver
						.requestMatchers("/**").permitAll() // Login ve Register'a izin ver
						.anyRequest().authenticated()
				)
				.formLogin(form -> form.disable())
				.httpBasic(httpBasic -> httpBasic.disable());
		
		return http.build();
	}
	
	
}