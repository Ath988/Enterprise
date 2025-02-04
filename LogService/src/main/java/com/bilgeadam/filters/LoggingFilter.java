package com.bilgeadam.filters;

import com.bilgeadam.filters.wrapper.RequestWrapper;
import com.bilgeadam.filters.wrapper.ResponseWrapper;
import com.bilgeadam.service.TraceIdGenerator;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static com.bilgeadam.constants.LoggingConstants.X_TRACE_ID;

@Slf4j
@RequiredArgsConstructor
@Component
/**
 * Tüm request ve response yakalar ve filtreler.
 */ public class LoggingFilter extends OncePerRequestFilter {
	
	private final TraceIdGenerator traceIdGeneratorService;
	
	@SneakyThrows
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		RequestWrapper requestWrapper = new RequestWrapper(request);
		ResponseWrapper responseWrapper = new ResponseWrapper(response);
		traceIdGeneratorService.generateRandomTraceId(request);
		setResponseHeader(responseWrapper);
		log.info(requestLogFormatString(requestWrapper));
		filterChain.doFilter(requestWrapper, responseWrapper);
		log.info(responseLogFormatString(responseWrapper));
	}
	
	private String requestLogFormatString(RequestWrapper request) throws IOException {
		return String.format("## %s ## Request Method: %s, Request Uri: %s, Request TraceId: %s, Request Headers: %s, " +
				                     "Request Body: %s", request.getServerName(), request.getMethod(),
		                     request.getRequestURI(), request.getAttribute(X_TRACE_ID), request.getAllHeaders(),
		                     RequestWrapper.body);
	}
	
	private String responseLogFormatString(ResponseWrapper responseWrapper) {
		return String.format("Response Status: %s, Response Headers: %s, Response TraceId: %s, Response Body, %s",
		                     responseWrapper.getStatus(), responseWrapper.getAllHeaders(),
		                     responseWrapper.getHeader(X_TRACE_ID), IOUtils.toString(responseWrapper.getCopyBody(),
		                                                                             responseWrapper.getCharacterEncoding()));
	}
	
	private void setResponseHeader(HttpServletResponse response) {
		response.addHeader(X_TRACE_ID, MDC.get(X_TRACE_ID));
	}
	
	
}