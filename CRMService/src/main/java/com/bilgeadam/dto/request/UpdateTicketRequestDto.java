package com.bilgeadam.dto.request;

import com.bilgeadam.entity.enums.ActivityType;
import com.bilgeadam.entity.enums.TicketPriority;
import com.bilgeadam.entity.enums.TicketStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateTicketRequestDto(
		@NotBlank(message = "Başlık boş olamaz!") String subject,
		@NotNull(message = "Durum seçimi zorunludur!") TicketStatus status,
		@NotNull(message = "Öncelik seçimi zorunludur!") TicketPriority priority,
		@NotNull(message = "Activity Type boş olamaz") ActivityType type,
		@NotBlank(message = "İçerik boş olamaz!") String content,
		@NotNull(message = "Personel ID boş olamaz!") Long performerId,
		@NotBlank(message = "Personel adı boş olamaz!") String performerName,
		Boolean isStaff
) {}