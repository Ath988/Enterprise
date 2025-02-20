package com.bilgeadam.controller;

import com.bilgeadam.dto.request.CreateMemberRequest;
import com.bilgeadam.dto.response.BaseResponse;
import com.bilgeadam.dto.response.UserPermissionResponse;
import com.bilgeadam.dto.response.UserProfileResponse;
import com.bilgeadam.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.bilgeadam.constants.RestApis.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(USER)
@CrossOrigin("*")

public class UserController {
    private final UserService userService;

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





}
