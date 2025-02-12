package com.bilgeadam.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
				.csrf(csrf -> csrf.disable())                // CSRF'yi devre dışı bırakıyoruz
				.authorizeHttpRequests(authz -> authz
						.anyRequest().permitAll()                 // Tüm isteklere izin veriyoruz
				)
				.httpBasic(httpBasic -> httpBasic.disable())  // HTTP Basic Auth'ı devre dışı bırakıyoruz
				.formLogin(formLogin -> formLogin.disable())  // Form tabanlı login'i devre dışı bırakıyoruz
				.logout(logout -> logout.disable());          // Çıkış işlemini devre dışı bırakıyoruz
		
		return http.build();
	}
	
	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(List.of("http://localhost:5173"));  // Frontend URL'sine izin ver
		configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));  // İzin verilen HTTP metodları
		configuration.setAllowedHeaders(List.of("*"));  // Tüm header’lara izin ver
		configuration.setAllowCredentials(true);  // Kimlik bilgilerini içeren isteklere izin ver
		
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);  // Tüm URL'ler için CORS'u uygula
		
		return source;
	}
}