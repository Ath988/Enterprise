package com.bilgeadam.dto.request;

public record ReplyRequestDto(
         Long questionId, // Cevabın bağlı olduğu soru
         String text    // Cevap içeriği
) {
}
