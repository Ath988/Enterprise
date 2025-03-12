package com.bilgeadam.client;

import com.bilgeadam.dto.request.LoginRequestDto;
import com.bilgeadam.dto.response.BaseResponse;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@FeignClient(url = "http://localhost:8081/v1/dev/auth", name = "authManager")
public interface AuthManager {
    @PostMapping("/create-employee")
    public ResponseEntity<BaseResponse<String>> doLogin(@RequestBody @Valid LoginRequestDto dto);
}

