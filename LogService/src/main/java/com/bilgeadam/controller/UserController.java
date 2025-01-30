package com.bilgeadam.controller;

import com.bilgeadam.entity.User;
import com.bilgeadam.request.GetUserRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/user")
@RequiredArgsConstructor
public class UserController {
	
	@PostMapping("/getUserById")
	
	public ResponseEntity<User> getUser (@RequestBody GetUserRequest request, @RequestHeader ("x-sesion-token") String token) {
		
			return ResponseEntity.ok(User.builder().userId(request.getUserId()).build());
		
	}
	
}