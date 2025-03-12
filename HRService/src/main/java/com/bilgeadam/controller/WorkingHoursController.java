package com.bilgeadam.controller;

import com.bilgeadam.dto.request.AddPayrollRequest;
import com.bilgeadam.dto.request.AddWorkingHoursRequest;
import com.bilgeadam.dto.request.UpdateWorkingHoursRequest;
import com.bilgeadam.dto.response.BaseResponse;
import com.bilgeadam.dto.response.WorkingHoursResponse;
import com.bilgeadam.entity.WorkingHours;
import com.bilgeadam.service.WorkingHoursService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.bilgeadam.constants.RestApis.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(WORKING_HOURS)
public class WorkingHoursController {
    private final WorkingHoursService workingHoursService;

    @PostMapping
    public ResponseEntity<BaseResponse<Boolean>> add(
            @RequestHeader(value = "Authorization", required = false) String token,
            @RequestBody AddWorkingHoursRequest dto) {
        return ResponseEntity.ok(BaseResponse.<Boolean>builder()
                .success(workingHoursService.addWorkingHours(token, dto))
                .message("Yeni çalışma saati kaydı eklendi.")
                .build());
    }

    @GetMapping
    public ResponseEntity<BaseResponse<List<WorkingHours>>> findAll(
            @RequestHeader(value = "Authorization", required = false) String token) {
        return ResponseEntity.ok(BaseResponse.<List<WorkingHours>>builder()
                .data(workingHoursService.findAllWorkingHours(token))
                .message("Bütün çalışma saatleri getirildi.")
                .build());
    }

    @GetMapping("{employeeId}")
    public ResponseEntity<BaseResponse<List<WorkingHoursResponse>>> findAll(
            @RequestHeader(value = "Authorization", required = false) String token,
            @PathVariable Long employeeId) {
        return ResponseEntity.ok(BaseResponse.<List<WorkingHoursResponse>>builder()
                .data(workingHoursService.findAllWorkingHoursByEmployeeId(token, employeeId))
                .message("Çalışana ait çalışma saatleri getirildi.")
                .build());
    }

    @PutMapping
    public ResponseEntity<BaseResponse<Boolean>> update(
            @RequestHeader(value = "Authorization", required = false) String token,
            @RequestBody UpdateWorkingHoursRequest dto) {
        return ResponseEntity.ok(BaseResponse.<Boolean>builder()
                .success(workingHoursService.updateWorkingHours(token, dto))
                .message("Çalışma saati kaydı güncellendi.")
                .build());
    }

    @DeleteMapping("{employeeId}")
    public ResponseEntity<BaseResponse<Boolean>> delete(
            @RequestHeader(value = "Authorization", required = false) String token,
            @PathVariable Long employeeId) {
        return ResponseEntity.ok(BaseResponse.<Boolean>builder()
                .success(workingHoursService.deleteWorkingHours(token, employeeId))
                .build());
    }


}
