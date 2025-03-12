package com.bilgeadam.manager;

import com.bilgeadam.dto.request.CreateCompanyManagerRequest;
import com.bilgeadam.dto.response.BaseResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;

import static com.bilgeadam.constant.RestApis.*;

@FeignClient(url = "http://localhost:8087/v1/dev/employee", name = "organisationManagementManager")
public interface OrganisationManagementManager {

    @PostMapping(CREATE_COMPANY_MANAGER)
    ResponseEntity<BaseResponse<Boolean>> createCompanyManager(CreateCompanyManagerRequest dto);
}