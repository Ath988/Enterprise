package com.projectmanagementservice.services;

import com.projectmanagementservice.dto.request.*;
import com.projectmanagementservice.dto.response.AllEmployeeResponse;
import com.projectmanagementservice.dto.response.BaseResponse;
import com.projectmanagementservice.entities.Task;
import com.projectmanagementservice.entities.enums.EStatus;
import com.projectmanagementservice.exceptions.ErrorType;
import com.projectmanagementservice.exceptions.ProjectManagementException;
import com.projectmanagementservice.manager.AuthManager;
import com.projectmanagementservice.manager.OrganisationManager;
import com.projectmanagementservice.repositories.TaskRepository;
import com.projectmanagementservice.utility.ETaskPriorityStatus;
import com.projectmanagementservice.utility.ETaskStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService
{
    private final TaskRepository taskRepository;
    private final AuthManager authManager;
    private final OrganisationManager organisationManager;
    public Boolean createTask(TaskSaveRequestDTO dto) {
        ResponseEntity<BaseResponse<Long>> authUserId = authManager.authUserId(dto.token());
        Long authId = authUserId.getBody().getData();
        taskRepository.save(Task
                .builder()
                .authId(authId)
                .projectId(dto.projectId())
                .name(dto.name())
                .taskPriorityStatus(dto.taskPriorityStatus())
                .description(dto.description())
                .taskStatus(dto.taskStatus())
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
    
    public Boolean updateTask(TaskUpdateRequestDTO dto) {
        Task task = taskRepository.findById(dto.id())
                                  .orElseThrow(() -> new ProjectManagementException(ErrorType.TASK_NOT_FOUND));
        
        // İsim ve açıklamayı güncelle
        task.setName(dto.name());
        task.setDescription(dto.description());
        
        // taskStatus String'ini ETaskStatus enum'una dönüştür
        ETaskStatus status;
        try {
            status = ETaskStatus.valueOf(dto.taskStatus().toString()); // Büyük/küçük harf uyumu sağlanır
        } catch (IllegalArgumentException e) {
            status = ETaskStatus.UNDEFINED; // Geçersiz bir değer geldiğinde UNDEFINED olarak ayarlanır
        }
        
        ETaskPriorityStatus priorityStatus;
        try {
            priorityStatus = ETaskPriorityStatus.valueOf(dto.taskPriorityStatus().toString());
        } catch (IllegalArgumentException e) {
            priorityStatus = ETaskPriorityStatus.UNDEFINED;
        }
        task.setTaskPriorityStatus(priorityStatus);
        // Dönüştürülmüş status'u ata
        task.setTaskStatus(status);
        
        // Veritabanında güncelle
        taskRepository.save(task);
        return true;
    }
    
    
    public List<Task> findAllTaskByProjectId(Long projectId) {
        return taskRepository.findAllByProjectIdAndStatus(projectId, EStatus.ACTIVE);
    }

    public Boolean addUserToTask(AddUserToTaskDTO dto){
        Task task = taskRepository.findById(dto.taskId()).orElseThrow(() -> new ProjectManagementException(ErrorType.TASK_NOT_FOUND));
        task.setAuthId(dto.userId());
        taskRepository.save(task);
        return true;
    }
    
    
    public Boolean updateStatus(TaskUpdateStatus dto) {
        Task task =
                taskRepository.findById(dto.taskId()).orElseThrow(() -> new ProjectManagementException(ErrorType.TASK_NOT_FOUND));
        task.setTaskStatus(dto.taskStatus());
        taskRepository.save(task);
        return true;
    }
    
    
    public List<AllEmployeeResponse> getEmployees(String token) {
        ResponseEntity<BaseResponse<Long>> authUserId = authManager.authUserId(token);
        Long authId = authUserId.getBody().getData();
        ResponseEntity<BaseResponse<Long>> companyIdByAuthId = organisationManager.getCompanyIdByAuthId(authId);
        Long companyId = companyIdByAuthId.getBody().getData();
        ResponseEntity<BaseResponse<List<AllEmployeeResponse>>> allEmployeesByCompanyId =
                organisationManager.getAllEmployeesByCompanyId(companyId);
        List<AllEmployeeResponse> listEmployee = allEmployeesByCompanyId.getBody().getData();
        return listEmployee;
    }
}