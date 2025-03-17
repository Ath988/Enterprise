package com.bilgeadam.controller;

import com.bilgeadam.dto.request.*;
import com.bilgeadam.dto.response.AllEmployeeResponse;
import com.bilgeadam.dto.response.BaseResponse;
import com.bilgeadam.dto.response.EmployeeDetailResponse;
import com.bilgeadam.dto.response.EmployeeSaveResponse;
import com.bilgeadam.entity.Employee;
import com.bilgeadam.entity.enums.EState;
import com.bilgeadam.service.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.bilgeadam.constants.RestApis.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(EMPLOYEE)
public class EmployeeController {
    private final EmployeeService employeeService;

    @PostMapping(CREATE_COMPANY_MANAGER)
    public ResponseEntity<BaseResponse<Boolean>> createCompanyManager(@RequestBody CreateCompanyManagerRequest dto) {
        return ResponseEntity.ok(BaseResponse.<Boolean>builder()
                .success(employeeService.createCompanyManager(dto))
                .message("Şirket sahibi hesabı oluşturuldu.")
                .build());
    }


    //Yeni çalışan ekler.
    @PostMapping
    public ResponseEntity<BaseResponse<EmployeeSaveResponse>> addEmployee(
            @RequestHeader(value = "Authorization", required = false) String token,
            @RequestBody AddEmployeeRequest dto) {
        return ResponseEntity.ok(BaseResponse.<EmployeeSaveResponse>builder()
                .data(employeeService.addNewEmployee(token, dto))
                .message("Yeni çalışan eklendi.")
                .build());
    }

    //Tüm çalışanların özet bilgilerini getirir.
    @GetMapping("/get-all-employee")
    public ResponseEntity<BaseResponse<List<AllEmployeeResponse>>> getAllEmployees(
            @RequestHeader(value = "Authorization", required = false) String token,
            @RequestParam Optional<EState> state
            ) {
        return ResponseEntity.ok(BaseResponse.<List<AllEmployeeResponse>>builder()
                .data(employeeService.findAllEmployees(token,state))
                .message("Firma çalışanlarının listesi getirildi.")
                .build());
    }


    //Çalışan detaylarını getirir.
    @GetMapping("{employeeId}")
    public ResponseEntity<BaseResponse<EmployeeDetailResponse>> getEmployeeDetail(
            @RequestHeader(value = "Authorization", required = false) String token,
            @PathVariable Long employeeId) {
        return ResponseEntity.ok(BaseResponse.<EmployeeDetailResponse>builder()
                .data(employeeService.findEmployeeDetailById(token, employeeId))
                .message("Çalışan detay bilgeri getirildi.")
                .build());
    }

    //Çalışanın üst yöneticilerini döner
    @GetMapping("{employeeId}/hierarchy")
    public ResponseEntity<BaseResponse<List<AllEmployeeResponse>>> getEmployeeHierarchy(
            @RequestHeader(value = "Authorization", required = false) String token,
            @PathVariable Long employeeId) {
        return ResponseEntity.ok(BaseResponse.<List<AllEmployeeResponse>>builder()
                .data(employeeService.findEmployeeHierarchy(token, employeeId))
                .message("Çalışan üst yöneticileri")
                .build());
    }

    //Departman yöneticisine bağlı çalışanları döner
    @GetMapping("{employeeId}/subordinates")
    public ResponseEntity<BaseResponse<List<AllEmployeeResponse>>> getEmployeeSubordinates(
            @RequestHeader(value = "Authorization", required = false) String token,
            @PathVariable Long employeeId) {
        return ResponseEntity.ok(BaseResponse.<List<AllEmployeeResponse>>builder()
                .data(employeeService.findEmployeeSubordinates(token, employeeId))
                .message("Çalışan alt ekibi")
                .build());
    }


    //Soft delete. Çalışan stateini pasif hale getirir.
    @DeleteMapping("{employeeId}")
    public ResponseEntity<BaseResponse<Boolean>> deleteEmployee(
            @RequestHeader(value = "Authorization", required = false) String token,
            @PathVariable Long employeeId) {
        Boolean success = employeeService.deleteEmployee(token, employeeId);
        return ResponseEntity.ok(BaseResponse.<Boolean>builder()
                .success(success)
                .message(success ?
                        "Çalışan başarı ile silindi." :
                        "Çalışan silinirken bir sorun oluştur.")
                .build());
    }

