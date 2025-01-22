package com.bilgeadam.enterprise.controller;

import com.bilgeadam.enterprise.dto.request.AddEmployeeRequest;
import com.bilgeadam.enterprise.dto.response.AllEmployeeResponse;
import com.bilgeadam.enterprise.dto.response.BaseResponse;
import com.bilgeadam.enterprise.dto.response.EmployeeDetailResponse;
import com.bilgeadam.enterprise.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.bilgeadam.enterprise.constants.RestApis.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(EMPLOYEE)
public class EmployeeController {
    private final EmployeeService employeeService;

    //Yeni çalışan ekler.
    @PostMapping
    public ResponseEntity<BaseResponse<Boolean>> addEmployee(@RequestBody AddEmployeeRequest dto) {
        return ResponseEntity.ok(BaseResponse.<Boolean>builder()
                .success(employeeService.addNewEmployee(dto))
                .message("Yeni çalışan eklendi.")
                .build());
    }

    //Tüm çalışanların özet bilgilerini getirir. Sonradan tokena göre tekrar düzenlenicek.
    @GetMapping
    public ResponseEntity<BaseResponse<List<AllEmployeeResponse>>> getAllEmployees(@RequestParam Long companyId) {
        return ResponseEntity.ok(BaseResponse.<List<AllEmployeeResponse>>builder()
                .data(employeeService.findAllEmployees(companyId))
                .message("Tüm çalışanların listesi getirildi.")
                .build());
    }

    //Todo: Parametre olarak girilecek Tokendan companyId çekilip çalışanın yönetici ile aynı şirkette olup
    // olmadığı kontrol edilecek.
    @GetMapping("{employeeId}")
    public ResponseEntity<BaseResponse<EmployeeDetailResponse>> getEmployeeDetail(@PathVariable Long employeeId) {
        return ResponseEntity.ok(BaseResponse.<EmployeeDetailResponse>builder()
                .data(employeeService.findEmployeeById(employeeId))
                .message("Çalışan detay bilgeri getirildi.")
                .build());
    }

    //Çalışanın üst yöneticilerini döner
    @GetMapping("{employeeId}/hierarchy")
    public ResponseEntity<BaseResponse<List<AllEmployeeResponse>>> getEmployeeHierarchy(@PathVariable Long employeeId) {
        return ResponseEntity.ok(BaseResponse.<List<AllEmployeeResponse>>builder()
                        .data(employeeService.findEmployeeHierarchy(employeeId))
                        .message("Çalışan üst yöneticileri")
                .build());
    }

    //Yöneticiye bağlı çalışanları döner
    @GetMapping("{employeeId}/subordinates")
    public ResponseEntity<BaseResponse<List<AllEmployeeResponse>>> getEmployeeSubordinates(@PathVariable Long employeeId) {
        return ResponseEntity.ok(BaseResponse.<List<AllEmployeeResponse>>builder()
                .data(employeeService.findEmployeeSubordinates(employeeId))
                .message("Çalışan alt ekibi")
                .build());
    }

    //Todo: Çalışan ekle,sil






}
