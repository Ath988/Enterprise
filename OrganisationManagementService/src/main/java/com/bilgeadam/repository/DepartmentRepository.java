package com.bilgeadam.repository;

import com.bilgeadam.dto.response.AllDepartmentResponse;
import com.bilgeadam.dto.response.DepartmentDetailResponse;
import com.bilgeadam.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface DepartmentRepository extends JpaRepository<Department,Long> {
    @Query("SELECT d.name FROM Department d WHERE d.id = ?1")
    Optional<String> findDepartmentNameByDepartmentId(Long departmentId);

    @Query("SELECT NEW com.bilgeadam.dto.response.DepartmentDetailResponse(d.id,d.name,d.description,CONCAT(e.firstName , '' , e.lastName)) FROM Department d JOIN Employee e ON e.id=d.managerId WHERE d.id = ?1")
    DepartmentDetailResponse findDepartmentDetailByDepartmentId(Long departmentId);

    @Query("SELECT NEW com.bilgeadam.dto.response.AllDepartmentResponse(d.id,d.name,CONCAT(e.firstName,' ',e.lastName)) FROM Department D JOIN Employee e ON d.managerId=e.id")
    List<AllDepartmentResponse> findAllDepartments();

    @Query("SELECT NEW com.bilgeadam.dto.response.AllDepartmentResponse(d.id,d.name,CONCAT(e.firstName,' ',e.lastName)) FROM Department D JOIN Employee e ON d.managerId=e.id WHERE d.parentDepartmentId = ?1")
    List<AllDepartmentResponse> findAllSubDepartments(Long departmentId);

    //Todo: Dönen nesne için yeni View yazılabilir.
    @Query("SELECT NEW com.bilgeadam.dto.response.AllDepartmentResponse(d.id,d.name,CONCAT(e.firstName,' ',e.lastName)) FROM Department D JOIN Employee e ON d.managerId=e.id WHERE d.managerId = ?1")
    List<AllDepartmentResponse> findAllDepartmentsOfManagerByManagerId(Long managerId);
}
