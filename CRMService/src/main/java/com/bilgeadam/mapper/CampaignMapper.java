package com.bilgeadam.mapper;

import com.bilgeadam.dto.request.AddCampaignRequestDto;
import com.bilgeadam.dto.request.UpdateCampaignRequestDto;
import com.bilgeadam.entity.Campaign;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface CampaignMapper {
	CampaignMapper INSTANCE = Mappers.getMapper(CampaignMapper.class);
	@Mapping(target = "customerId", ignore = true)
	Campaign fromAddCampaignDto(AddCampaignRequestDto dto);
	@Mapping(target = "id", ignore = true) // ID'yi güncellenmeyecek şekilde ayarlıyorum
	void updateCampaignFromDto(UpdateCampaignRequestDto dto, @MappingTarget Campaign campaign);
}