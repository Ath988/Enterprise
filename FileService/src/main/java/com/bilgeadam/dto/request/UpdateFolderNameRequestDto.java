package com.bilgeadam.dto.request;

public record UpdateFolderNameRequestDto(
        String oldFolderName,
        String newFolderName,
        String folderPath
) {
}
