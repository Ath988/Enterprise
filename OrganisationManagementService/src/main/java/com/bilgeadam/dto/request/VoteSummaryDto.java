package com.bilgeadam.dto.request;

public record VoteSummaryDto(
         Long questionId,
         Long totalLikes,
         Long totalDislikes
) {
}
