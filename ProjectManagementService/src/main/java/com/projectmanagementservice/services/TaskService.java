package com.projectmanagementservice.services;

import com.projectmanagementservice.dto.request.*;
import com.projectmanagementservice.entities.Project;
import com.projectmanagementservice.entities.Task;
import com.projectmanagementservice.entities.User;
import com.projectmanagementservice.entities.enums.EStatus;
import com.projectmanagementservice.exceptions.ErrorType;
import com.projectmanagementservice.exceptions.ProjectManagementException;
import com.projectmanagementservice.repositories.ProjectRepository;
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
    private final UserService userService;

    //TODO Gelen istekten hangi kullanıcının olduğu bilgisi gelince metodlardaki sabit "1L" değerleri değiştirilecek.
    public Boolean save(TaskSaveRequestDTO dto)
    {
        taskRepository.save(Task
                .builder()
                .name(dto.name())
                        .authId(1L)
                .description(dto.description())
                .build());
        return true;
    }

    public Boolean delete(Long id)
    {
        Task task = taskRepository.findByIdAndAuthId(id,1L).orElseThrow(() -> new ProjectManagementException(ErrorType.TASK_NOT_FOUND));
        task.setStatus(EStatus.DELETED);
        taskRepository.save(task);
        return true;
    }

    public Boolean update(TaskUpdateRequestDTO dto)
    {
        Task task = taskRepository.findByIdAndAuthId(dto.id(),1L).orElseThrow(() -> new ProjectManagementException(ErrorType.TASK_NOT_FOUND));
        task.setName(dto.name());
        task.setDescription(dto.description());
        taskRepository.save(task);
        return true;
    }


    public List<Task> findAllByNameContainingIgnoreCaseAndStatusIsNotAndAuthIdOrderByNameAsc(PageRequestDTO dto)
    {
        return taskRepository.findAllByNameContainingIgnoreCaseAndStatusIsNotAndAuthIdOrderByNameAsc(dto.searchText(), EStatus.DELETED,1L, PageRequest.of(dto.page(), dto.size()));
    }

    public Task findByIdAndAuthId(Long id)
    {
       return  taskRepository.findByIdAndAuthId(id,1L).orElseThrow(() -> new ProjectManagementException(ErrorType.TASK_NOT_FOUND));
    }

    public Boolean addUserToTask(AddUserToTaskDTO dto)
    {
        Task task = taskRepository.findByIdAndAuthId(dto.taskId(), 1L).orElseThrow(() -> new ProjectManagementException(ErrorType.TASK_NOT_FOUND));
        User user = userService.findByIdAndAuthId(dto.userId());
        task.setUser(user);
        return true;
    }
}

