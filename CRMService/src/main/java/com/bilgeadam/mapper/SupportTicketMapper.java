package com.bilgeadam.mapper;

import com.bilgeadam.dto.request.AddSupportTicketRequestDto;
import com.bilgeadam.dto.request.UpdateSupportTicketRequestDto;
import com.bilgeadam.entity.SupportTicket;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface SupportTicketMapper {
	SupportTicketMapper INSTANCE = Mappers.getMapper(SupportTicketMapper.class);
	
	SupportTicket fromAddSupportTicketDto(AddSupportTicketRequestDto dto);
	
	@Mapping(target = "id", ignore = true)
	void updateSupportTicketFromDto(UpdateSupportTicketRequestDto dto, @MappingTarget SupportTicket supportTicket);
}