package com.inventoryservice.dto.response;

import com.inventoryservice.entities.enums.EStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record BuyOrderResponseDTO(Long id, Long authId,String supplierName, String productName, Integer quantity, BigDecimal unitPrice, BigDecimal total, LocalDateTime createdAt, LocalDateTime updatedAt, EStatus status)
{
}
