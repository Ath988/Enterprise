package com.bilgeadam.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record AddTicketMessageRequestDto(
		@Email(message = "Geçerli bir e-posta adresi giriniz!")
		String senderEmail,
		
		@Email(message = "Geçerli bir e-posta adresi giriniz!")
		String recipientEmail,
		
		@NotBlank(message = "Mesaj konusu boş olamaz!")
		String subject,
		
		@NotBlank(message = "Mesaj içeriği boş olamaz!")
		String messageContent
) {
}