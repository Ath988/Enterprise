package com.bilgeadam.mapper;

import com.bilgeadam.dto.request.AddCustomerRequestDto;
import com.bilgeadam.dto.request.UpdateCustomerRequestDto;
import com.bilgeadam.entity.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface CustomerMapper {
	CustomerMapper INSTANCE = Mappers.getMapper(CustomerMapper.class);
	@Mapping(target = "profile", expression = "java(new com.bilgeadam.entity.Profile(dto.firstName(), dto.lastName()," +
			" dto.email(), dto.phoneNumber(), dto.address()))")
	@Mapping(target = "companyId", source = "dto.companyId")
	Customer fromAddCustomer(final AddCustomerRequestDto dto);
	
	@Mapping(target = "profile.firstName", source = "dto.firstName")
	@Mapping(target = "profile.lastName", source = "dto.lastName")
	@Mapping(target = "profile.email", source = "dto.email")
	@Mapping(target = "profile.phoneNumber", source = "dto.phoneNumber")
	@Mapping(target = "profile.address", source = "dto.address")
	void updateCustomerFromDto(UpdateCustomerRequestDto dto, @MappingTarget Customer customer);
}