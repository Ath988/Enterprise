package com.inventoryservice.dto.request;

import com.inventoryservice.entities.enums.EStockMovementType;

public record StockMovementUpdateRequestDTO(Long id,
                                            String description,
                                            Integer quantity,
                                            EStockMovementType type

                                          )

{
}
