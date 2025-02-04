    package com.bilgeadam.controller;

    import com.bilgeadam.dto.request.AddEmployeeRequest;
    import com.bilgeadam.dto.request.AssignEmployeesToDepartmentRequest;
    import com.bilgeadam.dto.request.CreateCompanyManagerRequest;
    import com.bilgeadam.dto.request.UpdateEmployeeRequest;
    import com.bilgeadam.dto.response.AllEmployeeResponse;
    import com.bilgeadam.dto.response.BaseResponse;
    import com.bilgeadam.dto.response.EmployeeDetailResponse;
    import com.bilgeadam.service.EmployeeService;
    import lombok.RequiredArgsConstructor;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.*;

    import java.util.List;

    import static com.bilgeadam.constants.RestApis.*;

    @RestController
    @RequiredArgsConstructor
    @RequestMapping(EMPLOYEE)
    public class EmployeeController {
        private final EmployeeService employeeService;

        @PostMapping(CREATE_COMPANY_MANAGER)
        public ResponseEntity<BaseResponse<Boolean>> createCompanyManager(@RequestBody CreateCompanyManagerRequest dto){
            return ResponseEntity.ok(BaseResponse.<Boolean>builder()
                            .success(employeeService.createCompanyManager(dto))
                            .message("Şirket sahibi hesabı oluşturuldu.")
                    .build());
        }


        //Yeni çalışan ekler.
        @PostMapping
        public ResponseEntity<BaseResponse<Boolean>> addEmployee(
                @RequestHeader(value = "Authorization", required = false) String token,
                @RequestBody AddEmployeeRequest dto) {
            return ResponseEntity.ok(BaseResponse.<Boolean>builder()
                    .success(employeeService.addNewEmployee(token,dto))
                    .message("Yeni çalışan eklendi.")
                    .build());
        }

        //Tüm çalışanların özet bilgilerini getirir.
        @GetMapping
        public ResponseEntity<BaseResponse<List<AllEmployeeResponse>>> getAllEmployees(
                @RequestHeader(value = "Authorization", required = false) String token) {
            return ResponseEntity.ok(BaseResponse.<List<AllEmployeeResponse>>builder()
                    .data(employeeService.findAllEmployees(token))
                    .message("Firma çalışanlarının listesi getirildi.")
                    .build());
        }


        //Çalışan detaylarını getirir.
        @GetMapping("{employeeId}")
        public ResponseEntity<BaseResponse<EmployeeDetailResponse>> getEmployeeDetail(
                @RequestHeader(value = "Authorization", required = false) String token,
                @PathVariable Long employeeId) {
            return ResponseEntity.ok(BaseResponse.<EmployeeDetailResponse>builder()
                    .data(employeeService.findEmployeeDetailById(token,employeeId))
                    .message("Çalışan detay bilgeri getirildi.")
                    .build());
        }

        //Çalışanın üst yöneticilerini döner
        @GetMapping("{employeeId}/hierarchy")
        public ResponseEntity<BaseResponse<List<AllEmployeeResponse>>> getEmployeeHierarchy(
                @RequestHeader(value = "Authorization", required = false) String token,
                @PathVariable Long employeeId) {
            return ResponseEntity.ok(BaseResponse.<List<AllEmployeeResponse>>builder()
                    .data(employeeService.findEmployeeHierarchy(token,employeeId))
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
            Boolean success = employeeService.deleteEmployee(token,employeeId);
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
            Boolean success = employeeService.updateEmployee(token,dto);
            return ResponseEntity.ok(BaseResponse.<Boolean>builder()
                    .success(success)
                    .message(success ?
                            "Çalışan bilgileri güncellendi." :
                            "Çalışan bilgileri güncellenirken bir sorun oluştu.")
                    .build());
        }


        //Belirli bir departmanın çalışanlarını getirir.
        @GetMapping("{departmentId}/employees")
        public ResponseEntity<BaseResponse<List<AllEmployeeResponse>>> getEmployeesOfDepartment(@PathVariable Long departmentId){
            return ResponseEntity.ok(BaseResponse.<List<AllEmployeeResponse>>builder()
                            .message("Seçilen departmana ait çalışanlar listesi.")
                            .data(employeeService.findAllEmployeesByDepartmentId(departmentId))
                    .build());
        }




    }
