package com.bilgeadam.service;

import org.slf4j.MDC;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class TraceIdGenerator {
	public String generateRadomTraceId() {
		String traceId = UUID.randomUUID().toString();
		
		MDC.put("X_TRACE_ID", traceId);
		return traceId;
	}
	
}