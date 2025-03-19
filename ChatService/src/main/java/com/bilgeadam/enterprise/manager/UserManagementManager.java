package com.bilgeadam.enterprise.manager;

import com.bilgeadam.enterprise.dto.response.BaseResponse;
import com.bilgeadam.enterprise.dto.response.UserDetailForChatResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(
		url = "http://localhost:8091/v1/dev/user",
		name = "userManager",
		fallbackFactory = UserManagementFallbackFactory.class
)
public interface UserManagementManager {
	
	@GetMapping("/get-user-detail-for-chat/{employeeId}")
	ResponseEntity<BaseResponse<UserDetailForChatResponse>> getEmployeeDetailForChat(@PathVariable Long employeeId);
	
	@GetMapping("/get-users-by-company/{companyId}/{employeeId}")
	ResponseEntity<BaseResponse<List<UserDetailForChatResponse>>> getEmployeesDetailByCompanyId(@PathVariable Long companyId, @PathVariable Long employeeId);
	
	@GetMapping("/get-users-by-ids")
	ResponseEntity<BaseResponse<List<UserDetailForChatResponse>>> getEmployeesDetailByIds(@RequestParam List<Long> ids);
	
	@PostMapping("/set-users-online-status/{employeeId}")
	ResponseEntity<BaseResponse<Boolean>> setUsersOnlineStatus(@PathVariable Long employeeId, @RequestParam boolean status);
}