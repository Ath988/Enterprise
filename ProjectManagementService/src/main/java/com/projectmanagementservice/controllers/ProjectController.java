package com.projectmanagementservice.controllers;

import com.projectmanagementservice.dto.request.*;
import com.projectmanagementservice.dto.response.BaseResponse;
import com.projectmanagementservice.entities.Project;
import com.projectmanagementservice.services.ProjectService;
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
    public ResponseEntity<BaseResponse<Boolean>> save(@RequestBody ProjectSaveRequestDTO dto){
        //TODO: token kontrolu yapilacak!
        return ResponseEntity.ok(BaseResponse.<Boolean>builder()
                        .code(200)
                        .success(true)
                        .message("Proje basarili bir sekilde kaydedildi!")
                        .data(projectService.save(dto))
                .build());
    }

    @DeleteMapping(DELETE)
    public ResponseEntity<BaseResponse<Boolean>> delete(ProjectDeleteRequest dto){
        //TODO: token kontrolu yapilacak!
        return ResponseEntity.ok(BaseResponse.<Boolean>builder()
                        .data(projectService.delete(dto))
                        .code(200)
                        .success(true)
                        .message("Proje basarili bir sekilde silindi!")
                .build());
    }

    @PutMapping(UPDATE)
    public ResponseEntity<BaseResponse<Boolean>> updateProjectDetails(@RequestBody ProjectUpdateRequestDTO dto){
        //TODO: token kontrolu yapilacak!
        return ResponseEntity.ok(BaseResponse.<Boolean>builder()
                        .success(true)
                        .message("Proje bilgileri guncellenmistir!")
                        .data(projectService.updateProjectDetails(dto))
                        .code(200)
                .build());
    }

    @GetMapping(FIND_ALL)
    public ResponseEntity<BaseResponse<List<Project>>> findAllProjects(@RequestParam(value = "token") String token){
        //TODO: token kontrolu yapilacak!
        Long authId = Long.parseLong(token); //Token onaylandigi senaryo
        return ResponseEntity.ok(BaseResponse.<List<Project>>builder()
                        .code(200)
                        .success(true)
                        .message("Proje listesi getirildi!")
                        .data(projectService.findAllProjectsByOrganizationId(authId))
                .build());
    }
}
