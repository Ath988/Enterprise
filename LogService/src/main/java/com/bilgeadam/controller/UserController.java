package com.bilgeadam.controller;
import com.bilgeadam.entity.User;
import com.bilgeadam.request.GetUserRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {
	
	@PostMapping("/getUserById")
	public ResponseEntity<User> getUser (@RequestBody GetUserRequest request, @RequestHeader ("x-sesion-token") String token) {
		log.error("Log error");
		log.warn("Log warn");
		log.info("Log info");
		log.debug("Log debug");
		log.trace("Log trace");
			return ResponseEntity.ok(User.builder().userId(request.getUserId()).build());
		
	}
	
}