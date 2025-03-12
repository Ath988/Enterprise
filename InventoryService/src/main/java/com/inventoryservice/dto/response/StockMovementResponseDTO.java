package com.inventoryservice.dto.response;

import com.inventoryservice.entities.enums.EStatus;
import com.inventoryservice.entities.enums.EStockMovementType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record StockMovementResponseDTO(Long id, Long authId, String productName, Integer quantity, BigDecimal unitPrice, BigDecimal total,
                                       EStockMovementType type,String description, LocalDateTime createdAt, LocalDateTime updatedAt, EStatus status)
{
}
