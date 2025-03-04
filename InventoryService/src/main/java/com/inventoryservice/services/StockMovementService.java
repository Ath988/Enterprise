package com.inventoryservice.services;


import com.inventoryservice.dto.request.*;
import com.inventoryservice.dto.response.BuyOrderResponseDTO;
import com.inventoryservice.dto.response.StockMovementResponseDTO;
import com.inventoryservice.entities.BuyOrder;
import com.inventoryservice.entities.Product;
import com.inventoryservice.entities.StockMovement;
import com.inventoryservice.entities.Supplier;
import com.inventoryservice.entities.enums.EStatus;
import com.inventoryservice.exceptions.ErrorType;
import com.inventoryservice.exceptions.InventoryServiceException;
import com.inventoryservice.manager.NotificationManager;
import com.inventoryservice.repositories.BuyOrderRepository;
import com.inventoryservice.repositories.StockMovementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class StockMovementService
{
    private final StockMovementRepository stockMovementRepository;
    private final ProductService productService;
    private final NotificationManager notificationManager;

    //1L OLAN BÜTÜN DEĞERLER DAHA SONRA İSTEK ATILAN HESABIN AUTH ID'SİNDEN ALINACAKTIR.
    public Boolean save(StockMovementSaveRequestDTO dto)
    {
        Product product = productService.findByIdAndAuthId(dto.productId());
        if (product.getStockCount() < dto.quantity())
        {
            throw new InventoryServiceException(ErrorType.STOCK_NOT_ENOUGH);
        }
        product.setStockCount(product.getStockCount() - dto.quantity());

        //Ürün stok miktarı düşükse bildirim gönderiliyor.
        if (!product.getIsNotified())
        {
            notificationManager.notificationSender(new NotificationMessageRequestDto("Düşük Stok",product.getName() + " adlı ürünün kalan stok miktarı: " + product.getStockCount(),true));
            product.setIsNotified(true);
        }

        stockMovementRepository.save(StockMovement
                .builder()
                .authId(1L)
                .product(product)
                .quantity(dto.quantity())
                .description(dto.description())
                .type(dto.type())
                .build());

        productService.save(product);
        return true;
    }


    public Boolean delete(Long id)
    {
        StockMovement stockMovement = stockMovementRepository.findByIdAndAuthId(id, 1L).orElseThrow(() -> new InventoryServiceException(ErrorType.STOCK_MOVEMENT_NOT_FOUND));
        stockMovement.setStatus(EStatus.DELETED);

        //Stok ürüne geri ekleniyor
        stockMovement.getProduct().setStockCount( stockMovement.getProduct().getStockCount()+stockMovement.getQuantity());

        //Eğer ürün min stock seviyesinin üzerine çıktıysa bildirim durumu tekrar default'a çekiliyor.
        if (stockMovement.getProduct().getStockCount()> stockMovement.getProduct().getMinimumStockLevel())
        {
            stockMovement.getProduct().setIsNotified(false);
        }
        stockMovementRepository.save(stockMovement);
        return true;
    }

    public Boolean update(StockMovementUpdateRequestDTO dto)
    {
        StockMovement stockMovement = stockMovementRepository.findByIdAndAuthId(dto.id(), 1L).orElseThrow(() -> new InventoryServiceException(ErrorType.STOCK_MOVEMENT_NOT_FOUND));

        int difference = stockMovement.getQuantity() - dto.quantity();

        //Ürün miktarındaki değişim pozitifse stok artacak, negatifse stok azalacak
        stockMovement.getProduct().setStockCount( stockMovement.getProduct().getStockCount() + difference);


        //Stok 0'dan düşükse hata fırlatılacak
        if (stockMovement.getProduct().getStockCount()<0)
        {
            throw new InventoryServiceException(ErrorType.STOCK_NOT_ENOUGH);
        }

        //Ürün stok miktarı düşükse bildirim gönderiliyor.
        if (!stockMovement.getProduct().getIsNotified())
        {
            notificationManager.notificationSender(new NotificationMessageRequestDto("Düşük Stok",stockMovement.getProduct().getName() + " adlı ürünün kalan stok miktarı: " + stockMovement.getProduct().getStockCount(),true));
            stockMovement.getProduct().setIsNotified(true);
        }

        stockMovement.setDescription(dto.description());
        stockMovement.setQuantity(dto.quantity());
        stockMovement.setType(dto.type());

        stockMovementRepository.save(stockMovement);
        return true;

    }

    public List<StockMovementResponseDTO> findAllByProduct_NameContainingIgnoreCaseAndStatusIsNotAndAuthIdOrderByProduct_NameAsc(PageRequestDTO dto)
    {
        List<StockMovement> stockMovements = stockMovementRepository.findAllByProduct_NameContainingIgnoreCaseAndStatusIsNotAndAuthIdOrderByProduct_NameAsc(dto.searchText(), EStatus.DELETED, 1L, PageRequest.of(dto.page(), dto.size()));

        List<StockMovementResponseDTO> newlist = new ArrayList<>();

        for (StockMovement stockMovement : stockMovements)
        {
            newlist.add(new StockMovementResponseDTO(stockMovement.getId(), stockMovement.getAuthId(),stockMovement.getProduct().getName(),stockMovement.getQuantity(),stockMovement.getProduct().getPrice(),stockMovement.getTotal(),stockMovement.getCreatedAt(),stockMovement.getUpdatedAt(),stockMovement.getStatus()));
        }

        return newlist;
    }

    public StockMovement findByIdAndAuthId(Long id)
    {
        return stockMovementRepository.findByIdAndAuthId(id, 1L).orElseThrow(() -> new InventoryServiceException(ErrorType.STOCK_MOVEMENT_NOT_FOUND));
    }
}
