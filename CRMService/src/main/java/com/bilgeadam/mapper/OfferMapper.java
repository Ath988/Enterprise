package com.bilgeadam.mapper;

import com.bilgeadam.dto.request.AddOfferRequestDto;
import com.bilgeadam.dto.request.UpdateOfferRequestDto;
import com.bilgeadam.entity.Offer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface OfferMapper {
	OfferMapper INSTANCE = Mappers.getMapper(OfferMapper.class);
	
	/** 📌 Request DTO'dan Entity'ye çevirme */
	@Mapping(target = "offerDetail", expression = "java(new OfferDetail(dto.title(), dto.description(), dto.expirationDate(), dto.createdAt()))")
	@Mapping(target = "status", expression = "java(com.bilgeadam.entity.enums.OfferStatus.PENDING)")
	@Mapping(target = "id", ignore = true)
	Offer toEntity(AddOfferRequestDto dto);
	
	/** 📌 Update işlemi için mevcut `Offer` nesnesini DTO ile güncelleme */
	@Mapping(target = "offerDetail.title", source = "title")
	@Mapping(target = "offerDetail.description", source = "description")
	@Mapping(target = "offerDetail.expirationDate", source = "expirationDate")
	void updateOfferFromDto(UpdateOfferRequestDto dto, @MappingTarget Offer offer);
}