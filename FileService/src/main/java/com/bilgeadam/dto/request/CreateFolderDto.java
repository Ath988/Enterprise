package com.bilgeadam.dto.request;

public record CreateFolderDto(
        String token,
        Long parentId,
        String folderName
) {
}
