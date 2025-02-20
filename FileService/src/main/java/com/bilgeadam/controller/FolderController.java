package com.bilgeadam.controller;

import com.bilgeadam.dto.request.CreateFolderDto;
import com.bilgeadam.dto.request.MoveFolderDto;
import com.bilgeadam.dto.request.UpdateFolderNameDto;
import com.bilgeadam.dto.response.BaseResponse;
import com.bilgeadam.dto.response.FolderFileResponseDto;
import com.bilgeadam.service.FolderService;
import lombok.RequiredArgsConstructor;
import org.simpleframework.xml.Path;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.bilgeadam.constants.RestApis.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(FOLDER)
public class FolderController {
    private final FolderService folderService;

    @PostMapping(CREATE_FOLDER)
    public ResponseEntity<BaseResponse<Boolean>> createFolder(@RequestBody CreateFolderDto dto) {
        return ResponseEntity.ok(BaseResponse.<Boolean>builder()
                        .code(200)
                        .message("Klasor basariyla olusturuldu!")
                        .data(folderService.createFolder(dto))
                        .success(true)
                .build());
    }

    @PutMapping(UPDATE_FOLDER)
    public ResponseEntity<BaseResponse<Boolean>> updateFolderName(@RequestBody UpdateFolderNameDto dto){
        return ResponseEntity.ok(BaseResponse.<Boolean>builder()
                        .code(200)
                        .success(true)
                        .data(folderService.updateFolderName(dto))
                        .message("Klasor ismi basariyla guncellendi!")
                .build());
    }

    @PutMapping(MOVE_FOLDER)
    public  ResponseEntity<BaseResponse<Boolean>> moveFolder(@RequestBody MoveFolderDto dto){
        return ResponseEntity.ok(BaseResponse.<Boolean>builder()
                        .code(200)
                        .message("Klasor basariyla tasindi!")
                        .data(folderService.moveFolder(dto))
                        .success(true)
                .build());
    }
    @GetMapping(LIST_FOLDER+ "{folderName}")
    public ResponseEntity<BaseResponse<List<FolderFileResponseDto>>> listFoldersAndFiles(@PathVariable String folderName){
        return ResponseEntity.ok(BaseResponse.<List<FolderFileResponseDto>>builder()
                        .success(true)
                        .message("Root klasoru basariyla listelendi!")
                        .code(200)
                        .data(folderService.listFoldersAndFiles(folderName))
                .build());
    }
    @DeleteMapping(DELETE_FOLDER+"{id}")
    public  ResponseEntity<BaseResponse<Boolean>> deleteFolder(@PathVariable Long id){
        return ResponseEntity.ok(BaseResponse.<Boolean>builder()
                        .data(folderService.deleteFolder(id))
                        .message("klasor basari ile silindi!")
                        .success(true)
                        .code(200)
                .build());
    }
}
