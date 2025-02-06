package com.inventoryservice.repositories;


import com.inventoryservice.entities.Supplier;
import com.inventoryservice.entities.enums.EStatus;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SupplierRepository extends JpaRepository<Supplier, Long>
{

    Optional<Supplier> findByEmailAndAuthId(String email,Long authId);

    Optional<Supplier> findByIdAndAuthId(Long id, Long authId);

    List<Supplier> findAllByNameContainingIgnoreCaseAndAuthIdAndStatusIsNotOrderByNameAsc(String s, Long authId, EStatus eStatus, PageRequest of);
}
