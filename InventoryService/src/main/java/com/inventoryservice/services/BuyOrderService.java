package com.inventoryservice.services;


import com.inventoryservice.dto.request.BuyOrderSaveRequestDTO;
import com.inventoryservice.dto.request.BuyOrderUpdateRequestDTO;
import com.inventoryservice.dto.request.PageRequestDTO;
import com.inventoryservice.entities.BuyOrder;
import com.inventoryservice.entities.Product;
import com.inventoryservice.entities.Supplier;

import com.inventoryservice.entities.enums.EStatus;
import com.inventoryservice.exceptions.ErrorType;
import com.inventoryservice.exceptions.InventoryServiceException;
import com.inventoryservice.repositories.BuyOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class BuyOrderService
{
    private final BuyOrderRepository buyOrderRepository;
    private final ProductService productService;
    private SupplierService supplierService;

    @Autowired
    private void setService(@Lazy SupplierService supplierService)
    {
        this.supplierService = supplierService;
    }
    //1L OLAN BÜTÜN DEĞERLER DAHA SONRA İSTEK ATILAN HESABIN AUTH ID'SİNDEN ALINACAKTIR.
    public Boolean save(BuyOrderSaveRequestDTO dto)
    {
        if (dto.quantity() < 0)
        {
            throw new InventoryServiceException(ErrorType.VALUE_CAN_NOT_BE_BELOW_ZERO);
        }
        Product product = productService.findByIdAndAuthId(dto.productId());
        Supplier supplier = supplierService.findById(dto.supplierId());

        BuyOrder order = BuyOrder
                .builder()
                .authId(1L)
                .product(product)
                .supplier(supplier)
                .unitPrice(product.getPrice())
                .quantity(dto.quantity())
                .build();

        buyOrderRepository.save(order);
        return true;
    }



    public Boolean delete(Long id)
    {
        BuyOrder order = buyOrderRepository.findByIdAndAuthId(id, 1L).orElseThrow(() -> new InventoryServiceException(ErrorType.ORDER_NOT_FOUND));

        order.setStatus(EStatus.DELETED);
        buyOrderRepository.save(order);
        return true;
    }


    public Boolean update(BuyOrderUpdateRequestDTO dto)
    {
        if (dto.quantity() < 0)
        {
            throw new InventoryServiceException(ErrorType.VALUE_CAN_NOT_BE_BELOW_ZERO);
        }
        BuyOrder order = buyOrderRepository.findByIdAndAuthId(dto.id(), 1L).orElseThrow(() -> new InventoryServiceException(ErrorType.ORDER_NOT_FOUND));
        Product product = productService.findByIdAndAuthId(dto.productId());
        Supplier supplier = supplierService.findById(dto.supplierId());

        order.setQuantity(dto.quantity());
        order.setProduct(product);
        order.setSupplier(supplier);
        buyOrderRepository.save(order);
        return true;
    }



    public List<BuyOrder> findAll(PageRequestDTO dto)
    {
        return buyOrderRepository.findAll(PageRequest.of(dto.page(), dto.size())).getContent();
    }

    public BuyOrder findById(Long id)
    {
        return buyOrderRepository.findById(id).orElseThrow(() -> new InventoryServiceException(ErrorType.ORDER_NOT_FOUND));
    }


    public List<BuyOrder> findAllByProduct_NameContainingIgnoreCaseAndStatusIsNotAndAuthIdOrderByProduct_NameAsc(PageRequestDTO dto)
    {
       return buyOrderRepository.findAllByProduct_NameContainingIgnoreCaseAndStatusIsNotAndAuthIdOrderByProduct_NameAsc(dto.searchText(), EStatus.DELETED, 1L, PageRequest.of(dto.page(), dto.size()));

    }

    public BuyOrder findByIdAndAuthId(Long id)
    {
        return buyOrderRepository.findByIdAndAuthId(id, 1L).orElseThrow(() -> new InventoryServiceException(ErrorType.ORDER_NOT_FOUND));
    }
}
