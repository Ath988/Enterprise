package com.bilgeadam.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class LoggingService {
	
	public void logRequest(String logMessage) {
		log.info(logMessage);
	}
	
	public void logResponse(String logMessage) {
		log.info(logMessage);
	}
}