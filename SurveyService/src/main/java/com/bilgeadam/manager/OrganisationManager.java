package com.bilgeadam.manager;

import com.bilgeadam.dto.response.BaseResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "organisationManagementService", url="http://localhost:8087/v1/dev/employee")
public interface OrganisationManager {
	@GetMapping("get-company-id/{authId}")
	ResponseEntity<BaseResponse<Long>> getCompanyIdByAuthId(@PathVariable("authId") Long authId);
}