package com.bilgeadam.dto.request;

import com.bilgeadam.entity.enums.TicketCategory;
import com.bilgeadam.entity.enums.TicketPriority;
import com.bilgeadam.entity.enums.TicketStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record AddTicketRequestDto(
		@NotBlank(message = "Başlık boş olamaz!")
		String subject,
		
		@NotNull(message = "Öncelik seçimi zorunludur!")
		TicketPriority priority,
		
		@NotNull(message = "Kategori seçimi zorunludur!")
		TicketCategory category,
		
		@NotNull(message = "Müşteri ID boş olamaz!")
		String customerEmail,
		
		@NotBlank(message = "İçerik boş olamaz!")
		String content,
		
		@NotNull(message = "Performer ID boş olamaz!")
		Long performerId,
		
		List<String> attachmentUrls // Opsiyonel dosya yolları
) {}