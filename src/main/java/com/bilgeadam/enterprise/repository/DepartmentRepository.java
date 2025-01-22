package com.bilgeadam.enterprise.repository;

import com.bilgeadam.enterprise.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface DepartmentRepository extends JpaRepository<Department,Long> {
    @Query("SELECT d.name FROM Department d WHERE d.id = ?1")
    Optional<String> findDepartmentNameByDepartmentId(Long departmentId);
}
