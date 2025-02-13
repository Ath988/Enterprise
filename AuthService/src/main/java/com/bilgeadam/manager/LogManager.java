package com.bilgeadam.manager;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import static com.bilgeadam.constant.RestApis.REQUEST;

@FeignClient(url = "http://localhost:9090/v1/dev/logging", name = "logManager")
public interface LogManager {
	@PostMapping(REQUEST)
	public ResponseEntity<String> logRequest(@RequestBody String requestLog);
}