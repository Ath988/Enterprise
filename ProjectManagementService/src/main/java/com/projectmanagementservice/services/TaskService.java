package com.projectmanagementservice.services;

import com.projectmanagementservice.dto.request.*;
import com.projectmanagementservice.entities.Task;
import com.projectmanagementservice.entities.enums.EStatus;
import com.projectmanagementservice.exceptions.ErrorType;
import com.projectmanagementservice.exceptions.ProjectManagementException;
import com.projectmanagementservice.repositories.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService
{
    private final TaskRepository taskRepository;

    public Boolean createTask(TaskSaveRequestDTO dto) {

        taskRepository.save(Task
                .builder()
                .name(dto.name())
                        .projectId(dto.projectId())
                .description(dto.description())
                .build());
        return true;
    }

    public Boolean deleteTask(Long id)
    {
        Task task = taskRepository.findById(id).orElseThrow(() -> new ProjectManagementException(ErrorType.TASK_NOT_FOUND));
        task.setStatus(EStatus.DELETED);
        taskRepository.save(task);
        return true;
    }

    public Boolean updateTask(TaskUpdateRequestDTO dto)
    {
        Task task = taskRepository.findById(dto.id()).orElseThrow(() -> new ProjectManagementException(ErrorType.TASK_NOT_FOUND));
        task.setName(dto.name());
        task.setAuthId(dto.authId() == null ? 0L : dto.authId());
        task.setDescription(dto.description());
        taskRepository.save(task);
        return true;
    }

    public List<Task> findAllTaskByProjectId(Long projectId) {
        return taskRepository.findAllByProjectId(projectId);
    }

    public Boolean addUserToTask(AddUserToTaskDTO dto){
        Task task = taskRepository.findById(dto.taskId()).orElseThrow(() -> new ProjectManagementException(ErrorType.TASK_NOT_FOUND));
        task.setAuthId(dto.userId());
        taskRepository.save(task);
        return true;
    }


}

