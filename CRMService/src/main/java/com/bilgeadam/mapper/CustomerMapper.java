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
	Customer fromAddCustomer(final AddCustomerRequestDto dto);
	@Mapping(target = "firstName", ignore = true)
	@Mapping(target = "lastName", ignore = true)
	void updateCustomerFromDto(UpdateCustomerRequestDto dto, @MappingTarget Customer customer);
}