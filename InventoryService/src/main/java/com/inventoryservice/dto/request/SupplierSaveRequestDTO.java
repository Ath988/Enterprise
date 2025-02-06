package com.inventoryservice.dto.request;

public record SupplierSaveRequestDTO(
    String name,
    String surname,
    String email,
    String contactInfo,
    String address,
    String notes)
{
}
