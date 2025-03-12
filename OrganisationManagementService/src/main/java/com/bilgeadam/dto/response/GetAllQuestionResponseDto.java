package com.bilgeadam.dto.response;

import java.time.LocalDateTime;

public record GetAllQuestionResponseDto(

        Long id,
        String text,
        String eCategory,
        String authorName,  // Author'un ismi ve soyismi
        LocalDateTime date
) {
}
