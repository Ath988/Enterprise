package com.inventoryservice.repositories;
import com.inventoryservice.entities.WareHouse;
import com.inventoryservice.entities.enums.EStatus;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface WareHouseRepository extends JpaRepository<WareHouse, Long>
{
    List<WareHouse> findAllByNameContainingIgnoreCaseAndAuthIdAndStatusIsNotOrderByNameAsc(String name, Long memberId, EStatus status, PageRequest pageRequest);

    Optional<WareHouse> findByIdAndAuthId(Long id, Long memberId);
}
