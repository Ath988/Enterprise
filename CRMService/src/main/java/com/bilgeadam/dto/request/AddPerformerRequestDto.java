package com.bilgeadam.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record AddPerformerRequestDto(
		@NotBlank(message = "Ad boş bırakılamaz.")
		@Size(min = 3, max = 50, message = "Ad en az 3, en fazla 50 karakter olabilir.")
		@Pattern(regexp = "^(?!\\s+$)[a-zA-ZçÇğĞıİöÖşŞüÜ\\s]+$", message = "Ad sadece harf ve boşluk içerebilir.")
		String firstName,
		
		@NotBlank(message = "Soyad boş bırakılamaz.")
		@Size(min = 3, max = 50, message = "Soyad en az 3, en fazla 50 karakter olabilir.")
		@Pattern(regexp = "^(?!\\s+$)[a-zA-ZçÇğĞıİöÖşŞüÜ\\s]+$", message = "Soyad sadece harf ve boşluk içerebilir.")
		String lastName,
		
		@NotBlank(message = "E-posta adresi boş bırakılamaz.")
		@Email(message = "Geçerli bir e-posta adresi giriniz.")
		String email,
		
		@NotBlank(message = "Telefon numarası boş bırakılamaz.")
		@Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Geçerli bir telefon numarası giriniz.")
		String phoneNumber
) {
}