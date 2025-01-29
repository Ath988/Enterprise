package com.bilgeadam.filters.wrapper;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;

import java.util.HashMap;
import java.util.Map;

public class ResponseWrapper extends HttpServletResponseWrapper {
	public ResponseWrapper(HttpServletResponse response) {
		super(response);
	}
	public Map<String,String> getAllHeaders() {
		Map<String,String> headers = new HashMap<>();
		getHeaderNames().forEach(name -> headers.put(name, getHeader(name)));
		return headers;
	}
}