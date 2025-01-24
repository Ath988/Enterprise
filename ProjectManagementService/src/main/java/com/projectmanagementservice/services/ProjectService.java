package com.projectmanagementservice.services;

import com.projectmanagementservice.dto.request.ProjectSaveRequestDTO;
import com.projectmanagementservice.entities.Project;
import com.projectmanagementservice.repositories.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProjectService
{
    private final ProjectRepository projectRepository;

    public Boolean save(ProjectSaveRequestDTO dto)
    {
        projectRepository.save(Project
                .builder()
                .name(dto.name())
                .description(dto.description())
                .startDate(dto.startDate())
                .endDate(dto.endDate())
                .build());
        return true;
    }
}
