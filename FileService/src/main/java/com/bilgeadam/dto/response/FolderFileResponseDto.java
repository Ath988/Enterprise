package com.bilgeadam.dto.response;

import com.bilgeadam.entity.FileInfo;

import java.util.List;

public record FolderFileResponseDto(
        Long folderId,
        String folderName,
        List<FileInfo> fileInfoList
) {
}
