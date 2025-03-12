package com.bilgeadam.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UpdateCustomerRequestDto(
		@NotNull
		@Size(min = 3, max = 50)
		String firstName,
		
		@NotNull
		@Size(min = 3, max = 50)
		String lastName,
		
		@Email
		String email,
		
		@Pattern(regexp = "^\\d{10}$", message = "(5556667788) formatında olmalıdır.")
		String phoneNumber,
		
		String address
) {
}