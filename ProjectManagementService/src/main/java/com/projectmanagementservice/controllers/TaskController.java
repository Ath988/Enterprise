package com.projectmanagementservice.controllers;

import com.projectmanagementservice.dto.request.*;
import com.projectmanagementservice.dto.response.BaseResponse;
import com.projectmanagementservice.entities.BaseEntity;
import com.projectmanagementservice.entities.Task;
import com.projectmanagementservice.services.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.projectmanagementservice.constants.Endpoints.*;

@RestController
@RequestMapping(ROOT+TASK)
@RequiredArgsConstructor
@CrossOrigin("*")
public class TaskController
{
    private final TaskService taskService;

    @PostMapping(SAVE)
    public ResponseEntity<BaseResponse<Boolean>> createTask(@RequestBody TaskSaveRequestDTO dto){
        //TODO: token kontrollu yapilacak!
        return ResponseEntity.ok(BaseResponse.<Boolean>builder()
                        .code(200)
                        .message("Task olusturuldu!")
                        .success(true)
                .data(taskService.createTask(dto))
                .build());
    }

    @DeleteMapping(DELETE)
    public ResponseEntity<BaseResponse<Boolean>> deleteTask(TaskDeleteRequest dto){
        //TODO: token kontrollu yapilacak!
        return ResponseEntity.ok(BaseResponse.<Boolean>builder()
                        .success(true)
                        .message("Task silme islemi basariyla gerceklestirilmistir!")
                        .code(200)
                        .data(taskService.deleteTask(dto.taskId()))
                .build());
    }

    @PutMapping(UPDATE)
    public ResponseEntity<BaseResponse<Boolean>> update(@RequestBody TaskUpdateRequestDTO dto){
        //TODO: token kontrollu yapilacak!
        return ResponseEntity.ok(BaseResponse.<Boolean>builder()
                        .code(200)
                        .success(true)
                        .message("Task guncellendi!")
                        .data(taskService.updateTask(dto))
                .build());
    }

    @GetMapping(FIND_ALL)
    public ResponseEntity<BaseResponse<List<Task>>> findAll(@RequestParam Long projectId){
        //TODO: token kontrollu yapilacak!
        return ResponseEntity.ok(BaseResponse.<List<Task>>builder()
                        .data(taskService.findAllTaskByProjectId(projectId))
                        .code(200)
                        .message("Tum proje tasklari getirildi!")
                        .success(true)
                .build());
    }


    @PostMapping(ADD_USER_TO_TASK)
    public ResponseEntity<BaseResponse<Boolean>> addUserToTask(AddUserToTaskDTO dto){
        //TODO: token kontrollu yapilacak!
        return ResponseEntity.ok(BaseResponse.<Boolean>builder()
                        .message("Kullanici atandi!")
                        .code(200)
                        .success(true)
                        .data(taskService.addUserToTask(dto))
                .build());
    }

}
