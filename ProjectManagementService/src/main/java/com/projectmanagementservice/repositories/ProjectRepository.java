package com.projectmanagementservice.repositories;

import com.projectmanagementservice.entities.Project;
import com.projectmanagementservice.entities.enums.EStatus;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project,Long>
{
    List<Project> findAllByNameContainingIgnoreCaseAndStatusIsNotAndOrganizationIdOrderByNameAsc
            (String s, EStatus eStatus, Long organizationId, PageRequest of);



    List<Project> findAllByOrganizationIdAndStatus(Long organizationId, EStatus eStatus);
}
