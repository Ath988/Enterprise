package com.bilgeadam.mapper;

import com.bilgeadam.dto.request.AddTicketRequestDto;
import com.bilgeadam.dto.request.UpdateTicketRequestDto;
import com.bilgeadam.entity.ActivityPerformer;
import com.bilgeadam.entity.Performer;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface ActivityPerformerMapper {
	
	ActivityPerformerMapper INSTANCE = Mappers.getMapper(ActivityPerformerMapper.class);
	
	/** 📌 `Performer` → `ActivityPerformer` dönüşümü */
	@Mapping(target = "id", source = "id")
	@Mapping(target = "firstName", source = "firstName")
	@Mapping(target = "lastName", source = "lastName")
	@Mapping(target = "email", source = "email")
	@Mapping(target = "phoneNumber", source = "phoneNumber")
	ActivityPerformer toActivityPerformer(Performer performer);
}