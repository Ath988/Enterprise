package com.bilgeadam.mapper;

import com.bilgeadam.dto.request.AddTicketRequestDto;
import com.bilgeadam.dto.request.UpdateTicketRequestDto;
import com.bilgeadam.entity.Ticket;
import com.bilgeadam.entity.TicketActivity;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface TicketMapper {
	TicketMapper INSTANCE = Mappers.getMapper(TicketMapper.class);
	
	/** 📌 `AddTicketRequestDto` → `Ticket` dönüşümü */
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "status", expression = "java(com.bilgeadam.entity.enums.TicketStatus.NEW)")
	@Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
	@Mapping(target = "activities", ignore = true)
	Ticket toTicket(AddTicketRequestDto dto);
	
	/** 📌 `AddTicketRequestDto` → `TicketActivity` dönüşümü */
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "type", expression = "java(com.bilgeadam.entity.enums.ActivityType.CREATION)")
	@Mapping(target = "ticketId", ignore = true) // Ticket ID sonradan set edilecek
	@Mapping(target = "timestamp", expression = "java(java.time.LocalDateTime.now())")
	@Mapping(target = "performedBy.name", source = "performerName", defaultValue = "Bilinmiyor")
	@Mapping(target = "performedBy.staff", source = "isStaff", defaultValue = "false")
	TicketActivity toTicketActivity(AddTicketRequestDto dto);
	
	/** 📌 `UpdateTicketRequestDto` → Varolan `Ticket` güncellemesi */
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "createdAt", ignore = true)
	@Mapping(target = "activities", ignore = true)
	@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
	void updateTicketFromDto(UpdateTicketRequestDto dto, @MappingTarget Ticket ticket);
}