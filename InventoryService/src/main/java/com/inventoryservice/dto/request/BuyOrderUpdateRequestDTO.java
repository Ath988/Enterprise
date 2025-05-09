package com.inventoryservice.dto.request;

public record BuyOrderUpdateRequestDTO(
        Long id,
        Long supplierId,
        Long productId,
        Integer quantity
)
{
}
