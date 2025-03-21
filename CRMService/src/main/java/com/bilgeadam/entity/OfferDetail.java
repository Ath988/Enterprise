package com.bilgeadam.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OfferDetail {
	String title;
	
	String description;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
	@Schema(type = "string", pattern = "dd/MM/yyyy", example = "19/03/2025") // 📌 Swagger formatı için ekleme
	LocalDate expirationDate;
}