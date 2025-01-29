package com.bilgeadam.filters;

import com.bilgeadam.filters.wrapper.RequestWrapper;
import com.bilgeadam.filters.wrapper.ResponseWrapper;
import com.bilgeadam.service.TraceIdGenerator;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
 */
public class LoggingFilter extends OncePerRequestFilter {
	
	private final TraceIdGenerator traceIdGenerator;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		setResponseHeaders(response);
		
		RequestWrapper requestWrapper = new RequestWrapper(request);
		ResponseWrapper responseWrapper = new ResponseWrapper(response);
		
		log.info(String.format("Request Headers: %s",requestWrapper.getAllHeaders().toString()));
		log.info(String.format("Response Headers: %s",responseWrapper.getAllHeaders().toString()));
		
		/**
		 * TraceId ile request ve response birbirine bağlamak için kullanılır
		 */
		
		traceIdGenerator.generateRadomTraceId();
		logResponse(responseWrapper);
		logRequest(requestWrapper);
		
		
		filterChain.doFilter(requestWrapper, responseWrapper);
		
	}
	
	private void logRequest(RequestWrapper requestWrapper) throws IOException {
		log.info(String.format("## %s ## Request Method: %s,Request URL: %s, RequestHeaders: %s, RequestBody: %s",
		                       requestWrapper.getServerName(),
		                       requestWrapper.getMethod(),
		                       requestWrapper.getRequestURL(),
		                       requestWrapper.getAllHeaders(),
		                       requestWrapper.body
		
		));
	}
	
	private void logResponse(ResponseWrapper responseWrapper) {
	
	}
	
	private void setResponseHeaders(HttpServletResponse response) {
		response.addHeader(X_TRACE_ID, MDC.get(X_TRACE_ID));
	}
}