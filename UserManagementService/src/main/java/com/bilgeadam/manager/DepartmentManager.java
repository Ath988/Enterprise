package com.bilgeadam.manager;

import com.bilgeadam.dto.response.BaseResponse;
import com.bilgeadam.dto.response.otherServices.VwDepartmendAndPosition;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(url = "http://localhost:8087/v1/dev/department", name = "departmentManager")
public interface DepartmentManager {

    @GetMapping("/other-services/get-department-and-position")
    ResponseEntity<BaseResponse<VwDepartmendAndPosition>> getPositionAndDepartmentName
            (@RequestHeader(value = "Authorization", required = false) String token);
}
