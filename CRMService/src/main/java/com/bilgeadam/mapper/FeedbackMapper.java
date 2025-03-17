package com.bilgeadam.mapper;

import com.bilgeadam.dto.request.AddFeedbackRequestDto;
import com.bilgeadam.entity.Feedback;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface FeedbackMapper {
	
	FeedbackMapper INSTANCE = Mappers.getMapper(FeedbackMapper.class);
	
	/** 📌 `AddFeedbackRequestDto` → `Feedback` dönüşümü */
	@Mapping(target = "id", ignore = true) // ID otomatik atanacak
	@Mapping(target = "ticketId", ignore = true) // Ticket ID Service içinde set edilecek
	Feedback fromAddFeedbackRequestDto(AddFeedbackRequestDto dto);
}