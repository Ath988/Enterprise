package com.bilgeadam.controller;

import com.bilgeadam.dto.request.AddNewDepartmentRequest;
import com.bilgeadam.dto.request.UpdateDepartmentRequest;
import com.bilgeadam.dto.response.AllDepartmentResponse;
import com.bilgeadam.dto.response.BaseResponse;
import com.bilgeadam.dto.response.DepartmentDetailResponse;
import com.bilgeadam.entity.Department;
import com.bilgeadam.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static com.bilgeadam.constants.RestApis.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(DEPARTMENT)
public class DepartmentController {
    private final DepartmentService departmentService;

    //Yeni departman oluşturur.
    @PostMapping
    public ResponseEntity<BaseResponse<Boolean>> addDepartment(
            @RequestHeader(value = "Authorization", required = false) String token,
            @RequestBody AddNewDepartmentRequest dto) {
        return ResponseEntity.ok(BaseResponse.<Boolean>builder()
                .success(departmentService.addDepartment(token, dto))
                .message("Yeni departman eklendi.")
                .build());
    }

    //Departman bilgilerini günceller.
    @PutMapping
    public ResponseEntity<BaseResponse<Boolean>> updateDepartment(
            @RequestHeader(value = "Authorization", required = false) String token,
            @RequestBody UpdateDepartmentRequest dto) {
        return ResponseEntity.ok(BaseResponse.<Boolean>builder()
                .success(departmentService.updateDepartment(token,dto))
                .message("Departman bilgileri güncellendi.")
                .build());
    }

    //Soft delete.
    @DeleteMapping("{departmentId}")
    public ResponseEntity<BaseResponse<Boolean>> deleteDepartment(
            @RequestHeader(value = "Authorization", required = false) String token,
            @PathVariable Long departmentId) {
        return ResponseEntity.ok(BaseResponse.<Boolean>builder()
                .success(departmentService.deleteDepartment(token, departmentId))
                .message("Departman silindi.")
                .build());
    }

    @GetMapping("{departmentId}")
    public ResponseEntity<BaseResponse<DepartmentDetailResponse>> getDepartment(
            @RequestHeader(value = "Authorization", required = false) String token,
            @PathVariable Long departmentId) {
        return ResponseEntity.ok(BaseResponse.<DepartmentDetailResponse>builder()
                .data(departmentService.findDepartmentDetail(token,departmentId))
                .message("Departman detayı getirildi.")
                .build());
    }

    //Firmanın tüm departmanlarını listeler.
    @GetMapping
    public ResponseEntity<BaseResponse<List<AllDepartmentResponse>>> getAllDepartment(
            @RequestHeader(value = "Authorization", required = false) String token) {
        return ResponseEntity.ok(BaseResponse.<List<AllDepartmentResponse>>builder()
                .data(departmentService.findAllDepartments(token))
                .message("Tüm departmanlar listesi.")
                .build());
    }

    @GetMapping("{departmentId}/subdepartments")
    public ResponseEntity<BaseResponse<List<AllDepartmentResponse>>> getAllSubDepartments(
            @RequestHeader(value = "Authorization", required = false) String token,
            @PathVariable Long departmentId) {
        return ResponseEntity.ok(BaseResponse.<List<AllDepartmentResponse>>builder()
                .data(departmentService.findAllSubDepartments(token,departmentId))
                .message("Alt departmanlar getirildi.")
                .build());
    }

    @GetMapping("{managerId}/manager_departments")
    public ResponseEntity<BaseResponse<List<AllDepartmentResponse>>> getDepartmentsOfManager(
            @RequestHeader(value = "Authorization", required = false) String token,
            @PathVariable Long managerId) {
        return ResponseEntity.ok(BaseResponse.<List<AllDepartmentResponse>>builder()
                .data(departmentService.findAllDepartmentsOfManager(token,managerId))
                .message("Menajerin yönettiği departmanların listesi.")
                .build());
    }

    //Departmanın bağlı olduğu üst birimleri getirir.
    @GetMapping("{departmentId}/hierarchy")
    public ResponseEntity<BaseResponse<List<AllDepartmentResponse>>> getDepartmentHierarchy(
            @RequestHeader(value = "Authorization", required = false) String token,
            @PathVariable Long departmentId) {
        return ResponseEntity.ok(BaseResponse.<List<AllDepartmentResponse>>builder()
                .data(departmentService.findDepartmentHierarchy(token,departmentId))
                .message("Departman üst birimleri getirildi.")
                .build());
    }

    //Todo: Departman istatistikleri için endpoint, frontend ihtiyaçlarına göre oluşturulcak.


}
