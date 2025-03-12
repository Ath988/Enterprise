package com.bilgeadam.mapper;

import com.bilgeadam.dto.request.AddCustomerRequestDto;
import com.bilgeadam.dto.request.UpdateCustomerRequestDto;
import com.bilgeadam.entity.Customer;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface CustomerMapper {
	CustomerMapper INSTANCE = Mappers.getMapper(CustomerMapper.class);
	@Mapping(target = "profile", expression = "java(new com.bilgeadam.entity.Profile(dto.firstName(), dto.lastName()," +
			" dto.email(), dto.phoneNumber(), dto.address()))")
	@Mapping(target = "companyId", source = "dto.companyId")
	Customer fromAddCustomer(final AddCustomerRequestDto dto);
	
	@AfterMapping
	default void trimNames(@MappingTarget Customer customer) {
		if (customer.getProfile() != null) {
			customer.getProfile().setFirstName(customer.getProfile().getFirstName().trim());
			customer.getProfile().setLastName(customer.getProfile().getLastName().trim());
		}
	}
	
	@Mapping(target = "profile.firstName", source = "dto.firstName")
	@Mapping(target = "profile.lastName", source = "dto.lastName")
	@Mapping(target = "profile.email", source = "dto.email")
	@Mapping(target = "profile.phoneNumber", source = "dto.phoneNumber")
	@Mapping(target = "profile.address", source = "dto.address")
	void updateCustomerFromDto(UpdateCustomerRequestDto dto, @MappingTarget Customer customer);
}