package com.bilgeadam.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record AddCustomerRequestDto(
		@NotNull
		@Size(min = 3, max = 50)
		@Pattern(regexp = "^(?!\\s+$)[a-zA-ZçÇğĞıİöÖşŞüÜ\\s]+$", message = "Ad sadece harf ve boşluk içerebilir, boş olamaz ve en az 3 harf içermelidir.")
		String firstName,
		
		@NotNull
		@Size(min = 3, max = 50)
		@Pattern(regexp = "^(?!\\s+$)[a-zA-ZçÇğĞıİöÖşŞüÜ\\s]+$", message = "Soyad sadece harf ve boşluk içerebilir, boş olamaz ve en az 3 harf içermelidir.")
		String lastName,
		
		@NotNull
		@Email
		String email,
		
		@Pattern(regexp = "^\\d{10}$", message = "(5556667788) formatında olmalıdır.")
		String phoneNumber,
		
		String address,
		
		@NotNull
		Long companyId
) {
}