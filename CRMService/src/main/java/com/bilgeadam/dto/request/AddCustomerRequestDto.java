package com.bilgeadam.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record AddCustomerRequestDto(
		@NotNull
		@Size(min = 3, max = 50)
		String firstName,
		
		@NotNull
		@Size(min = 3, max = 50)
		String lastName,
		
		@NotNull
		@Email
		String email,
		
		@Pattern(regexp = "^(\\+90|0)[3-5][0-9]{9}$", message = "Geçerli bir Türkiye telefon numarası giriniz. (+90XXXXXXXXXX veya 05XXXXXXXXX formatında olmalıdır.)")
		String phoneNumber,
		
		String address,
		
		@NotNull
		Long companyId
) {
}