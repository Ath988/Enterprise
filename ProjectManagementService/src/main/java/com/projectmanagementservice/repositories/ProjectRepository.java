package com.projectmanagementservice.repositories;

import com.projectmanagementservice.entities.Project;
import com.projectmanagementservice.entities.enums.EStatus;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project,Long>
{
    List<Project> findAllByNameContainingIgnoreCaseAndStatusIsNotAndAuthIdOrderByNameAsc(String s, EStatus eStatus, long i, PageRequest of);

    Optional<Project> findByIdAndAuthId(Long id, Long authId);

    Optional<Project> findOptionalById(Long authId);
}
