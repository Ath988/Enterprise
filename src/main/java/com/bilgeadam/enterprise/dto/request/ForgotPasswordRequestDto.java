package com.bilgeadam.enterprise.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

public record ForgotPasswordRequestDto(
        @Email
        @NotBlank (message = "sifre tekrar alani bos birakilamaz!")
        @NotEmpty (message = "sifre tekrar alani bos birakilamaz! Sadece Bosluk karakteri gecerli degildir!")
        String email
) {
}
