package com.projectmanagementservice.manager;

import com.projectmanagementservice.dto.response.BaseResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static com.projectmanagementservice.constants.Endpoints.*;
@FeignClient(url = "http://localhost:8081/v1/dev/auth", name = "authManager")
public interface AuthManager {
	@GetMapping(AUTD_ID)
	public ResponseEntity<BaseResponse<Long>> authUserId(@RequestParam("token") String token);
}