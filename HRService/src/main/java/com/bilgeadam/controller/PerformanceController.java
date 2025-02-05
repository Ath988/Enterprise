package com.bilgeadam.controller;

import com.bilgeadam.dto.request.AddEmployeeRecordRequest;
import com.bilgeadam.dto.request.AddPerformanceRequest;
import com.bilgeadam.dto.response.BaseResponse;
import com.bilgeadam.dto.response.PerformanceResponse;
import com.bilgeadam.service.PerformanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.bilgeadam.constants.RestApis.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(PERFORMANCE)
public class PerformanceController {
    private final PerformanceService performanceService;

    @GetMapping("{performanceId}")
    public ResponseEntity<BaseResponse<PerformanceResponse>> getPerformance(
            @RequestHeader(value = "Authorization", required = false) String token,
            @PathVariable Long performanceId) {
        return ResponseEntity.ok(BaseResponse.<PerformanceResponse>builder()
                .message("Performans bilgileri getirildi.")
                        .data(performanceService.getPerformance(token, performanceId))
                .build());
    }

    @PostMapping
    public ResponseEntity<BaseResponse<Boolean>> addPerformance(
            @RequestHeader(value = "Authorization", required = false) String token,
            @RequestBody AddPerformanceRequest dto) {
        return ResponseEntity.ok(BaseResponse.<Boolean>builder()
                .success(performanceService.addPerformance(token, dto))
                .message("Yeni performans kaydı oluşturuldu.")
                .build());
    }


}
