package com.inventoryservice.repositories;


import com.inventoryservice.entities.BuyOrder;
import com.inventoryservice.entities.enums.EStatus;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BuyOrderRepository extends JpaRepository<BuyOrder, Long>
{


    Optional<BuyOrder> findByIdAndAuthId(Long id, Long authId);
    List<BuyOrder> findAllByProduct_NameContainingIgnoreCaseAndStatusIsNotAndAuthIdOrderByProduct_NameAsc(String name, EStatus status, Long memberId, PageRequest pageRequest);
}
