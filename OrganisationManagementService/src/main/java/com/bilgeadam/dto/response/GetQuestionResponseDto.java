package com.bilgeadam.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public record GetQuestionResponseDto(
        Long id,
        String text,
        String eCategory,
        String authorName,  // Author'un ismi ve soyismi
        LocalDateTime date,
        List<GetReplyResponseDto> replies  // Cevapları tutacak alan
) {
}
