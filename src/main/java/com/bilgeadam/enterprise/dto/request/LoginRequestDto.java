package com.bilgeadam.enterprise.dto.request;

import jakarta.validation.constraints.*;

public record LoginRequestDto(
        @Email
        @NotNull
        @NotEmpty
        @NotBlank
        String email,
        @NotNull
        @Size(min = 8, max = 64)
        @Pattern(
                message = "Şifreniz en az 8 en fazla 64 karakter olmalı, Şirenizde En az Bir büyük bir küçük harf ve özel karakter olmalıdır.",
                regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=*!.,?/])(?=\\S+$).{8,}$"
        )
        String password
) {
}
