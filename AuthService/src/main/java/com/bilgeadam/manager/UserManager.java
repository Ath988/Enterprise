package com.bilgeadam.manager;

import com.bilgeadam.dto.request.CreateMemberRequest;
import com.bilgeadam.dto.response.BaseResponse;
import com.bilgeadam.dto.response.UserPermissionResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(url = "http://localhost:8091/v1/dev/user", name = "userManagementManager")
public interface UserManager {

    @PostMapping("/create-member")
    ResponseEntity<BaseResponse<Boolean>> createMember(@RequestBody CreateMemberRequest dto);

    @PostMapping("create-user")
    ResponseEntity<BaseResponse<Boolean>> createUser(
            @RequestHeader(value = "Authorization", required = false) String token,
            @RequestBody CreateMemberRequest dto);

    @GetMapping("/user-permission-response")
    ResponseEntity<BaseResponse<UserPermissionResponse>> getUserPermission(@RequestParam Long authId);

}
