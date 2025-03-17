package com.bilgeadam.dto.request;

import org.springframework.web.multipart.MultipartFile;

public record SaveFileRequestDto(
        MultipartFile file,
        String folderPath
) {
}
