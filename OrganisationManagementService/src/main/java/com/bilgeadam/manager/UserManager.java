package com.bilgeadam.manager;

import com.bilgeadam.dto.request.ManageEmployeePermissionsRequest;
import com.bilgeadam.dto.response.BaseResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(url = "http://localhost:8091/v1/dev/user", name = "userManagementManager")
public interface UserManager {

    @PostMapping("/manage-user-permissions")
    ResponseEntity<BaseResponse<Boolean>> manageUserPermissions(@RequestBody ManageEmployeePermissionsRequest dto);
}
