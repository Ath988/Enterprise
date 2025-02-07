package com.bilgeadam.repository;
import com.bilgeadam.entity.TaxRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaxRecordReposiory extends JpaRepository<TaxRecord, Long> {
}
