package com.inventoryservice.dto.request;

import com.inventoryservice.entities.enums.EStockMovementType;

public record StockMovementSaveRequestDTO(String description,
                                          Long productId,
                                          Integer quantity,
                                          EStockMovementType type

                                          )

{
}
