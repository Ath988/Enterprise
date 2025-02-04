package com.bilgeadam.enterprise.utility;

import com.bilgeadam.enterprise.exception.EnterpriseException;
import com.bilgeadam.enterprise.exception.ErrorType;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Aspect
@Component
public class TokenValidationAspect {
	private final JwtManager jwtManager;
	private final HttpServletRequest httpServletRequest;
	
	public TokenValidationAspect(JwtManager jwtManager, HttpServletRequest httpServletRequest) {
		this.jwtManager = jwtManager;
		this.httpServletRequest = httpServletRequest;
	}
	
	@Before("execution(* com.bilgeadam.enterprise.controller.*.*(..)) && !execution(* com.bilgeadam.enterprise" +
			".controller.ChatController.login(..)) && !execution(* com.bilgeadam.enterprise.controller" +
			".ChatController.sendPrivateMessage(..)) && !execution(* com.bilgeadam.enterprise.controller" +
			".ChatController.sendGroupMessage(..))")
	public void validateToken() {
		String headerToken = httpServletRequest.getHeader("Authorization");
		
		if (headerToken == null || !headerToken.startsWith("Bearer ")) {
			System.out.println("Aspect: Token eksik veya geçersiz!");
			throw new EnterpriseException(ErrorType.USER_NOT_AUTHORIZED, "Invalid Authorization header");
		}
		
		String token = headerToken.replace("Bearer ", "");
		Optional<String> userId = jwtManager.validateToken(token);
		
		if (userId.isEmpty()) {
			System.out.println("Aspect: Token doğrulama başarısız!");
			throw new EnterpriseException(ErrorType.USER_NOT_AUTHORIZED, "Invalid or expired token");
		}
		
		System.out.println("Aspect: Token doğrulandı, kullanıcı ID'si: " + userId.get());
		httpServletRequest.setAttribute("userId", userId.get());
	}
}