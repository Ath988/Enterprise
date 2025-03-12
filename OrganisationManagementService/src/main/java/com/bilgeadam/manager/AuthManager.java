package com.bilgeadam.manager;

import com.bilgeadam.dto.request.otherServices.RegisterRequestDto;
import com.bilgeadam.dto.response.BaseResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import static com.bilgeadam.constants.RestApis.*;

@FeignClient(url = "http://localhost:8081/v1/dev/auth", name = "authManager")
public interface AuthManager {

    @PostMapping("/create-employee")
    ResponseEntity<BaseResponse<Long>> register(
            @RequestHeader(value = "Authorization", required = false) String token,
            @RequestBody RegisterRequestDto dto);

}