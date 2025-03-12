package com.bilgeadam.dto.response;

import com.bilgeadam.view.VwFile;

import javax.swing.filechooser.FileView;
import java.io.File;
import java.util.List;

public record FolderListResponseDto(
        String folderName,
        String folderPath,
        List<VwFile> fileList
) {
}
