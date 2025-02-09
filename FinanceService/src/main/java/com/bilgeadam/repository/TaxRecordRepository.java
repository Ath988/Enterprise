package com.bilgeadam.repository;
import com.bilgeadam.entity.TaxRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaxRecordRepository extends JpaRepository<TaxRecord, Long> {
    List<TaxRecord> findByAccountId(Long accountId);

}
