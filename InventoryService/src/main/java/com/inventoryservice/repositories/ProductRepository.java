package com.inventoryservice.repositories;



import com.inventoryservice.entities.Product;
import com.inventoryservice.entities.enums.EStatus;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long>
{

    Optional<Product> findByIdAndAuthId(Long id, Long authId);

    List<Product> findAllByNameContainingIgnoreCaseAndStatusIsNotAndAuthIdOrderByName(String text, EStatus eStatus, Long authId, PageRequest of);

    @Query("SELECT p FROM Product p WHERE p.stockCount < p.minimumStockLevel AND p.status = :status")
    List<Product> findAllByMinimumStockLevelAndStatus(EStatus status);
}
