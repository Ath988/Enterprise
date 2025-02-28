package com.bilgeadam.controller;

import static com.bilgeadam.constants.RestApis.*;

import com.bilgeadam.dto.request.AddAssetRequestDto;
import com.bilgeadam.dto.request.UpdateAssetRequestDto;
import com.bilgeadam.dto.response.AllEmployeeResponse;
import com.bilgeadam.dto.response.AssetResponseDto;
import com.bilgeadam.dto.response.BaseResponse;
import com.bilgeadam.entity.Asset;
import com.bilgeadam.entity.enums.EState;
import com.bilgeadam.service.AssetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(ASSET)
@RequiredArgsConstructor
@CrossOrigin("*")
public class AssetController {
    private final AssetService assetService;

    @PostMapping("/create-asset")
    public ResponseEntity<BaseResponse<Boolean>> createAsset(@RequestBody AddAssetRequestDto dto,
     @RequestHeader(value = "Authorization", required = false) String token){
        return ResponseEntity.ok(BaseResponse.<Boolean>builder()
                        .code(200)
                        .message("Varlık ekleme işlemi başarılı")
                        .success(true)
                        .data(assetService.createAsset(dto, token))
                .build());
    }

    @GetMapping("/get-all-employees")
    public ResponseEntity<BaseResponse<List<AllEmployeeResponse>>> getAllEmployees(
            @RequestHeader(value = "Authorization", required = false) String token){
        return ResponseEntity.ok(BaseResponse.<List<AllEmployeeResponse>>builder()
                        .code(200)
                        .message("Çalışan listesi başarıyla getirildi.")
                        .success(true)
                        .data(assetService.getAllEmployees(token, Optional.of(EState.ACTIVE)))
                .build());
    }

    @GetMapping("/get-all-list")
    public ResponseEntity<BaseResponse<List<AssetResponseDto>>> getAllAssets(
            @RequestHeader(value = "Authorization", required = false) String token){
        return ResponseEntity.ok(BaseResponse.<List<AssetResponseDto>>builder()
                        .code(200)
                        .message("Varlık listesi başarıyla getirildi.")
                        .success(true)
                        .data(assetService.getAllAssets(token))
                .build());
    }

    @PutMapping("/update-asset")
    public ResponseEntity<BaseResponse<Boolean>> updateAsset(
            @RequestBody UpdateAssetRequestDto dto,
            @RequestHeader(value = "Authorization", required = false) String token){
        return ResponseEntity.ok(BaseResponse.<Boolean>builder()
                        .code(200)
                        .success(true)
                        .message("Varlık güncelleme işlemi başarılı.")
                        .data(assetService.updateAsset(dto, token))
                .build());
    }

    @PutMapping("/delete-asset")
    public ResponseEntity<BaseResponse<Boolean>> deleteAsset(Long assetId, @RequestHeader(value = "Authorization", required = false) String token){
        return ResponseEntity.ok(BaseResponse.<Boolean>builder()
                        .code(200)
                        .success(true)
                        .message("Varlık silme işlemi başarılı.")
                        .data(assetService.deleteAsset(token, assetId))
                .build());
    }
}
