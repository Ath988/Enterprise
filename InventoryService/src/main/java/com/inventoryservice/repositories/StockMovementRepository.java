package com.inventoryservice.repositories;


import com.inventoryservice.entities.BuyOrder;
import com.inventoryservice.entities.StockMovement;
import com.inventoryservice.entities.enums.EStatus;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StockMovementRepository extends JpaRepository<StockMovement, Long>
{

    Optional<StockMovement> findByIdAndAuthId(Long id, Long authId);
    List<StockMovement> findAllByProduct_NameContainingIgnoreCaseAndStatusIsNotAndAuthIdOrderByProduct_NameAsc(String name, EStatus status, Long memberId, PageRequest pageRequest);
}
