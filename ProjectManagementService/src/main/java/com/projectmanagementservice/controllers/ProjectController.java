package com.projectmanagementservice.controllers;

import com.projectmanagementservice.dto.request.PageRequestDTO;
import com.projectmanagementservice.dto.request.ProjectSaveRequestDTO;
import com.projectmanagementservice.dto.request.ProjectUpdateRequestDTO;
import com.projectmanagementservice.entities.Project;
import com.projectmanagementservice.services.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.projectmanagementservice.constants.Endpoints.*;

@RestController
@RequestMapping(ROOT+PROJECT)
@RequiredArgsConstructor
@CrossOrigin("*")
public class ProjectController
{
    private final ProjectService projectService;

    @PostMapping(SAVE)
    public ResponseEntity<Boolean> save(@RequestBody ProjectSaveRequestDTO dto){

        return ResponseEntity.ok(projectService.save(dto));
    }

    @DeleteMapping(DELETE)
    public ResponseEntity<Boolean> delete(Long id){

        return ResponseEntity.ok(projectService.delete(id));
    }

    @PutMapping(UPDATE)
    public ResponseEntity<Boolean> update(@RequestBody ProjectUpdateRequestDTO dto){

        return ResponseEntity.ok(projectService.update(dto));
    }

    @PostMapping(FIND_ALL)
    public ResponseEntity<List<Project>> findAll(@RequestBody PageRequestDTO dto){

        return ResponseEntity.ok(projectService.findAllByNameContainingIgnoreCaseAndStatusIsNotAndAuthIdOrderByNameAsc(dto));
    }

    @PostMapping(FIND_BY_ID)
    public ResponseEntity<Project> findByIdAndAuthId(Long id){

        return ResponseEntity.ok(projectService.findByIdAndAuthId(id));
    }

}
