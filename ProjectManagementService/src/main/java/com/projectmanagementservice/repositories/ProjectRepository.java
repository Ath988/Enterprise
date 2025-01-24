package com.projectmanagementservice.repositories;

import com.projectmanagementservice.entities.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project,Long>
{
}
