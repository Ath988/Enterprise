package com.bilgeadam.repository;

import com.bilgeadam.entity.Budget;
import com.bilgeadam.entity.enums.EStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BudgetRepository extends JpaRepository<Budget, Long> {
    Page<Budget> findAllByStatusNot(EStatus status, Pageable pageable);
    List<Budget> findAllByDepartmentId(Long departmentId);
}
