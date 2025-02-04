package com.bilgeadam.manager;

import com.bilgeadam.dto.request.otherServices.RegisterRequestDto;
import com.bilgeadam.dto.response.BaseResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import static com.bilgeadam.constants.RestApis.*;

@FeignClient(url = "http://localhost:9091/v1/dev/auth", name = "authManager")
public interface AuthManager {

    @PostMapping(DOREGISTER)
    ResponseEntity<BaseResponse<Long>> register(RegisterRequestDto dto);

}
