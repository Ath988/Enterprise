package com.projectmanagementservice.repositories;

import com.projectmanagementservice.entities.Project;
import com.projectmanagementservice.entities.Task;
import com.projectmanagementservice.entities.enums.EStatus;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task,Long>
{
    List<Task> findAllByNameContainingIgnoreCaseAndStatusIsNotAndAuthIdOrderByNameAsc(String s, EStatus eStatus, long i, PageRequest of);

    Optional<Task> findByIdAndAuthId(Long id, Long authId);
}
