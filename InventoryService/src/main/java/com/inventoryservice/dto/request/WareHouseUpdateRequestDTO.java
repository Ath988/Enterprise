package com.inventoryservice.dto.request;

public record WareHouseUpdateRequestDTO(
    Long id,
    String name,
    String location
)
{
}
