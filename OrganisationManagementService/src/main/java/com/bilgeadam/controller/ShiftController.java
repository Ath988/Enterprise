package com.bilgeadam.controller;

import com.bilgeadam.dto.request.*;
import com.bilgeadam.dto.response.BaseResponse;
import com.bilgeadam.entity.Shift;
import com.bilgeadam.service.ShiftService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import static com.bilgeadam.constants.RestApis.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(SHIFT)
@CrossOrigin("*")
public class ShiftController {
    private final ShiftService shiftService;

    @PostMapping(CREATE)
    public ResponseEntity<BaseResponse<Boolean>> createShift(@RequestBody @Valid CreateShiftRequestDto dto,
                                                          @RequestHeader(value = "Authorization", required = false) String token) {
        return ResponseEntity.ok(BaseResponse.<Boolean>builder()
                .code(200)
                .success(true)
                .message("Vardiya başarıyla oluşturuldu!")
                .data(shiftService.createShift(dto,token))
                .build());
    }

    @GetMapping(GETALL)
    public ResponseEntity<BaseResponse<List<Shift>>> getAllShifts(@RequestHeader(value = "Authorization", required = false) String token) {
        return ResponseEntity.ok(BaseResponse.<List<Shift>>builder()
                .code(200)
                .success(true)
                .message("Tüm vardiyalar başarıyla getirildi.")
                .data(shiftService.getAllShifts(token))
                .build());
    }

    @PutMapping(UPDATE)
    public ResponseEntity<BaseResponse<Boolean>> updateShift(@RequestBody UpdateShiftRequestDto dto,
                                                             @RequestHeader(value = "Authorization", required = false) String token) {
        return ResponseEntity.ok(BaseResponse.<Boolean>builder()
                .code(200)
                .success(true)
                .message("Vardiya bilgisi başarıyla güncellendi.")
                .data(shiftService.updateShift(dto,token))
                .build());
    }

    @DeleteMapping(DELETE)
    public ResponseEntity<BaseResponse<Boolean>> deleteShift(Long shiftId, @RequestHeader(value = "Authorization", required = false) String token) {
        return ResponseEntity.ok(BaseResponse.<Boolean>builder()
                .code(200)
                .success(true)
                .message("Vardiya başarıyla silindi.")
                .data(shiftService.deleteShift(shiftId,token))
                .build());
    }
}
