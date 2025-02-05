package com.bilgeadam.controller;

import com.bilgeadam.dto.request.AddEmployeeRecordRequest;
import com.bilgeadam.dto.request.UpdateEmployeeRecordRequest;
import com.bilgeadam.dto.response.AllEmployeeRecordResponse;
import com.bilgeadam.dto.response.BaseResponse;
import com.bilgeadam.dto.response.EmployeeRecordResponse;
import com.bilgeadam.entity.EmployeeRecord;
import com.bilgeadam.service.EmployeeRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.bilgeadam.constants.RestApis.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(EMPLOYEE_RECORD)
public class EmployeeRecordController {

    private final EmployeeRecordService employeeRecordService;

    @PostMapping
    public ResponseEntity<BaseResponse<Boolean>> addEmployeeRecord(
            @RequestHeader(value = "Authorization", required = false) String token,
            @RequestBody AddEmployeeRecordRequest dto) {
        return ResponseEntity.ok(BaseResponse.<Boolean>builder()
                .success(employeeRecordService.addNewEmployeeRecord(token, dto))
                .message("Yeni çalışan kaydı başarıyla eklendi.")
                .build());

    }

    @GetMapping
    public ResponseEntity<BaseResponse<List<AllEmployeeRecordResponse>>> getEmployeeRecord(
            @RequestHeader(value = "Authorization", required = false) String token){
        return ResponseEntity.ok(BaseResponse.<List<AllEmployeeRecordResponse>>builder()
                        .data(employeeRecordService.findAllEmployeeRecord(token))
                        .message("Bütün çalışan kayıtları getirildi.")
                .build());
    }


    @GetMapping("{employeeRecordId}")
    public ResponseEntity<BaseResponse<EmployeeRecordResponse>> getEmployeeRecord(
            @RequestHeader(value = "Authorization", required = false) String token,
            @PathVariable Long employeeRecordId) {

        return ResponseEntity.ok(BaseResponse.<EmployeeRecordResponse>builder()
                .data(employeeRecordService.getEmployeeRecord(token, employeeRecordId))
                .message("Çalışan kaydı getirildi.")
                .build());
    }

    @PutMapping
    public ResponseEntity<BaseResponse<Boolean>> updateEmployeeRecord(
            @RequestHeader(value = "Authorization", required = false) String token,
            @RequestBody UpdateEmployeeRecordRequest dto) {
        return ResponseEntity.ok(BaseResponse.<Boolean>builder()
                .success(employeeRecordService.updateEmployeeRecord(token, dto))
                .message("Çalışan kaydı güncellendi.")
                .build());
    }

    @DeleteMapping("{employeeRecordId}")
    public ResponseEntity<BaseResponse<EmployeeRecordResponse>> deleteEmployeeRecord(
            @RequestHeader(value = "Authorization", required = false) String token,
            @PathVariable Long employeeRecordId) {

        return ResponseEntity.ok(BaseResponse.<EmployeeRecordResponse>builder()
                .success(employeeRecordService.deleteEmployeeRecord(token, employeeRecordId))
                .message("Çalışan kaydı silindi.")
                .build());
    }




}
