package com.projectmanagementservice.controllers;

import com.projectmanagementservice.dto.request.*;
import com.projectmanagementservice.entities.Project;
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
    public ResponseEntity<Boolean> save(@RequestBody TaskSaveRequestDTO dto){

        return ResponseEntity.ok(taskService.save(dto));
    }

    @DeleteMapping(DELETE)
    public ResponseEntity<Boolean> delete(Long id){

        return ResponseEntity.ok(taskService.delete(id));
    }

    @PutMapping(UPDATE)
    public ResponseEntity<Boolean> update(@RequestBody TaskUpdateRequestDTO dto){

        return ResponseEntity.ok(taskService.update(dto));
    }

    @PostMapping(FIND_ALL)
    public ResponseEntity<List<Task>> findAll(@RequestBody PageRequestDTO dto){

        return ResponseEntity.ok(taskService.findAllByNameContainingIgnoreCaseAndStatusIsNotAndAuthIdOrderByNameAsc(dto));
    }

    @PostMapping(FIND_BY_ID)
    public ResponseEntity<Task> findByIdAndAuthId(Long id){

        return ResponseEntity.ok(taskService.findByIdAndAuthId(id));
    }

    @PostMapping(ADD_USER_TO_TASK)
    public ResponseEntity<Boolean> addUserToTask(AddUserToTaskDTO dto){

        return ResponseEntity.ok(taskService.addUserToTask(dto));
    }

}
