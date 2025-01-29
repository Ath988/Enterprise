package com.bilgeadam.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

public record NotificationMessageRequestDto(
        @NotBlank(message = "Title alanı boş bırakılamaz!")
        @NotEmpty(message = "Title alanı sadece boşluk karakterlerinden oluşturulamaz!")
        String title,

        @NotBlank(message = "Message alanı boş bırakılamaz!")
        @NotEmpty(message = "Message alanı sadece boşluk karakterlerinden oluşturulamaz!")
        String description,

        @NotBlank(message = "isRead alanı boş bırakılamaz!")
        @NotEmpty(message = "isRead alanı sadece boşluk karakterlerinden oluşturulamaz!")
        Boolean isRead


) {

}
