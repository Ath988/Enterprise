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
	@Mapping(target = "ticketStatus", expression = "java(com.bilgeadam.entity.enums.TicketStatus.NEW)")
	@Mapping(target = "activities", ignore = true) // Activities liste olarak eklenmeyecek
	@Mapping(target = "ticketNumber", ignore = true) // UUID, entity içinde otomatik oluşturulacak.
	Ticket toTicket(AddTicketRequestDto dto);
	
	/** 📌 `AddTicketRequestDto` → `TicketActivity` dönüşümü */
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "type", expression = "java(com.bilgeadam.entity.enums.ActivityType.CREATION)") // İlk aktivite oluşturuldu
	@Mapping(target = "ticketId", ignore = true) // Ticket ID sonradan set edilecek
	@Mapping(target = "performedBy", ignore = true) // ActivityPerformer DTO üzerinden oluşturulacak
	TicketActivity toTicketActivity(AddTicketRequestDto dto);
	
	/** 📌 `UpdateTicketRequestDto` → `Ticket` güncellemesi */
	@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
	@Mapping(target = "ticketStatus", source = "ticketStatus")
	void updateTicketFromDto(UpdateTicketRequestDto dto, @MappingTarget Ticket ticket);
	
	/** 📌 `UpdateTicketRequestDto` → `TicketActivity` dönüşümü (Güncelleme işlemi için) */
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "ticketId", source = "ticket.id")
	@Mapping(target = "type", source = "dto.type") // Güncelleme aktivite türü
	@Mapping(target = "performedBy", ignore = true) // `ActivityPerformerMapper` ile set edilecek
	@Mapping(target = "content", source = "dto.content")
	TicketActivity toTicketActivity(UpdateTicketRequestDto dto, Ticket ticket);
}