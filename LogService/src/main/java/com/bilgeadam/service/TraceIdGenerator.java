package com.bilgeadam.service;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.bilgeadam.constants.LoggingConstants.X_TRACE_ID;

@Service
public class TraceIdGenerator {
	public void generateRandomTraceId(HttpServletRequest request) {
		String traceId = UUID.randomUUID().toString();
		MDC.put(X_TRACE_ID, traceId);
	}
	
}