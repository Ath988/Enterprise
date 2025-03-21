package com.bilgeadam.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record AddOfferRequestDto(
		@NotNull
		@Size(min = 3, max = 100)
		String title,
		
		@NotNull
		@Size(min = 3, max = 500)
		String description,
		
		@NotNull
		@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
		@Schema(type = "string", pattern = "dd/MM/yyyy", example = "19/03/2025") // 📌 Swagger için formatı belirtme
		LocalDate expirationDate,
		
		String customerEmail
) {
}