package com.inventoryservice.dto.response;

import com.inventoryservice.entities.enums.EStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record ProductResponseDTO(Long id, Long authId, String supplierName, String wareHouseName, String name, String description, BigDecimal price, Integer stockCount, Integer minimumStockLevel, LocalDateTime createdAt, LocalDateTime updatedAt, EStatus status)
{
}
