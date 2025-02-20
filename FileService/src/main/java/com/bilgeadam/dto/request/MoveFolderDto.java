package com.bilgeadam.dto.request;

public record MoveFolderDto(
        String token,
        Long folderId,
        String newParentFolderName

) {
}
