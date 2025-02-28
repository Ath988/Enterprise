package com.bilgeadam.service;

import com.bilgeadam.dto.request.ReplyRequestDto;
import com.bilgeadam.entity.Reply;
import com.bilgeadam.repository.ReplyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReplyService {

    private final ReplyRepository replyRepository;

    /**
     * Yeni bir cevap oluşturur.
     */
    public boolean createReply(Long employeeId, ReplyRequestDto dto) {
        // Yeni Reply nesnesi oluşturuluyor
        Reply reply = Reply.builder()
                .questionId(dto.questionId()) // Sorunun ID'si
                .employeeId(employeeId) // Yazarın ID'si (token'dan alınan employeeId)
                .text(dto.text()) // Cevap metni
                .date(LocalDateTime.now()) // Cevap tarihi
                .build();

        // Veritabanına kaydediliyor
        replyRepository.save(reply);

        return true; // Başarıyla kaydedildi
    }

    public List<Reply> findByQuestionId(Long questionId) {
        return replyRepository.findByQuestionId(questionId);
    }
}
