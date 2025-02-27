package com.bilgeadam.repository;

import com.bilgeadam.entity.Asset;
import com.bilgeadam.entity.enums.EState;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AssetRepository extends JpaRepository<Asset, Long> {

    List<Asset> findAllByState(EState state);
}
