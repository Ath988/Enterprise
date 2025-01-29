package com.bilgeadam.filters.wrapper;

import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class RequestWrapper extends HttpServletRequestWrapper {
	public static String body;
	
	
	public RequestWrapper(HttpServletRequest request) {
		super(request);
		body ="";
		try (BufferedReader reader = request.getReader()) {
			String line;
			while ((line = reader.readLine()) != null) {
				body += line;
			}
			
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
	}
	
	public Map<String,String> getAllHeaders() {
		Map<String,String> headers = new HashMap<>();
		Collections.list(getHeaderNames()).forEach(name -> headers.put(name, getHeader(name)));
		return headers;
	}
	@Override
	public ServletInputStream getInputStream() {
		final ByteArrayInputStream bais = new ByteArrayInputStream(body.getBytes());
		return new ServletInputStream() {
			@Override
			public int read() throws IOException {
				return bais.read();
			}
			
			@Override
			public boolean isFinished() {
				return false;
			}
			
			@Override
			public boolean isReady() {
				return true;
			}
			
			@Override
			public void setReadListener(ReadListener readListener) {
			
			}
		};
	}
	@Override
	public BufferedReader getReader() throws IOException {
		return new BufferedReader(new InputStreamReader(this.getInputStream()));
	}
}