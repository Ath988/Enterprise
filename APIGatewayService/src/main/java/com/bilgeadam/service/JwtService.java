package com.bilgeadam.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

@Service
public class JwtService {
	
	@Value("${jwt.secret}")
	private String secretKey;
	
	public boolean isValidToken(String token) {
		try {
			SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
			
			Claims claims = Jwts.parserBuilder()
			                    .setSigningKey(key)
			                    .build()
			                    .parseClaimsJws(token)
			                    .getBody();
			
			System.out.println("Kullanıcı: " + claims.getSubject());
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}