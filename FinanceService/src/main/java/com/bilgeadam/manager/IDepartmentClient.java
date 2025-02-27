package com.bilgeadam.manager;

import com.bilgeadam.dto.response.DepartmentResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(url = "http://localhost:8087/v1/dev/departmentId", name = "organisationManagementManager")
public interface IDepartmentClient {
    @GetMapping("/api/v1/department/{id}")
    DepartmentResponseDTO getDepartmentById(@PathVariable Long id);
    @GetMapping("/api/v1/department/name/{name}")
    DepartmentResponseDTO getDepartmentByName(@PathVariable String name);
}
