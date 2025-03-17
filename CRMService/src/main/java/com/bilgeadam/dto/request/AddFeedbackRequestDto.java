package com.bilgeadam.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AddFeedbackRequestDto(
		@NotBlank(message = "Geri bildirim konusu boş olamaz!")
		String subject,
		
		@Email(message = "Geçerli bir e-posta adresi giriniz!")
		String email,
		
		@NotBlank(message = "Geri bildirim içeriği boş olamaz!")
		String message
) {
}