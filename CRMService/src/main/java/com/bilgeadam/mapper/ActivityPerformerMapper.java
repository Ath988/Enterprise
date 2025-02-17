package com.bilgeadam.mapper;

import com.bilgeadam.dto.request.AddTicketRequestDto;
import com.bilgeadam.dto.request.UpdateTicketRequestDto;
import com.bilgeadam.entity.ActivityPerformer;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface ActivityPerformerMapper {
	
	ActivityPerformerMapper INSTANCE = Mappers.getMapper(ActivityPerformerMapper.class);
	
	/** 📌 `AddTicketRequestDto` → `ActivityPerformer` */
	@Mapping(target = "name", source = "performerName")
	@Mapping(target = "staff", source = "isStaff")
	@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
	ActivityPerformer toActivityPerformer(AddTicketRequestDto dto);
	
	/** 📌 `UpdateTicketRequestDto` → `ActivityPerformer` */
	@Mapping(target = "name", source = "performerName")
	@Mapping(target = "staff", source = "isStaff")
	@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
	void updateActivityPerformerFromDto(UpdateTicketRequestDto dto, @MappingTarget ActivityPerformer performer);
}