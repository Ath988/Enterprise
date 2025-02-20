package com.bilgeadam.dto.request;

public record UpdateFolderNameDto(
        String token,
        Long folderId,
        String newFolderName
) {
}
