package com.bilgeadam.enterprise.manager;

import com.bilgeadam.enterprise.dto.request.LoginRequestDto;
import com.bilgeadam.enterprise.dto.response.BaseResponse;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import static com.bilgeadam.enterprise.constant.RestApis.*;

@FeignClient(url = "http://localhost:8081/v1/dev/auth", name = "authManager")
public interface AuthManager {

    @PostMapping(DO_LOGIN)
    public ResponseEntity<BaseResponse<String>> doLogin(@RequestBody @Valid LoginRequestDto dto);

}
