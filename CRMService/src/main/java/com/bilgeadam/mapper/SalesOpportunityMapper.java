package com.bilgeadam.mapper;

import com.bilgeadam.dto.request.AddSalesOpportunityRequestDto;
import com.bilgeadam.dto.request.UpdateSalesOpportunityRequestDto;
import com.bilgeadam.entity.SalesOpportunity;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface SalesOpportunityMapper {
	SalesOpportunityMapper INSTANCE = Mappers.getMapper(SalesOpportunityMapper.class);
	
	@Mapping(target = "campaignId", source = "campaignId", qualifiedByName = "nullIfEmpty")
	@Mapping(target = "customerId", source = "customerId", qualifiedByName = "nullIfEmpty")
	SalesOpportunity fromAddSalesOpportunityDto(AddSalesOpportunityRequestDto dto);
	
	@Mapping(target = "id", ignore = true) // ID'yi güncellenmeyecek şekilde ayarlıyorum
	@Mapping(target = "campaignId", source = "campaignId", qualifiedByName = "nullIfEmpty")
	@Mapping(target = "customerId", source = "customerId", qualifiedByName = "nullIfEmpty")
	void updateSalesOpportunityFromDto(UpdateSalesOpportunityRequestDto dto, @MappingTarget SalesOpportunity salesOpportunity);
	
	@Named("nullIfEmpty")
	static Long nullIfEmpty(Long value) {
		return value != null && value > 0 ? value : null;
	}
}