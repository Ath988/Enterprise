package com.bilgeadam.controller;

import com.bilgeadam.dto.request.AddLeaveRequest;
import com.bilgeadam.dto.request.AddPayrollRequest;
import com.bilgeadam.dto.request.UpdateLeaveRequest;
import com.bilgeadam.dto.response.BaseResponse;
import com.bilgeadam.dto.response.LeaveDetailsResponse;
import com.bilgeadam.dto.response.LeaveResponse;
import com.bilgeadam.service.LeaveService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.bilgeadam.constants.RestApis.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(LEAVE)
public class LeaveController {
    private final LeaveService leaveService;

    @PostMapping
    public ResponseEntity<BaseResponse<Boolean>> add(
            @RequestHeader(value = "Authorization", required = false) String token,
            @RequestBody AddLeaveRequest dto) {
        return ResponseEntity.ok(BaseResponse.<Boolean>builder()
                .success(leaveService.createLeaveRequest(token, dto))
                .message("Yeni izin isteği oluşturuldu.")
                .build());
    }

    @Operation(summary = "Çalışanların yapmış olduğu ve PENDING olan izin isteklerinin listesini getirir.")
    @GetMapping("/get-all-pending")
    public ResponseEntity<BaseResponse<List<LeaveResponse>>> findAllPendingLeaves(
            @RequestHeader(value = "Authorization", required = false) String token) {
        return ResponseEntity.ok(BaseResponse.<List<LeaveResponse>>builder()
                .data(leaveService.findAllPendingLeaves(token))
                .message("Beklemede olan izin istekleri listesi.")
                .build());
    }

    @Operation(summary = "Çalışanın yapmış olduğu tüm izin isteklerini listeler.")
    @GetMapping("/employee-leave-requests")
    public ResponseEntity<BaseResponse<List<LeaveDetailsResponse>>> findAllLeaves(
            @RequestHeader(value = "Authorization", required = false) String token) {
        return ResponseEntity.ok(BaseResponse.<List<LeaveDetailsResponse>>builder()
                .data(leaveService.findAllLeavesByEmployeeId(token))
                .message("Çalışana ait izin istekleri listesi.")
                .build());

    }

    @Operation(summary = "Girilen ID'ye ait iznin detaylarını döner.")
    @GetMapping("{leaveId}")
    public ResponseEntity<BaseResponse<LeaveDetailsResponse>> findById(
            @RequestHeader(value = "Authorization", required = false) String token,
            @PathVariable Long leaveId) {
        return ResponseEntity.ok(BaseResponse.<LeaveDetailsResponse>builder()
                .data(leaveService.findLeaveDetails(token, leaveId))
                .message("İzin detayları getirildi.")
                .build());
    }

    @Operation(summary = "Çalışanın yapmış olduğu izin isteğini günceller.")
    @PutMapping
    public ResponseEntity<BaseResponse<LeaveDetailsResponse>> updateLeave(
            @RequestHeader(value = "Authorization", required = false) String token,
            @RequestBody UpdateLeaveRequest dto) {
        return ResponseEntity.ok(BaseResponse.<LeaveDetailsResponse>builder()
                .success(leaveService.updateLeaveRequest(token, dto))
                .message("İzin isteği güncellendi.")
                .build());
    }

    @Operation(summary = "Soft delete.")
    @DeleteMapping("{leaveId}")
    public ResponseEntity<BaseResponse<LeaveDetailsResponse>> deleteLeave(
            @RequestHeader(value = "Authorization", required = false) String token,
            @PathVariable Long leaveId) {
        return ResponseEntity.ok(BaseResponse.<LeaveDetailsResponse>builder()
                .success(leaveService.deleteLeaveRequest(token, leaveId))
                .message("İzin isteği silindi.")
                .build());
    }

    @Operation(summary = "Çalışan yapmış olduğu izin isteğini iptal eder.")
    @PatchMapping("/cancel/{leaveId}")
    public ResponseEntity<BaseResponse<LeaveDetailsResponse>> cancelLeave(
            @RequestHeader(value = "Authorization", required = false) String token,
            @PathVariable Long leaveId) {
        return ResponseEntity.ok(BaseResponse.<LeaveDetailsResponse>builder()
                .success(leaveService.cancelLeaveRequest(token, leaveId))
                .message("İzin isteği iptal edildi.")
                .build());
    }

    @Operation(summary = "Yönetici çalışanın yapmış olduğu izin isteğini onaylar.")
    @PatchMapping("/approve/{leaveId}")
    public ResponseEntity<BaseResponse<LeaveDetailsResponse>> approveLeave(
            @RequestHeader(value = "Authorization", required = false) String token,
            @PathVariable Long leaveId) {
        return ResponseEntity.ok(BaseResponse.<LeaveDetailsResponse>builder()
                .success(leaveService.approveLeaveRequest(token, leaveId))
                .message("İzin isteği onaylandı.")
                .build());
    }

    @Operation(summary = "Yönetici çalışanın yapmış olduğu izin isteğini reddeder.")
    @PatchMapping("/reject/{leaveId}")
    public ResponseEntity<BaseResponse<LeaveDetailsResponse>> rejectLeave(
            @RequestHeader(value = "Authorization", required = false) String token,
            @PathVariable Long leaveId) {
        return ResponseEntity.ok(BaseResponse.<LeaveDetailsResponse>builder()
                .success(leaveService.rejectLeaveRequest(token, leaveId))
                .message("İzin isteği reddedildi.")
                .build());
    }
}
