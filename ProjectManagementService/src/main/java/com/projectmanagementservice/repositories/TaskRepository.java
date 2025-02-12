package com.projectmanagementservice.repositories;

import com.projectmanagementservice.entities.Task;
import com.projectmanagementservice.entities.enums.EStatus;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task,Long>
{

    Optional<Task> findByIdAndAuthId(Long id, Long authId);


    List<Task> findAllByProjectIdAndStatus(Long projectId, EStatus eStatus);
}
