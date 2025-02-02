package com.bilgeadam.manager;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(url = "http://localhost:9091/v1/dev/auth", name = "authManager")
public interface AuthManager {


}
