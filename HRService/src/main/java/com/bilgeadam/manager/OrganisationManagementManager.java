package com.bilgeadam.manager;

import com.bilgeadam.dto.request.otherServices.AddEmployeeRequest;
import com.bilgeadam.dto.request.otherServices.UpdateEmployeeRequest;
import com.bilgeadam.dto.response.BaseResponse;
import com.bilgeadam.dto.response.otherServices.AllEmployeeResponse;
import com.bilgeadam.dto.response.otherServices.EmployeeDetailResponse;
import com.bilgeadam.dto.response.otherServices.EmployeeSaveResponse;
import com.bilgeadam.entity.enums.EState;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@FeignClient(url = "http://localhost:8087/v1/dev/employee", name = "organisationManager")
public interface OrganisationManagementManager {

    @PostMapping
    ResponseEntity<BaseResponse<EmployeeSaveResponse>> addEmployee(
            @RequestHeader(value = "Authorization", required = false) String token,
            @RequestBody AddEmployeeRequest dto);

    @GetMapping("{employeeId}")
    ResponseEntity<BaseResponse<EmployeeDetailResponse>> getEmployeeDetails(
            @RequestHeader(value = "Authorization", required = false) String token,
            @PathVariable Long employeeId);

    @PutMapping
    ResponseEntity<BaseResponse<Boolean>> updateEmployee(
            @RequestHeader(value = "Authorization", required = false) String token,
            @RequestBody UpdateEmployeeRequest dto);

    @DeleteMapping("{employeeId}")
    ResponseEntity<BaseResponse<Boolean>> deleteEmployee(
            @RequestHeader(value = "Authorization", required = false) String token,
            @PathVariable Long employeeId);

    @GetMapping("/get-all-employee")
    ResponseEntity<BaseResponse<List<AllEmployeeResponse>>> getAllEmployees(
            @RequestHeader(value = "Authorization", required = false) String token,
            @RequestParam Optional<EState> state);


    @GetMapping("/employee-name")
    ResponseEntity<BaseResponse<String>> getEmployeeNameByToken(
            @RequestHeader(value = "Authorization", required = false) String token,
            @RequestParam Optional<Long> employeeId);

    @GetMapping("/check-company/{employeeId}")
    ResponseEntity<BaseResponse<Boolean>> checkCompanyId(
            @RequestHeader(value = "Authorization", required = false) String token,
            @PathVariable Long employeeId);

    @GetMapping("/get-id")
    ResponseEntity<BaseResponse<Long>> getEmployeeId(
            @RequestHeader(value = "Authorization", required = false) String token);

    @PostMapping("get-all-employee-names")
    ResponseEntity<BaseResponse<Map<Long,String>>> getAllEmployeeNames(@RequestBody List<Long> employeeIdList);
}