package com.projectmanagementservice.services;

import com.projectmanagementservice.dto.request.AddTaskToProjectDTO;
import com.projectmanagementservice.dto.request.PageRequestDTO;
import com.projectmanagementservice.dto.request.ProjectSaveRequestDTO;
import com.projectmanagementservice.dto.request.ProjectUpdateRequestDTO;
import com.projectmanagementservice.entities.Project;
import com.projectmanagementservice.entities.Task;
import com.projectmanagementservice.entities.enums.EStatus;
import com.projectmanagementservice.exceptions.ErrorType;
import com.projectmanagementservice.exceptions.ProjectManagementException;
import com.projectmanagementservice.repositories.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
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
                        .authId(1L)
                .startDate(dto.startDate())
                .endDate(dto.endDate())
                .build());
        return true;
    }

    public Boolean delete(Long id)
    {
        Project project = projectRepository.findByIdAndAuthId(id,1L).orElseThrow(() -> new ProjectManagementException(ErrorType.PROJECT_NOT_FOUND));
        project.setStatus(EStatus.DELETED);
        projectRepository.save(project);
        return true;
    }

    public Boolean update(ProjectUpdateRequestDTO dto)
    {
        Project project = projectRepository.findByIdAndAuthId(dto.id(),1L).orElseThrow(() -> new ProjectManagementException(ErrorType.PROJECT_NOT_FOUND));
        project.setName(dto.name());
        project.setDescription(dto.description());
        project.setStartDate(dto.startDate());
        project.setEndDate(dto.endDate());
        projectRepository.save(project);
        return true;
    }


    public List<Project> findAllByNameContainingIgnoreCaseAndStatusIsNotAndAuthIdOrderByNameAsc(PageRequestDTO dto)
    {
        return projectRepository.findAllByNameContainingIgnoreCaseAndStatusIsNotAndAuthIdOrderByNameAsc(dto.searchText(), EStatus.DELETED,1L, PageRequest.of(dto.page(), dto.size()));
    }

    public Project findByIdAndAuthId(Long id)
    {
       return  projectRepository.findByIdAndAuthId(id,1L).orElseThrow(() -> new ProjectManagementException(ErrorType.PROJECT_NOT_FOUND));
    }

    public Boolean addTaskToProject(AddTaskToProjectDTO dto)
    {
        Project project = findByIdAndAuthId(dto.projectId());
        Task task = taskService.findByIdAndAuthId(dto.taskId());
        project.getTasks().add(task);
        projectRepository.save(project);
        return true;
    }
}