    //Yöneticinin çalışan bilgilerini günceller.
    @PutMapping
    public ResponseEntity<BaseResponse<Boolean>> updateEmployee(
            @RequestHeader(value = "Authorization", required = false) String token,
            @RequestBody UpdateEmployeeRequest dto) {
        Boolean success = employeeService.updateEmployee(token, dto);
        return ResponseEntity.ok(BaseResponse.<Boolean>builder()
                .success(success)
                .message(success ?
                        "Çalışan bilgileri güncellendi." :
                        "Çalışan bilgileri güncellenirken bir sorun oluştu.")
                .build());
    }


    //Belirli bir departmanın çalışanlarını getirir.
    @GetMapping("{departmentId}/employees")
    public ResponseEntity<BaseResponse<List<AllEmployeeResponse>>> getEmployeesOfDepartment(@PathVariable Long departmentId) {
        return ResponseEntity.ok(BaseResponse.<List<AllEmployeeResponse>>builder()
                .message("Seçilen departmana ait çalışanlar listesi.")
                .data(employeeService.findAllEmployeesByDepartmentId(departmentId))
                .build());
    }


    @Operation(
            summary = "Diğer servisler için",
            description = "Eğer employeeId varsa o idye ait çalışanın ismini döner, yoksa tokena sahip kişinin ismini döner.")
    @GetMapping("/employee-name")
    public ResponseEntity<BaseResponse<String>> getEmployeeNameByToken(
            @RequestHeader(value = "Authorization", required = false) String token,
            @RequestParam Optional<Long> employeeId) {
        return ResponseEntity.ok(BaseResponse.<String>builder()
                .data(employeeService.getEmployeeNameByToken(token, employeeId))
                .build());
    }

    @Operation(
            summary = "Diğer servisler için",
            description = "İşlem yapılmak istenen çalışan ile yönetici aynı şirkette mi çalışıyor kontrolü için."
    )
    @GetMapping("/check-company/{employeeId}")
    public ResponseEntity<BaseResponse<Boolean>> checkCompanyId(
            @RequestHeader(value = "Authorization", required = false) String token,
            @PathVariable Long employeeId) {
        return ResponseEntity.ok(BaseResponse.<Boolean>builder()
                .success(employeeService.checkCompanyByToken(token, employeeId))
                .build());
    }

    @Operation(summary = "Diğer servisler için, tokendan employeeId döner.")
    @GetMapping("/get-id")
    public ResponseEntity<BaseResponse<Long>> getEmployeeId(
            @RequestHeader(value = "Authorization", required = false) String token) {
        return ResponseEntity.ok(BaseResponse.<Long>builder()
                .data(employeeService.getEmployeeIdFromToken(token))
                .build());
    }

    @PostMapping("get-all-employee-names")
    public ResponseEntity<BaseResponse<Map<Long,String>>> getAllEmployeeNames(@RequestBody List<Long> employeeIdList) {
        return ResponseEntity.ok(BaseResponse.<Map<Long,String>>builder()
                        .data(employeeService.findAllEmployeeNamesByEmployeeIdList(employeeIdList))
                .build());
    }
    
    @PostMapping("/manage-employee-permissions")
    public ResponseEntity<BaseResponse<Boolean>> manageEmployeePermissions(
            @RequestHeader(value = "Authorization", required = false) String token,
            @RequestBody ManageEmployeePermissionsRequest dto){
        return ResponseEntity.ok(BaseResponse.<Boolean>builder()
                                             .success(employeeService.updateEmployeePermissions(token,dto))
                                             .message("Çalışan izinleri başarı ile güncellendi.")
                                             .build());
    }
    
    
    
    @GetMapping("get-company-id/{authId}")
    public ResponseEntity<BaseResponse<Long>> getCompanyIdByAuthId(@PathVariable Long authId){
        return ResponseEntity.ok(BaseResponse.<Long>builder()
                                         .data(employeeService.getCompanyIdByAuthId(authId))
                                             .build());
    }
    
    @GetMapping("get-employee-id/{authId}")
    public ResponseEntity<BaseResponse<Long>> getEmployeeIdByAuthId(@PathVariable Long authId){
        return ResponseEntity.ok(BaseResponse.<Long>builder()
                                         .data(employeeService.getEmployeeIdByAuthId(authId))
                                             .build());
    }
}