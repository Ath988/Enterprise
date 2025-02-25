package com.bilgeadam.repository;

import com.bilgeadam.dto.response.PositionDetailResponse;
import com.bilgeadam.entity.Position;
import com.bilgeadam.view.VwPosition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PositionRepository extends JpaRepository<Position, Long> {

    @Query("SELECT p.departmentId FROM Position p WHERE p.id = ?1")
    Optional<Long> findDepartmentIdByPositionId(Long id);

    @Query("SELECT new com.bilgeadam.dto.response.PositionDetailResponse(p.id,d.name,p.title,p.description) FROM Position p JOIN Department d ON p.departmentId=d.id WHERE p.id = ?1")
    Optional<PositionDetailResponse> findPositionDetailById(Long id);

    @Query("SELECT new com.bilgeadam.dto.response.PositionDetailResponse(p.id,d.name,p.title,p.description) FROM Position p JOIN Department d ON p.departmentId=d.id WHERE p.companyId = ?1 AND p.state = com.bilgeadam.entity.enums.EState.ACTIVE")
    List<PositionDetailResponse> findAllPositionsByCompanyId(Long companyId);

    @Query("SELECT new com.bilgeadam.dto.response.PositionDetailResponse(p.id,d.name,p.title,p.description) FROM Position p JOIN Department d ON p.departmentId=d.id WHERE p.departmentId = ?1 AND p.state = com.bilgeadam.entity.enums.EState.ACTIVE")
    List<PositionDetailResponse> findAllPositionsByDepartmentId(Long departmentId);

    @Query("SELECT p.title FROM Position p WHERE p.departmentId = ?1 AND p.state = com.bilgeadam.entity.enums.EState.ACTIVE")
    List<String> findAllPositionNamesByDepartmentId(Long departmentId);
    @Query("SELECT NEW com.bilgeadam.view.VwPosition(p.id,p.title) FROM Position p WHERE p.parentPositionId = ?1")
    List<VwPosition> findAllVwPositionsByParentPositionId(Long parentPositionId);
}