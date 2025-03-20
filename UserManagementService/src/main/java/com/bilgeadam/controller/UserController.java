package com.bilgeadam.controller;

import com.bilgeadam.dto.request.CreateMemberRequest;
import com.bilgeadam.dto.request.otherServices.ManageEmployeePermissionsRequest;
import com.bilgeadam.dto.response.*;
import com.bilgeadam.exception.UserManagementException;
import com.bilgeadam.utility.AuthUtil;
import com.bilgeadam.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.bilgeadam.constants.RestApis.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(USER)
@CrossOrigin("*")

public class UserController {
    private final UserService userService;
    private final AuthUtil authUtil;

    @PostMapping(CREATE_MEMBER)
    public ResponseEntity<BaseResponse<Boolean>> createMember(@RequestBody CreateMemberRequest dto){
        return ResponseEntity.ok(BaseResponse.<Boolean>builder()
                        .message("Yeni üye kaydı başarı ile oluşturuldu.")
                        .success(userService.createMember(dto))
                .build());
    }


    @PostMapping(CREATE_USER)
    public ResponseEntity<BaseResponse<Boolean>> createUser(
            @RequestHeader(value = "Authorization", required = false) String token,
            @RequestBody CreateMemberRequest dto){
        return ResponseEntity.ok(BaseResponse.<Boolean>builder()
                .message("Yeni çalışan kaydı başarı ile oluşturuldu.")
                .success(userService.createUser(token,dto))
                .build());
    }

    @GetMapping
    public ResponseEntity<BaseResponse<UserProfileResponse>> getProfile(
            @RequestHeader(value = "Authorization", required = false) String token){
        return ResponseEntity.ok(BaseResponse.<UserProfileResponse>builder()
                        .message("Kullanıcı profili yüklendi.")
                        .data(userService.getUserProfile(token))
                .build());
    }

    @GetMapping("/user-permission-response")
    public ResponseEntity<BaseResponse<UserPermissionResponse>> getUserPermission(@RequestParam Long authId){
        return ResponseEntity.ok(BaseResponse.<UserPermissionResponse>builder()
                        .data(userService.findUserPermissionResponse(authId))
                .build());
    }

    @PostMapping("/manage-user-permissions")
    public ResponseEntity<BaseResponse<Boolean>> manageUserPermissions(@RequestBody ManageEmployeePermissionsRequest dto){
        return ResponseEntity.ok(BaseResponse.<Boolean>builder()
                        .success(userService.updateUserPermissionsByAuthIdList(dto))
                        .message("Çalışan sayfa görüntüleme izin yetkileri başarı ile güncellendi.")
                .build());
    }

    @Operation(summary = "Bütün servislerdeki yetki işlemlerini rol,permission ve subscripton mapi olarak döner.")
    @GetMapping("/get-all-authorizations")
    public ResponseEntity<BaseResponse<AuthUtil>> test(){
        return ResponseEntity.ok(BaseResponse.<AuthUtil>builder()
                        .data(authUtil)
                .build());
    }
    
    @GetMapping("/get-user-detail-for-chat/{employeeId}")
    public ResponseEntity<BaseResponse<UserDetailsForChatResponse>> getEmployeeDetailForChat(@PathVariable Long employeeId) {
        try {
            UserDetailsForChatResponse employeeDetail = userService.getUserDetailForChat(employeeId);
            return ResponseEntity.ok(BaseResponse.<UserDetailsForChatResponse>builder()
                                                 .code(200)
                                                 .message("Employee detail retrieved successfully!")
                                                 .success(true)
                                                 .data(employeeDetail)
                                                 .build());
        } catch (UserManagementException e) {
            return ResponseEntity.status(404).body(BaseResponse.<UserDetailsForChatResponse>builder()
                                                               .code(404)
                                                               .message(e.getMessage())
                                                               .success(false)
                                                               .build());
        }
    }

    @GetMapping(GET_ADMINS_FOR_CHAT)
    public ResponseEntity<BaseResponse<List<AdminDetailsForChatResponse>>> getAdminsForChat(){
        return ResponseEntity.ok(BaseResponse.<List<AdminDetailsForChatResponse>>builder()
                        .code(200).message("Admins retrieved successfully").success(true)
                        .data(userService.getAdminsForChat())
                .build());
    }
    
    @GetMapping("/get-users-by-company/{companyId}/{employeeId}")
    public ResponseEntity<BaseResponse<List<UserDetailsForChatResponse>>> getEmployeesDetailByCompanyId(@PathVariable Long companyId, @PathVariable Long employeeId) {
        List<UserDetailsForChatResponse> employees = userService.getUsersDetailByCompanyId(companyId, employeeId);
        
        return ResponseEntity.ok(BaseResponse.<List<UserDetailsForChatResponse>>builder()
                                             .code(200)
                                             .message(employees.isEmpty() ? "No employees found for the given company." : "Employees retrieved successfully!")
                                             .success(!employees.isEmpty())
                                             .data(employees)
                                             .build());
    }
    
    
    @GetMapping("/get-users-by-ids")
    public ResponseEntity<BaseResponse<List<UserDetailsForChatResponse>>> getEmployeesDetailByIds(@RequestParam List<Long> ids) {
        List<UserDetailsForChatResponse> employees = userService.getUsersDetailByIds(ids);
        
        return ResponseEntity.ok(BaseResponse.<List<UserDetailsForChatResponse>>builder()
                                             .code(200)
                                             .message(employees.isEmpty() ? "No employees found for the given IDs." : "Employees retrieved successfully!")
                                             .success(!employees.isEmpty())
                                             .data(employees)
                                             .build());
    }
    
    @PostMapping("/set-users-online-status/{employeeId}")
    public ResponseEntity<BaseResponse<Boolean>> setUsersOnlineStatus(@PathVariable Long employeeId,
                                                                      @RequestParam boolean status) {
        System.out.println(employeeId+" online status degisiyor mu?");
        boolean updated = userService.setUserOnlineStatus(employeeId, status);
        return ResponseEntity.ok(BaseResponse.<Boolean>builder()
                                             .code(updated ? 200 : 404)
                                             .message(updated ? "User status updated successfully" : "User not found")
                                             .success(updated)
                                             .data(updated)
                                             .build());
    }






}