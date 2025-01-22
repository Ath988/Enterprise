package com.bilgeadam.enterprise.controller;

import com.bilgeadam.enterprise.dto.request.LoginRequestDto;
import com.bilgeadam.enterprise.dto.request.RegisterRequestDto;
import com.bilgeadam.enterprise.dto.response.BaseResponse;
import com.bilgeadam.enterprise.entity.User;
import com.bilgeadam.enterprise.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.bilgeadam.enterprise.constant.RestApis.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(USER)
@CrossOrigin("*")
public class UserController {
    private final UserService userService;


    @PostMapping(DOLOGIN)
    public ResponseEntity<BaseResponse<String>> doLogin(@RequestBody @Valid LoginRequestDto dto) {
        return ResponseEntity.ok(BaseResponse.<String>builder()
                        .code(200)
                        .data(userService.doLogin(dto))
                        .message("Basariyla giris islemi tamamlanmistir!")
                        .success(true)
                .build());

    }
    @PostMapping(DOREGISTER)
    public ResponseEntity<BaseResponse<Boolean>> doRegister(@RequestBody @Valid RegisterRequestDto dto) {
        return ResponseEntity.ok(BaseResponse.<Boolean>builder()
                        .success(true)
                        .data(userService.doRegister(dto))
                        .code(200)
                        .message("Kayit olma islemi basariyla tamamlanmistir!\nHesabinizi aktiflestirmek icin e-postanizi kotrol ediniz!")
                .build());
    }

}
