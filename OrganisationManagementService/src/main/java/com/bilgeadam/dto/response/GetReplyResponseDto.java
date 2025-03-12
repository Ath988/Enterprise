package com.bilgeadam.dto.response;

import java.time.LocalDateTime;

public record GetReplyResponseDto(
        String text,
        String authorName,  // Yazarın ismi
        LocalDateTime date
) {
}
