package com.inventoryservice.dto.request;

public record SupplierUpdateRequestDTO(
        Long id,
        String name,
        String surname,
        String email,
        String contactInfo,
        String address,
        String notes)
{
}
