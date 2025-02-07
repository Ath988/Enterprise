package com.inventoryservice.dto.request;

public record PageRequestDTO(
    String searchText,
    int page,
    int size)
{
}
