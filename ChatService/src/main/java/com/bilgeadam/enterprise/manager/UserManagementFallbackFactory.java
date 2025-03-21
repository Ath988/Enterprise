package com.bilgeadam.enterprise.manager;

import com.bilgeadam.enterprise.dto.response.AdminDetailsForChatResponse;
import com.bilgeadam.enterprise.dto.response.BaseResponse;
import com.bilgeadam.enterprise.dto.response.UserDetailForChatResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class UserManagementFallbackFactory implements FallbackFactory<UserManagementManager> {
	@Override
	public UserManagementManager create(Throwable cause) {
		log.error("UserManagementManager service failed: {}", cause.getMessage());
		
		return new UserManagementManager() {
			@Override
			public ResponseEntity<BaseResponse<UserDetailForChatResponse>> getEmployeeDetailForChat(Long employeeId) {
				return ResponseEntity.ok(BaseResponse.<UserDetailForChatResponse>builder()
				                                     .message("Fallback: User details not available")
				                                     .success(false)
				                                     .build());
			}
			
			@Override
			public ResponseEntity<BaseResponse<List<UserDetailForChatResponse>>> getEmployeesDetailByCompanyId(Long companyId, Long employeeId) {
				return ResponseEntity.ok(BaseResponse.<List<UserDetailForChatResponse>>builder()
				                                     .message("Fallback: User details not available")
				                                     .success(false)
				                                     .build());
			}
			
			@Override
			public ResponseEntity<BaseResponse<List<UserDetailForChatResponse>>> getEmployeesDetailByIds(List<Long> ids) {
				return ResponseEntity.ok(BaseResponse.<List<UserDetailForChatResponse>>builder()
				                                     .message("Fallback: User details not available")
				                                     .success(false)
				                                     .build());
			}
			
			
			@Override
			public ResponseEntity<BaseResponse<Boolean>> setUsersOnlineStatus(Long employeeId, boolean status) {
				return ResponseEntity.ok(BaseResponse.<Boolean>builder()
				                                     .message("Fallback: Unable to set User's online status")
				                                     .success(false)
				                                     .build());
			}

			@Override
			public ResponseEntity<BaseResponse<List<AdminDetailsForChatResponse>>> getAdminsForChat() {
				return ResponseEntity.ok(BaseResponse.<List<AdminDetailsForChatResponse>>builder()
								.code(200)
						.message("Fallback: Unable to set User's online status")
						.success(false)
						.build());
			}
		};
	}
}