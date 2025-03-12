package com.bilgeadam.controller;

import com.bilgeadam.dto.request.*;
import com.bilgeadam.dto.response.BaseResponse;
import com.bilgeadam.entity.Auth;
import com.bilgeadam.exception.EnterpriseException;
import com.bilgeadam.exception.ErrorType;
import com.bilgeadam.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.net.URI;

import static com.bilgeadam.constant.RestApis.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(AUTH)
@CrossOrigin("*")
public class AuthController {
    private final AuthService authService;


    @PostMapping(DOLOGIN)
    public ResponseEntity<BaseResponse<String>> doLogin(@RequestBody @Valid LoginRequestDto dto) {
        return ResponseEntity.ok(BaseResponse.<String>builder()
                        .code(200)
                        .data(authService.doLogin(dto))
                        .message("Basariyla giris islemi tamamlanmistir!")
                        .success(true)
                .build());

    }
    @PostMapping(DOREGISTER)
    public ResponseEntity<BaseResponse<Long>> doRegister(@RequestBody @Valid RegisterRequestDto dto) {
        return ResponseEntity.ok(BaseResponse.<Long>builder()
                        .success(true)
                        .data(authService.doRegister(dto))
                        .code(200)
                        .message("Kayit olma islemi basariyla tamamlanmistir!\nHesabinizi aktiflestirmek icin e-postanizi kotrol ediniz!")
                .build());
    }

    @PostMapping("/create-employee")
    public ResponseEntity<BaseResponse<Long>> registerEmployee(
            @RequestHeader(value = "Authorization", required = false) String token,
            @RequestBody @Valid RegisterRequestDto dto) {
        return ResponseEntity.ok(BaseResponse.<Long>builder()
                .success(true)
                .data(authService.registerEmployee(token,dto))
                .code(200)
                .message("Kayit olma islemi basariyla tamamlanmistir!\nHesabinizi aktiflestirmek icin e-postanizi kotrol ediniz!")
                .build());
    }

    @GetMapping(AUTHMAIL)
    public ResponseEntity<BaseResponse<Boolean>> authUser(@RequestParam(name = "auth") String authCode) {
        authService.authUserRegister(authCode);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create("http://localhost:5173"));
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }

    @PostMapping(FORGOT_PASSWORD_MAIL)
    public ResponseEntity<BaseResponse<Boolean>> forgotPasswordMail(@RequestBody ForgotPasswordRequestDto dto) {
        return ResponseEntity.ok(BaseResponse.<Boolean>builder().success(true)
                .message("Yeni sifre olusturma linki mail adresine gonderilmistir!")
                .data(authService.forgotPasswordMail(dto.email()))
                .code(200)
                .build());
    }

    @GetMapping(NEW_PASSWORD)
    public RedirectView setNewPassword(@RequestParam(name = "auth") String authCode) {
        authService.checkAuthUser(authCode);

        return new RedirectView("http://localhost:5173/set-new-password" + "?code=" + authCode);

    }

    @PostMapping(NEW_PASSWORD)
    public ResponseEntity<BaseResponse<Boolean>> setNewPassword(@RequestBody @Valid NewPasswordRequestDto dto) {
        if (!dto.password().equals(dto.rePassword())) {
            throw new EnterpriseException(ErrorType.INVALID_PASSWORD);
        }

        return ResponseEntity.ok(BaseResponse.<Boolean>builder()
                .success(true)
                .message("Yeni sifre basiriyla olsuturuldu!")
                .data(authService.updateUserForgotPassword(dto))
                .code(200)
                .build());
    }

    @GetMapping(GETPROFILE)
    public ResponseEntity<BaseResponse<Auth>> getUserProfile(@RequestParam String token) {
        return ResponseEntity.ok(BaseResponse.<Auth>builder()
                .code(200)
                .success(true)
                .message("Kullanıcı bilgisi başarıyla getirildi.")
                .data(authService.getUserProfile(token))
                .build());
    }

    @PutMapping(UPDATEPROFILE)
    public ResponseEntity<BaseResponse<Boolean>> updateProfile(@RequestBody @Valid UpdateProfileRequestDto dto) {
        return ResponseEntity.ok(BaseResponse.<Boolean>builder()
                .code(200)
                .success(true)
                .message("Güncelleme işlemi başarılı")
                .data(authService.updateUserProfile(dto))
                .build());
    }

    @PutMapping(UPDATEPASSWORD)
    public ResponseEntity<BaseResponse<Boolean>> updatePasswordProfile(@RequestBody @Valid UpdatePasswordProfileRequestDto dto) {
        return ResponseEntity.ok(BaseResponse.<Boolean>builder()
                .code(200)
                .success(true)
                .message("Şifre Güncelleme işlemi başarılı!")
                .data(authService.updateUserPassword(dto))
                .build());

    }

}