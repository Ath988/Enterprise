package com.bilgeadam.dto.request;

import com.bilgeadam.entity.enums.Status;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UpdatePerformerRequestDto(
		@Size(min = 3, max = 50, message = "Ad en az 3, en fazla 50 karakter olabilir.")
		@Pattern(regexp = "^(?!\\s+$)[a-zA-ZçÇğĞıİöÖşŞüÜ\\s]+$", message = "Ad sadece harf ve boşluk içerebilir.")
		String firstName,
		
		@Size(min = 3, max = 50, message = "Soyad en az 3, en fazla 50 karakter olabilir.")
		@Pattern(regexp = "^(?!\\s+$)[a-zA-ZçÇğĞıİöÖşŞüÜ\\s]+$", message = "Soyad sadece harf ve boşluk içerebilir.")
		String lastName,
		
		@NotBlank(message = "E-posta adresi boş olamaz.")
		@Email(message = "Geçerli bir e-posta adresi giriniz.")
		String email,
		
		@Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Geçerli bir telefon numarası giriniz.")
		String phoneNumber,
		
		Status status
) {
}