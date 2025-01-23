package com.bilgeadam.controller;

import com.bilgeadam.dto.request.LoginRequestDto;
import com.bilgeadam.dto.request.RegisterRequestDto;
import com.bilgeadam.dto.request.ForgotPasswordRequestDto;
import com.bilgeadam.dto.request.NewPasswordRequestDto;
import com.bilgeadam.dto.response.BaseResponse;
import com.bilgeadam.exception.EnterpriseException;
import com.bilgeadam.exception.ErrorType;
import com.bilgeadam.service.UserService;
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

    @GetMapping(AUTHMAIL)
    public ResponseEntity<BaseResponse<Boolean>> authUser(@RequestParam(name = "auth") String authCode) {
        userService.authUserRegister(authCode);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create("http://localhost:3000"));
        return new ResponseEntity<BaseResponse<Boolean>>(headers, HttpStatus.FOUND);
    }

    @PostMapping(FORGOT_PASSWORD_MAIL)
    public ResponseEntity<BaseResponse<Boolean>> forgotPasswordMail(@RequestBody ForgotPasswordRequestDto dto) {
        return ResponseEntity.ok(BaseResponse.<Boolean>builder().success(true)
                .message("Yeni sifre olusturma linki mail adresine gonderilmistir!")
                .data(userService.forgotPasswordMail(dto.email()))
                .code(200)
                .build());
    }

    @GetMapping(NEW_PASSWORD)
    public RedirectView setNewPassword(@RequestParam(name = "auth") String authCode) {
        userService.checkAuthUser(authCode);

        return new RedirectView("http://localhost:3000/set-new-password" + "?code=" + authCode);

    }

    @PostMapping(NEW_PASSWORD)
    public ResponseEntity<BaseResponse<Boolean>> setNewPassword(@RequestBody @Valid NewPasswordRequestDto dto) {
        if (!dto.password().equals(dto.rePassword())) {
            throw new EnterpriseException(ErrorType.INVALID_PASSWORD);
        }

        return ResponseEntity.ok(BaseResponse.<Boolean>builder()
                .success(true)
                .message("Yeni sifre basiriyla olsuturuldu!")
                .data(userService.updateUserForgotPassword(dto))
                .code(200)
                .build());
    }
}
