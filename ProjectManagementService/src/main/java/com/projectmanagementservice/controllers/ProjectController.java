package com.projectmanagementservice.controllers;

import com.projectmanagementservice.dto.request.ProjectSaveRequestDTO;
import com.projectmanagementservice.services.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}
