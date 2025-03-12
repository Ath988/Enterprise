package com.inventoryservice.dto.request;

import java.math.BigDecimal;

public record ProductSaveRequestDTO(Long supplierId,
                                    Long wareHouseId,
                                    String name,
                                    String description,
                                    BigDecimal price,
                                    Integer stockCount,
                                    Integer minimumStockLevel
)
{
}
