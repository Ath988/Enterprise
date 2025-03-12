package com.bilgeadam.manager;

import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import static com.bilgeadam.constant.RestApis.*;

@FeignClient(url = "http://localhost:8081/v1/dev/auth", name = "authManager")
public interface AuthManager {


}
