package com.bilgeadam.controller;

import com.bilgeadam.dto.request.AddPayrollRequest;
import com.bilgeadam.dto.request.UpdatePayrollRequest;
import com.bilgeadam.dto.response.BaseResponse;
import com.bilgeadam.dto.response.PayrollResponse;
import com.bilgeadam.entity.Payroll;
import com.bilgeadam.service.PayrollService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.bilgeadam.constants.RestApis.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(PAYROLL)
@CrossOrigin
public class PayrollController {
    private final PayrollService payrollService;

    @PostMapping
    public ResponseEntity<BaseResponse<Boolean>> addPayroll(
            @RequestHeader(value = "Authorization", required = false) String token,
            @RequestBody AddPayrollRequest dto) {
        return ResponseEntity.ok(BaseResponse.<Boolean>builder()
                .success(payrollService.addPayroll(token, dto))
                .message("Yeni bordro kaydı eklendi.")
                .build());
    }

    @GetMapping("{payrollId}")
    public ResponseEntity<BaseResponse<PayrollResponse>> getPayroll(
            @RequestHeader(value = "Authorization", required = false) String token,
            @PathVariable Long payrollId) {
        return ResponseEntity.ok(BaseResponse.<PayrollResponse>builder()
                .data(payrollService.getPayroll(token, payrollId))
                .message("Bordro kaydı getirildi.")
                .build());
    }

    @GetMapping
    public ResponseEntity<BaseResponse<List<PayrollResponse>>> getPayrolls(
            @RequestHeader(value = "Authorization", required = false) String token) {
        return ResponseEntity.ok(BaseResponse.<List<PayrollResponse>>builder()
                .data(payrollService.getAllPayrolls(token))
                .message("Maaş bordro kayıtları listesi getirildi.")
                .build());
    }

    @PutMapping
    public ResponseEntity<BaseResponse<Boolean>> updatePayroll(
            @RequestHeader(value = "Authorization", required = false) String token,
            @RequestBody UpdatePayrollRequest dto) {
        return ResponseEntity.ok(BaseResponse.<Boolean>builder()
                .success(payrollService.updatePayroll(token, dto))
                .message("Bordo kaydı güncellendi.")
                .build());
    }

    @DeleteMapping("{payrollId}")
    public ResponseEntity<BaseResponse<Boolean>> deletePayroll(
            @RequestHeader(value = "Authorization", required = false) String token,
            @PathVariable Long payrollId) {
        return ResponseEntity.ok(BaseResponse.<Boolean>builder()
                .success(payrollService.deletePayroll(token, payrollId))
                .message("Bordo kaydı silindi.")
                .build());
    }

    @Operation(summary = "Çalışan idsine göre Payroll listesi döner.")
    @GetMapping("/employee-payrolls/{employeeId}")
    public ResponseEntity<BaseResponse<List<Payroll>>> getAllEmployeePayrolls(
            @RequestHeader(value = "Authorization", required = false) String token,
            @PathVariable Long employeeId) {
        return ResponseEntity.ok(BaseResponse.<List<Payroll>>builder()
                .data(payrollService.findAllPayrollsByEmployeeId(token, employeeId))
                .message("Çalışana ait bordo kayıtlar listesi getirildi.")
                .build());
    }

    @Operation(summary = "Ödeme zamanı yıl ve ay ile eşleşen Payroll listesini döner.")
    @GetMapping("get-all-by-month/{year}/{month}")
    public ResponseEntity<BaseResponse<List<PayrollResponse>>> getPayrollsByYearAndMonth(
            @RequestHeader(value = "Authorization", required = false) String token,
            @PathVariable int year,
            @PathVariable int month) {
        return ResponseEntity.ok(BaseResponse.<List<PayrollResponse>>builder()
                .data(payrollService.getAllPayrollsByYearAndMonth(token, year, month))
                .message("Belirli bir aya ait bordro kayıtları getirildi.")
                .build());
    }

    @Operation(summary = "PENDING olan Payroll listesini döner.")
    @GetMapping("get-pending-payrolls")
    public ResponseEntity<BaseResponse<List<PayrollResponse>>> getPendingPayrolls(
            @RequestHeader(value = "Authorization", required = false) String token) {
        return ResponseEntity.ok(BaseResponse.<List<PayrollResponse>>builder()
                .data(payrollService.getAllPendingPayrolls(token))
                .message("Durumu PENDING olan maaş bordroları getirildi.")
                .build());
    }

    @PatchMapping("/approve/{payrollId}")
    public ResponseEntity<BaseResponse<Boolean>> approvePayroll(
            @RequestHeader(value = "Authorization", required = false) String token,
            @PathVariable Long payrollId) {
        return ResponseEntity.ok(BaseResponse.<Boolean>builder()
                        .success(payrollService.approvePayroll(token, payrollId))
                        .message("Maaş bordrosu onaylandı.")
                .build());
    }

    @PatchMapping("/reject/{payrollId}")
    public ResponseEntity<BaseResponse<Boolean>> rejectPayroll(
            @RequestHeader(value = "Authorization", required = false) String token,
            @PathVariable Long payrollId) {
        return ResponseEntity.ok(BaseResponse.<Boolean>builder()
                .success(payrollService.rejectPayroll(token, payrollId))
                .message("Maaş bordrosu reddedildi.")
                .build());
    }



}
