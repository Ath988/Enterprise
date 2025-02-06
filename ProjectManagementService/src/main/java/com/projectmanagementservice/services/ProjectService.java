package com.projectmanagementservice.services;

import com.projectmanagementservice.dto.request.*;
import com.projectmanagementservice.entities.Project;
import com.projectmanagementservice.entities.enums.EStatus;
import com.projectmanagementservice.exceptions.ErrorType;
import com.projectmanagementservice.exceptions.ProjectManagementException;
import com.projectmanagementservice.repositories.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectService
{
    private final ProjectRepository projectRepository;
    private final TaskService taskService;

    //TODO Gelen istekten hangi kullanıcının olduğu bilgisi gelince metodlardaki sabit "1L" değerleri değiştirilecek.
    public Boolean save(ProjectSaveRequestDTO dto)
    {
        projectRepository.save(Project
                .builder()
                .name(dto.name())
                .description(dto.description())
                        .organizationId(1L)
                .startDate(dto.startDate())
                .endDate(dto.endDate())
                .build());
        return true;
    }

    public Boolean delete(ProjectDeleteRequest dto)
    {
        Project project = projectRepository.findById(dto.projectId()).orElseThrow(() -> new ProjectManagementException(ErrorType.PROJECT_NOT_FOUND));
        project.setStatus(EStatus.DELETED);
        projectRepository.save(project);
        return true;
    }

    public Boolean updateProjectDetails(ProjectUpdateRequestDTO dto)
    {
        Project project = projectRepository.findById(dto.id()).orElseThrow(() -> new ProjectManagementException(ErrorType.PROJECT_NOT_FOUND));
        project.setName(dto.name());
        project.setDescription(dto.description());
        project.setStartDate(dto.startDate());
        project.setEndDate(dto.endDate());
        projectRepository.save(project);
        return true;
    }
    //TODO: kullanici authIdsi ile hangi organizasyona bagli oldugu bilgisi getirilmelidir!
    public List<Project> findAllProjectsByOrganizationId(Long authId) {
        Long organizationId = authId; // organizasyonun bulundugu senaryo
        return projectRepository.findAllByOrganizationIdAndStatus(organizationId, EStatus.ACTIVE);
    }


}

