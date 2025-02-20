package com.bilgeadam.service;

import com.bilgeadam.dto.request.CreateFolderDto;
import com.bilgeadam.dto.request.MoveFolderDto;
import com.bilgeadam.dto.request.UpdateFolderNameDto;
import com.bilgeadam.dto.response.FolderFileResponseDto;
import com.bilgeadam.entity.FileInfo;
import com.bilgeadam.entity.FolderManagement;
import com.bilgeadam.entity.enums.EState;
import com.bilgeadam.exception.ErrorType;
import com.bilgeadam.exception.FileServiceException;
import com.bilgeadam.repository.FolderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FolderService {
    private final FolderRepository folderRepository;
    private final FileService fileService;

    public Boolean createFolder(CreateFolderDto dto) {
        if (folderRepository.countByOrganizationId(1L) == 0) {
            folderRepository.save(FolderManagement.builder()
                    .folderName("root")
                    .organizationId(1L) //TODO: daha sonrasinda organizasyonu bulup getirilmesi gereklidir.
                    .build());

        }
        folderRepository.save(FolderManagement.builder()
                        .organizationId(1L) //TODO: daha sonrasinda organizasyonu bulup getirilmesi gereklidir.
                        .folderName(dto.folderName())
                        .parentId(dto.parentId())
                .build());
        return true;

    }

    public Boolean updateFolderName(UpdateFolderNameDto dto) {
        Optional<FolderManagement> optionalFolderManagement = folderRepository.findById(dto.folderId());
        if (optionalFolderManagement.isEmpty()){
            throw new FileServiceException(ErrorType.FOLDER_NOT_FOUND);
        }
        FolderManagement folderManagement = optionalFolderManagement.get();
        folderManagement.setFolderName(dto.newFolderName());
        folderRepository.save(folderManagement);

        return true;
    }

    public Boolean moveFolder(MoveFolderDto dto) {
        Optional<FolderManagement> optionalFolderManagement = folderRepository.findById(dto.folderId());
        if (optionalFolderManagement.isEmpty()){
            throw new FileServiceException(ErrorType.FOLDER_NOT_FOUND);
        }
        FolderManagement folderManagement = optionalFolderManagement.get();
        if (dto.newParentFolderName().equals("root")){
            folderManagement.setParentId(1L);
        }
        else{
            Optional<FolderManagement> optionalParentFolderManagement = folderRepository.findByFolderName(dto.newParentFolderName());
            if (optionalParentFolderManagement.isEmpty()){
                throw new FileServiceException(ErrorType.FOLDER_NOT_FOUND);
            }
            FolderManagement parentFolderManagement = optionalParentFolderManagement.get();
            folderManagement.setParentId(parentFolderManagement.getId());
        }
        folderRepository.save(folderManagement);

        return true;
    }

    public List<FolderFileResponseDto> listFoldersAndFiles(String folderName) {
        Long parentFolderId = folderRepository.findFolderIdByFolderName(folderName).get();
        List<FolderManagement> folderManagementList = folderRepository.findAllByParentIdAndOrganizationIdAndState(parentFolderId, 1L, EState.ACTIVE);
        return folderManagementList.stream()
                .map(folder -> {
                    List<FileInfo> fileInfoList = fileService.findAllById(folder.getFileIds());

                    return new FolderFileResponseDto(folder.getId(), folder.getFolderName(), fileInfoList);
                })
                .toList();

    }

    public Boolean deleteFolder(Long id) {
        Optional<FolderManagement> optionalFolderManagement = folderRepository.findById(id);
        if (optionalFolderManagement.isEmpty()){
            throw new FileServiceException(ErrorType.FOLDER_NOT_FOUND);
        }
        FolderManagement folderManagement = optionalFolderManagement.get();
        if (folderManagement.getId().equals(1L)){
            throw new FileServiceException(ErrorType.FOLDER_CANNOT_DELETE);
        }
        folderManagement.setState(EState.PASSIVE);
        folderRepository.save(folderManagement);
        return true;
    }
}
