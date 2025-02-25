package com.inventoryservice.services;


import com.inventoryservice.InventoryServiceApplication;
import com.inventoryservice.dto.request.PageRequestDTO;
import com.inventoryservice.dto.request.ProductSaveRequestDTO;
import com.inventoryservice.dto.request.ProductUpdateRequestDTO;
import com.inventoryservice.dto.response.ProductResponseDTO;
import com.inventoryservice.entities.Product;
import com.inventoryservice.entities.Supplier;
import com.inventoryservice.entities.WareHouse;
import com.inventoryservice.entities.enums.EStatus;
import com.inventoryservice.exceptions.ErrorType;
import com.inventoryservice.exceptions.InventoryServiceException;
import com.inventoryservice.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService
{
    private final ProductRepository productRepository;
    private final WareHouseService wareHouseService;
    private  SupplierService supplierService;

    @Autowired
    private void setService(@Lazy SupplierService supplierService)
    {
        this.supplierService = supplierService;
    }

    //1L OLAN BÜTÜN DEĞERLER DAHA SONRA İSTEK ATILAN HESABIN AUTH ID'SİNDEN ALINACAKTIR.
    public Product findByIdAndAuthId(Long id)
    {
        return productRepository.findByIdAndAuthId(id, 1L).orElseThrow(() -> new InventoryServiceException(ErrorType.PRODUCT_NOT_FOUND));
    }


    public Product findById(Long id)
    {
        return productRepository.findById(id).orElseThrow(() -> new InventoryServiceException(ErrorType.PRODUCT_NOT_FOUND));
    }

    public Boolean save(ProductSaveRequestDTO dto)
    {
        if (dto.stockCount() < 0 || dto.minimumStockLevel() < 0 || dto.price().compareTo(BigDecimal.ZERO) < 0)
        {
            throw new InventoryServiceException(ErrorType.VALUE_CAN_NOT_BE_BELOW_ZERO);
        }

        Supplier supplier = supplierService.findById(dto.supplierId());
        WareHouse wareHouse = wareHouseService.findByIdAndAuthId(dto.wareHouseId());

        productRepository.save(Product
                .builder()

                .name(dto.name())
                .supplier(supplier)
                .wareHouse(wareHouse)
                .authId(1L)
                .description(dto.description())
                .price(dto.price())
                .stockCount(dto.stockCount())
                .minimumStockLevel(dto.minimumStockLevel())
                .build());
        return true;
    }


    public Boolean save(Product product)
    {
        productRepository.save(product);
        return true;
    }

    public Boolean delete(Long id)
    {
        Product product = productRepository.findByIdAndAuthId(id, 1L).orElseThrow(() -> new InventoryServiceException(ErrorType.PRODUCT_NOT_FOUND));
        product.setStatus(EStatus.DELETED);
        productRepository.save(product);
        return true;
    }

    public Boolean update(ProductUpdateRequestDTO dto)
    {
        if (dto.stockCount() < 0 || dto.minimumStockLevel() < 0 || dto.price().compareTo(BigDecimal.ZERO) < 0)
        {
            throw new InventoryServiceException(ErrorType.VALUE_CAN_NOT_BE_BELOW_ZERO);
        }
        Product product = productRepository.findByIdAndAuthId(dto.id(), 1L).orElseThrow(() -> new InventoryServiceException(ErrorType.PRODUCT_NOT_FOUND));
        WareHouse wareHouse = wareHouseService.findByIdAndAuthId(dto.wareHouseId());
        Supplier supplier = supplierService.findByIdAndAuthId(dto.supplierId());
        if (dto.name() != null)
        {
            product.setName(dto.name());
        }
        if (dto.description() != null)
        {
            product.setDescription(dto.description());
        }
        if (dto.price() != null)
        {
            product.setPrice(dto.price());
        }
        if (dto.stockCount() != null)
        {
            product.setStockCount(dto.stockCount());
        }
        if (dto.minimumStockLevel() != null)
        {
            product.setMinimumStockLevel(dto.minimumStockLevel());
        }
        product.setSupplier(supplier);
        product.setWareHouse(wareHouse);
        productRepository.save(product);
        return true;
    }

    public List<ProductResponseDTO> findAllByNameContainingIgnoreCaseAndStatusIsNotAndAuthIdOrderByName(PageRequestDTO dto)
    {
        List<Product> productList = productRepository.findAllByNameContainingIgnoreCaseAndStatusIsNotAndAuthIdOrderByName(dto.searchText(), EStatus.DELETED, 1L, PageRequest.of(dto.page(), dto.size()));
        List<ProductResponseDTO> newList = new ArrayList<>();

        for (Product product : productList)
        {
            newList.add(new ProductResponseDTO( product.getId(), product.getAuthId(), product.getSupplier().getName(),product.getWareHouse().getName(),product.getName(),product.getDescription(),product.getPrice(),product.getStockCount(),product.getMinimumStockLevel(),product.getCreatedAt(),product.getUpdatedAt(),product.getStatus()));
        }

        return newList;
    }

    public List<Product> findAllByMinimumStockLevelAndStatus(EStatus status)
    {
        return productRepository.findAllByMinimumStockLevelAndStatus(status);
    }


}
