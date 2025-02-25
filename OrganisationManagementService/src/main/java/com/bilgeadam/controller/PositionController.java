package com.bilgeadam.controller;

import com.bilgeadam.dto.request.AddEmployeeRequest;
import com.bilgeadam.dto.request.AddNewPositionRequest;
import com.bilgeadam.dto.request.AssignPositionToEmployeeListRequest;
import com.bilgeadam.dto.request.UpdatePositionRequest;
import com.bilgeadam.dto.response.BaseResponse;
import com.bilgeadam.dto.response.OrganizationTreeResponse;
import com.bilgeadam.dto.response.PositionDetailResponse;
import com.bilgeadam.dto.response.PositionTreeResponse;
import com.bilgeadam.entity.Position;
import com.bilgeadam.service.PositionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.bilgeadam.constants.RestApis.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(POSITION)
@CrossOrigin("*")
public class PositionController {
    private final PositionService positionService;


    @PostMapping("/add-position")
    public ResponseEntity<BaseResponse<Boolean>> addPosition(
            @RequestHeader(value = "Authorization", required = false) String token,
            @RequestBody AddNewPositionRequest dto) {
        return ResponseEntity.ok(BaseResponse.<Boolean>builder()
                .success(positionService.addNewPosition(token, dto))
                .message("Yeni pozisyon eklendi.")
                .build());
    }

    @PutMapping("/update-position")
    public ResponseEntity<BaseResponse<Boolean>> updatePosition(
            @RequestHeader(value = "Authorization", required = false) String token,
            @RequestBody UpdatePositionRequest dto) {
        return ResponseEntity.ok(BaseResponse.<Boolean>builder()
                .success(positionService.updatePosition(token, dto))
                .message("Pozisyon bilgileri güncellendi.")
                .build());
    }

    @DeleteMapping("/delete-position/{positionId}")
    public ResponseEntity<BaseResponse<Boolean>> deletePosition(
            @RequestHeader(value = "Authorization", required = false) String token,
            @PathVariable Long positionId) {
        return ResponseEntity.ok(BaseResponse.<Boolean>builder()
                .success(positionService.deletePosition(token, positionId))
                .message("Pozisyon başarıyla silindi.")
                .build());
    }

    @GetMapping("/all-positions/{companyId}")
    public ResponseEntity<BaseResponse<List<PositionDetailResponse>>> getAllPositions(@PathVariable Long companyId){
        return ResponseEntity.ok(BaseResponse.<List<PositionDetailResponse>>builder()
                .data(positionService.findAllPositionsByCompanyId(companyId))
                .message("Pozisyon detayları getirildi.")
                .build());
    }


    @GetMapping("{positionId}")
    public ResponseEntity<BaseResponse<PositionDetailResponse>> getPositionDetail(@PathVariable Long positionId) {
        return ResponseEntity.ok(BaseResponse.<PositionDetailResponse>builder()
                .data(positionService.findPositionDetailById(positionId))
                .message("Pozisyon detayları getirildi.")
                .build());
    }

    @GetMapping("{departmentId}/all-positions-in-department")
    public ResponseEntity<BaseResponse<List<PositionDetailResponse>>> getAllPositionsInDepartment(@PathVariable Long departmentId){
        return ResponseEntity.ok(BaseResponse.<List<PositionDetailResponse>>builder()
                .data(positionService.findAllPositionsByDepartmentId(departmentId))
                .message("Departmana ait tüm pozisyonlar")
                .build());
    }


    @PutMapping("/assign-position-to-employees")
    public ResponseEntity<BaseResponse<Boolean>> assignPositionToEmployees(
            @RequestHeader(value = "Authorization", required = false) String token,
            @RequestBody AssignPositionToEmployeeListRequest dto) {
        return ResponseEntity.ok(BaseResponse.<Boolean>builder()
                .success(positionService.assignPositionToEmployeeList(token, dto))
                .message("Seçilen pozisyon listedeki çalışanlara atandı.")
                .build());
    }
    
    @GetMapping("/{companyId}/organization-tree")
    public ResponseEntity<BaseResponse<PositionTreeResponse>> getPositionTree(@PathVariable Long companyId) {
        return ResponseEntity.ok(BaseResponse.<PositionTreeResponse>builder()
                                             .data(positionService.getPositionTree(companyId))
                                             .message("Şirket organizasyon şeması position'a göre getirildi.")
                                             .build());
    }
}