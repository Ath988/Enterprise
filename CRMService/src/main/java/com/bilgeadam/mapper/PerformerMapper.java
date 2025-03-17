package com.bilgeadam.mapper;

import com.bilgeadam.dto.request.AddPerformerRequestDto;
import com.bilgeadam.dto.request.UpdatePerformerRequestDto;
import com.bilgeadam.entity.Performer;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface PerformerMapper {
	PerformerMapper INSTANCE = Mappers.getMapper(PerformerMapper.class);
	
	Performer fromAddPerformerRequestDto(AddPerformerRequestDto dto);
	
	/** 📌 `UpdatePerformerRequestDto` → `Performer` güncellemesi */
	@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
	void updatePerformerFromDto(UpdatePerformerRequestDto dto, @MappingTarget Performer performer);
}