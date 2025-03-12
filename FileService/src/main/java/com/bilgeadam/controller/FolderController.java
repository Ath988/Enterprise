package com.bilgeadam.controller;

import com.bilgeadam.dto.request.CreateFolderDto;
import com.bilgeadam.dto.request.FolderDeleteRequestDto;
import com.bilgeadam.dto.request.UpdateFolderNameRequestDto;
import com.bilgeadam.dto.response.BaseResponse;
import com.bilgeadam.dto.response.FolderListResponseDto;
import com.bilgeadam.entity.Folder;
import com.bilgeadam.service.FolderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.bilgeadam.constants.RestApis.*;

@RequiredArgsConstructor
@RestController
@RequestMapping(FOLDER)
public class FolderController {

    private final FolderService folderService;

    @PostMapping(CREATE_FOLDER)
    public ResponseEntity<BaseResponse<Boolean>> createFolder(@RequestBody CreateFolderDto dto) {
        return ResponseEntity.ok(BaseResponse.<Boolean>builder()
                        .code(200)
                        .data(folderService.createFolder(dto))
                        .message("Klasor basariyla olusturuldu!")
                        .success(true)
                .build());
    }

    @GetMapping(LIST_FOLDER + "/{folderPath}")
    public ResponseEntity<BaseResponse<List<FolderListResponseDto>>> listFolders(@PathVariable(value = "folderPath") String folderPath) {
        return ResponseEntity.ok(BaseResponse.<List<FolderListResponseDto>>builder()
                        .success(true)
                        .message("Klasor listesi getirildi!")
                        .data(folderService.listFoldersDto(folderPath))
                        .code(200)
                .build());
    }

    @PutMapping(UPDATE_FOLDER)
    public ResponseEntity<BaseResponse<Boolean>> updateFolderName(@RequestBody UpdateFolderNameRequestDto dto){
       return ResponseEntity.ok(BaseResponse.<Boolean>builder()
                        .code(200)
                        .data(folderService.updateFolderName(dto))
                        .message("Klasor ismi degistirildi!")
                        .success(true)
                .build());
    }

    @DeleteMapping(DELETE_FOLDER)
    public ResponseEntity<BaseResponse<Boolean>> deleteFolder(@RequestBody FolderDeleteRequestDto dto){
        return ResponseEntity.ok(BaseResponse.<Boolean>builder()
                        .success(true)
                        .message("Klasor basariyla silindi!")
                        .code(200)
                        .data(folderService.deleteFolder(dto))
                .build());
    }
}
