package com.projectmanagementservice.manager;

import com.projectmanagementservice.dto.response.AllEmployeeResponse;
import com.projectmanagementservice.dto.response.BaseResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(url = "http://localhost:8087/v1/dev/employee", name = "OrganisationManager")
public interface OrganisationManager {
	@GetMapping("get-company-id/{authId}")
	public ResponseEntity<BaseResponse<Long>> getCompanyIdByAuthId(@PathVariable Long authId);
	@GetMapping("get-all-employees-by-company-id/{companyId}")
	public ResponseEntity<BaseResponse<List<AllEmployeeResponse>>> getAllEmployeesByCompanyId(@PathVariable Long companyId);
}