package com.bilgeadam.dto.response;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record AnnouncementReadResponseDto(
        Long id,
        String title,
        String content,
        Boolean isRead,
        LocalDate creationDate
) {
}
