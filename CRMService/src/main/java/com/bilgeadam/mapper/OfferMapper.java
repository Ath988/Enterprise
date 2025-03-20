package com.bilgeadam.mapper;

import com.bilgeadam.dto.request.AddOfferRequestDto;
import com.bilgeadam.dto.request.UpdateOfferRequestDto;
import com.bilgeadam.entity.Offer;
import com.bilgeadam.entity.OfferDetail;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface OfferMapper {
	OfferMapper INSTANCE = Mappers.getMapper(OfferMapper.class);
	
	/** 📌 Yeni teklif oluştururken DTO'dan Entity'ye çevirme */
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "isAccepted", constant = "false")
	@Mapping(target = "customerId", ignore = true)
	@Mapping(target = "offerDetail", source = ".")
	@Mapping(target = "customerName", ignore = true)
	@Mapping(target = "customerEmail", source = "customerEmail")
	Offer toEntity(AddOfferRequestDto dto);
	
	/** 📌 `AddOfferRequestDto` içinde OfferDetail mapleme */
	default OfferDetail mapToOfferDetail(AddOfferRequestDto dto) {
		return new OfferDetail(dto.title(), dto.description(), dto.expirationDate());
	}
	
	/** 📌 Update işlemi için mevcut `Offer` nesnesini DTO ile güncelleme */
	@Mapping(target = "offerDetail.title", source = "title")
	@Mapping(target = "offerDetail.description", source = "description")
	@Mapping(target = "offerDetail.expirationDate", source = "expirationDate")
	@Mapping(target = "offerStatus", source = "offerStatus")
	@Mapping(target = "status", source = "status")
	@Mapping(target = "customerEmail", ignore = true)
	void updateOfferFromDto(UpdateOfferRequestDto dto, @MappingTarget Offer offer);
}