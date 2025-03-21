package com.bilgeadam.dto.request;

import com.bilgeadam.entity.enums.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record UpdateTicketRequestDto(
		@NotBlank(message = "Başlık boş olamaz!")
		String subject,
		
		@NotNull(message = "Bilet Durum seçimi zorunludur!")
		TicketStatus ticketStatus,
		
		@NotNull(message = "Öncelik seçimi zorunludur!")
		TicketPriority priority,
		
		@NotNull(message = "Activity Type boş olamaz")
		ActivityType type,
		
		@NotNull(message = "Kategori seçimi zorunludur!")
		TicketCategory category,
		
		@NotBlank(message = "İçerik boş olamaz!")
		String content,
		
		@NotNull(message = "Performer ID boş olamaz!")
		Long performerId,
		
		List<String> attachmentUrls, // Opsiyonel dosya yolları
		
		Status status
) {}