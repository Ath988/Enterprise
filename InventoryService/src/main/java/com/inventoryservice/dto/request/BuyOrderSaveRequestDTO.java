package com.inventoryservice.dto.request;

public record BuyOrderSaveRequestDTO(Long supplierId,
                                     Long productId,
                                     Integer quantity

                                  )

{
}
