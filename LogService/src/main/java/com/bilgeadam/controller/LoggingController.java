package com.bilgeadam.controller;

import static com.bilgeadam.config.RestApis.*;

import com.bilgeadam.service.LoggingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(LOGGING)
public class LoggingController {
	
	
	private final LoggingService loggingService;
	
	@PostMapping(REQUEST)
	public ResponseEntity<String> logRequest(@RequestBody String requestLog) {
		loggingService.logRequest(requestLog);
		return ResponseEntity.ok("Log isteği alındı.");
	}
	
	@PostMapping(RESPONSE)
	public ResponseEntity<String> logResponse(@RequestBody String responseLog) {
		loggingService.logResponse(responseLog);
		return ResponseEntity.ok("Log isteği yanıtlandı.");
	}
}