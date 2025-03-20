package com.bilgeadam.dto.request;

import com.bilgeadam.entity.enums.OfferStatus;
import com.bilgeadam.entity.enums.Status;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record UpdateOfferRequestDto(
		@NotBlank
		@Size(min = 3, max = 100, message = "Başlık en az 3, en fazla 100 karakter olmalıdır.")
		String title,
		
		@NotBlank
		@Size(min = 3, max = 500, message = "Açıklama en az 3, en fazla 500 karakter olmalıdır.")
		String description,
		
		@NotNull(message = "Son geçerlilik tarihi belirtilmelidir.")
		@FutureOrPresent(message = "Son geçerlilik tarihi bugünden önce olamaz.")
		@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
		LocalDate expirationDate,
		
		@NotNull(message = "Teklif durumu belirtilmelidir.")
		OfferStatus offerStatus,
		
		@NotNull(message = "Durum belirtilmelidir.")
		Status status
) {
}