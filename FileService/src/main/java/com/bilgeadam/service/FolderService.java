package com.bilgeadam.service;

import com.bilgeadam.dto.request.CreateFolderDto;
import com.bilgeadam.dto.request.FolderDeleteRequestDto;
import com.bilgeadam.dto.request.UpdateFolderNameRequestDto;
import com.bilgeadam.dto.response.FolderListResponseDto;
import com.bilgeadam.entity.Folder;
import com.bilgeadam.exception.ErrorType;
import com.bilgeadam.exception.FileServiceException;
import com.bilgeadam.repository.FolderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class FolderService {
    private final FolderRepository folderRepository;
    private final FileService fileService;

    public Boolean createFolder(CreateFolderDto dto) {

        if (dto.folderName() == null || dto.folderName().isEmpty()) {
            throw new FileServiceException(ErrorType.FOLDER_CREATE_ERROR);
        }
        List<Folder> folders = listFolders(dto.folderPath());

        if (folders.stream().anyMatch(f -> f.getFolderName().equals(dto.folderName()))) {
            throw new FileServiceException(ErrorType.FOLDER_ALREADY_EXIST);
        }


        Folder folder;
        if (dto.folderPath() == null || dto.folderPath().isEmpty()) {
            folder = Folder.builder()
                    .folderName(dto.folderName())
                    .folderPath("root")
                    .build();
        } else {
            folder = Folder.builder()
                    .folderName(dto.folderName())
                    .folderPath(dto.folderPath())
                    .build();
        }

        folderRepository.save(folder);
        return true;
    }

    public List<Folder> listFolders(String folderPath) {
        if (folderPath == null || folderPath.isEmpty()) {
            return folderRepository.findAllByFolderPath("root");
        }
        return folderRepository.findAllByFolderPath(folderPath);
    }

    public List<FolderListResponseDto> listFoldersDto(String folderPath) {
        List<Folder> folderList;

        if (folderPath == null || folderPath.isEmpty()) {
            folderList = folderRepository.findAllByFolderPath("root");
        } else {
            folderList = folderRepository.findAllByFolderPath(folderPath);
        }

        Map<String, List<String>> fileMap = folderList.stream()
                .collect(Collectors.toMap(Folder::getFolderName, Folder::getFileIdList));

        return folderList.stream()
                .map(folder -> new FolderListResponseDto(
                        folder.getFolderName(),
                        folder.getFolderPath(),
                        fileService.getFileByMap(fileMap.get(folder.getFolderName()))
                ))
                .collect(Collectors.toList());
    }

    public Boolean updateFolderName(UpdateFolderNameRequestDto dto) {
        if (dto.newFolderName() == null || dto.newFolderName().isEmpty()) {
            throw new FileServiceException(ErrorType.FOLDER_CREATE_ERROR);
        }
        List<Folder> folders = listFolders(dto.folderPath());

        if (folders.stream().anyMatch(f -> f.getFolderName().equals(dto.newFolderName()))) {
            throw new FileServiceException(ErrorType.FOLDER_ALREADY_EXIST);
        }
        Optional<Folder> folderOptional = folderRepository.findByFolderNameAndFolderPath(dto.oldFolderName(), dto.folderPath());
        if (folderOptional.isEmpty()) {
            throw new FileServiceException(ErrorType.FOLDER_NOT_FOUND);
        }

        folders = listFolders(dto.oldFolderName());
        if(!folders.isEmpty()){
            folders.forEach(f -> f.setFolderPath(dto.newFolderName()));
            folderRepository.saveAll(folders);
        }

        Folder folder = folderOptional.get();
        folder.setFolderName(dto.newFolderName());
        folderRepository.save(folder);

        return true;
    }

    public Boolean deleteFolder(FolderDeleteRequestDto dto) {
        Optional<Folder> folderOptional = folderRepository.findByFolderNameAndFolderPath(dto.folderName(), dto.folderPath());
        if (folderOptional.isEmpty()) {
            throw new FileServiceException(ErrorType.FOLDER_NOT_FOUND);
        }
        folderRepository.delete(folderOptional.get());
        List<Folder> folders = listFolders(dto.folderName());
        if(!folders.isEmpty()){
            folderRepository.deleteAll(folders);
        }
        return true;
    }
}
