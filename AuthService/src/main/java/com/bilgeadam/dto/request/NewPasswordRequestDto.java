package com.bilgeadam.dto.request;

import jakarta.validation.constraints.*;

public record NewPasswordRequestDto(
        String authCode,
        @NotEmpty (message = "sifre tekrar alani bos birakilamaz!")
        @NotBlank (message = "sifre tekrar alani bos birakilamaz! Sadece Bosluk karakteri gecerli degildir!")
        @Pattern(
                message = "Şifreniz en az 8 en fazla 64 karakter olmalı, Şirenizde En az Bir büyük bir küçük harf ve özel karakter olmalıdır.",
                regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=*!.,?/])(?=\\S+$).{8,}$"
        )
        String password,
        @NotEmpty(message = "sifre tekrar alani bos birakilamaz!")
        @NotBlank(message = "sifre tekrar alani bos birakilamaz! Sadece Bosluk karakteri gecerli degildir!")
        @Size(min = 8, max = 64)
        @Pattern(
                message = "Şifreniz en az 8 en fazla 64 karakter olmalı, Şirenizde En az Bir büyük bir küçük harf ve özel karakter olmalıdır.",
                regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=*!.,?/])(?=\\S+$).{8,}$"
        )
        String rePassword
) {
}
