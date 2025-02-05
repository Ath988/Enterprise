package com.inventoryservice.dto.request;

public record SupplierUpdateRequestDTO(
        Long id,
        String name,
        String surname,
        String contactInfo,
        String address,
        String notes)
{
}
