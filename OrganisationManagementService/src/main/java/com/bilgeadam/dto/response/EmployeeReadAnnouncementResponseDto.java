package com.bilgeadam.dto.response;

public record EmployeeReadAnnouncementResponseDto(
        Long announcementId, // Duyuru ID'si
        String name,
        String surname
) {
}
