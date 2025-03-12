package com.bilgeadam.controller;

import com.bilgeadam.dto.request.CreateMaintenanceRequestDto;
import com.bilgeadam.dto.request.UpdateMaintenanceRequestDto;
import com.bilgeadam.dto.response.BaseResponse;
import com.bilgeadam.dto.response.MaintenanceResponseDto;
import com.bilgeadam.service.MaintenanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.bilgeadam.constants.RestApis.MAINTENANCE;

@RestController
@RequestMapping(MAINTENANCE)
@RequiredArgsConstructor
public class MaintenanceController {
    private final MaintenanceService maintenanceService;

    @PostMapping("/create-maintenance")
    public ResponseEntity<BaseResponse<Boolean>> createMaintenance(@RequestBody CreateMaintenanceRequestDto dto,
                                                                   @RequestHeader(value = "Authorization", required = false) String token){
        return ResponseEntity.ok(BaseResponse.<Boolean>builder()
                        .code(200)
                        .success(true)
                        .message("Bakım talebi oluşturma işlemi başarılı")
                        .data(maintenanceService.createMaintenance(dto, token))
                .build());
    }

    @GetMapping("/get-all-maintenance")
    public ResponseEntity<BaseResponse<List<MaintenanceResponseDto>>> getAllMaintenance(
            @RequestHeader(value = "Authorization", required = false) String token){
        return ResponseEntity.ok(BaseResponse.<List<MaintenanceResponseDto>>builder()
                        .code(200)
                        .success(true)
                        .message("Bakım talebi listeleme işlemi başarılı")
                        .data(maintenanceService.getAllMaintenance(token))
                .build());
    }

    @PutMapping("/update-maintenance")
    public ResponseEntity<BaseResponse<Boolean>> updateMaintenance(@RequestBody UpdateMaintenanceRequestDto dto,
                                                                   @RequestHeader(value = "Authorization", required = false) String token){
        return ResponseEntity.ok(BaseResponse.<Boolean>builder()
                        .code(200)
                        .success(true)
                        .message("Güncelleme İşleminiz Başarılı")
                        .data(maintenanceService.updateMaintenance(dto, token))
                .build());
    }

    @PutMapping("/delete-maintenance")
    public ResponseEntity<BaseResponse<Boolean>> deleteMaintenance(Long maintenanceId,
            @RequestHeader(value = "Authorization", required = false) String token){
        return ResponseEntity.ok(BaseResponse.<Boolean>builder()
                        .code(200)
                        .success(true)
                        .message("Bakım talebi silme işleminiz başarılı")
                        .data(maintenanceService.deleteMaintenance(token, maintenanceId))
                .build());
    }
}
