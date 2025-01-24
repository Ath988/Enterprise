package com.bilgeadam.enterprise.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
				.csrf(csrf -> csrf.disable()) // CSRF'yi devre dışı bırak
				.authorizeHttpRequests(auth -> auth
						.anyRequest().permitAll() // Tüm istekleri izinli yap
				)
				.formLogin(form -> form.disable()) // Default login sayfasını devre dışı bırak
				.httpBasic(httpBasic -> httpBasic.disable()); // Basic Auth'u devre dışı bırak
		
		return http.build();
	}
}